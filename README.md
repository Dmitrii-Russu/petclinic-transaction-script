# PetClinic — Transaction Script + CQRS with Spring JDBC

Another PetClinic? Yes — deliberately. It's the most recognizable teaching
domain in the Java ecosystem, so there's no need to explain what the app does.
We can go straight to how it's built.

**Highlights:**
- CQRS carried through at the **package** level, not just the class level
- Physical layer isolation — package-private implementations, exposed only
  through `*Factory` classes with a private constructor
- No ORM — raw `JdbcClient`, SQL kept in `.sql` resource files
- Single-query aggregate hydration via Postgres `jsonb_agg`/`LEFT JOIN LATERAL`
- Two independent UIs (REST + SSR/JTE) on top of the same use cases
- Deliberately a Transaction Script, not a rich domain model — see why below

The domain is deliberately simple, with no business logic. That pushes one
principle to the limit — "simple code is easier to develop, maintain, test,
and read."

## Why Transaction Script, not a Rich Domain Model

There is no business logic in the application — only CRUD operations on
owners, pets, and visits. A rich domain model earns its keep when there are
invariants and behavior worth encapsulating in an entity; there's no such
behavior here. Complicating the model for its own sake contradicts the
"simple code" goal, so a Transaction Script was chosen instead: `Owner`,
`Pet`, `Visit` are plain `record`s, with no methods and no state.

## Architecture

```
presentation  ──►  application (usecase)  ◄──  infrastructure
 (rest/ssr)            │
                     model
```

CQRS is carried through consistently at the package level, not just at the
class level:

| Layer | Package | Responsibility |
|---|---|---|
| Model | `model` | `Owner`, `Pet`, `Visit` — records with no outward dependencies. |
| Application / command | `application.command.usecase` / `.model` / `.repository` | Write use cases (`OwnerCreateUseCase`, `PetUpdateUseCase`, `VisitCreateUseCase`, ...), commands, and write ports. |
| Application / query | `application.query.usecase` / `.repository` / `.view` / `.catalog` | Read use cases, read ports, and view models tailored to a specific screen (`OwnerDetailsView`, `OwnerListView`, `SsrPetEditView`, `SsrVisitCreateView`...). |
| Infrastructure | `infrastructure.command`, `infrastructure.query`, `infrastructure.query.catalog`, `infrastructure.bootstrap` | JDBC adapters via `JdbcClient` (no ORM), plus Spring wiring (`@Configuration` classes exposing only interfaces as beans). |
| Presentation | `presentation.rest.*`, `presentation.ssr.*` | REST controllers (JSON) and SSR controllers (JTE templates), both on top of the same use cases. |
| Shared | `shared.pagination`, `shared.SqlLoader` | `PageQuery`, `PageResult`, `OwnerSearchCriteria` — cross-cutting pagination/search types — and a small loader that reads `.sql` resources into constants. |

**What's already guaranteed vs. what's planned.** Layer isolation is enforced
today, physically, by package-private `*ServiceImpl` / `*RepositoryImpl`
classes: nothing outside `application.*.service` / `infrastructure.*` can even
compile a reference to an implementation — only the interface, exposed through
the package's `All*Factory`. This holds regardless of tooling. ArchUnit is
listed as a dependency and is the intended way to turn this rule (plus command
↔ query non-dependence, and "no infrastructure exception ever leaves
`infrastructure`") into an automated regression check — those tests aren't
written yet, so today the guarantee comes from the compiler, not from a CI
gate.

## Key Decisions

### Isolation & wiring

**CQRS instead of one service per entity.** Commands and queries aren't just
different methods on one class — they live in different packages, with
different use cases, different repositories, and different view models.
`FindOwnerUseCase` and `OwnerCreateUseCase` know nothing about each other.

**Layer isolation via package visibility, not discipline alone.** Following a
zero-trust principle toward developer discipline, `*ServiceImpl` and
`*RepositoryImpl` are declared without `public`. Only the interface is visible
outside the package — exposed by one public class per package,
`All<Command|Query><Repository|Service>Factory`, each with a private
constructor. One factory covers every entity on its side of CQRS (all command
repositories, all query repositories, and so on) rather than one factory per
entity — this keeps the physical compile-time barrier without one extra class
per aggregate. `@Bean` methods in `infrastructure.bootstrap` return only
interfaces, never implementations.

**Use cases split into packages by action, not by a CRUD facade.** SSR exposes
more operations than REST (separate create/edit forms, name-lookup helpers for
forms), so instead of one "fat" service per entity, use cases are one
interface per action (`OwnerCreateUseCase`, `OwnerUpdateUseCase`,
`SsrOwnerEditFormUseCase`, `FindOwnerListUseCase`...). No implementation drags
in methods it doesn't need.

### Data & consistency

**Validation and three invariants.** Input validation (format, required
fields) is delegated to controllers via Jakarta Bean Validation — deliberately
not duplicated in services. The domain has exactly three meaningful
constraints, each an existence/uniqueness check rather than real business
logic:
- an owner's phone number must be unique (`owners.telephone UNIQUE`);
- an owner cannot have two identical pets — equality by fields, not id
  (`uq_pet_owner UNIQUE(owner_id, name, birth_date, type)`);
- a pet cannot have two visits on the same date
  (`uq_visit_pet_date UNIQUE(pet_id, visit_date)`).

All three are delegated to unique constraints in the database rather than
in-code existence checks: an in-memory or extra-query check degrades at scale
and doesn't protect against TOCTOU. The SQL exception is caught at the
`infrastructure.command` boundary and translated into a standard Java
exception — only that exception is visible further up the call stack.

**Optimistic duplicate pre-check via cache.** Before writing to the database,
`OwnerCreateRepositoryImpl`, `PetCreateRepositoryImpl`, and
`VisitCreateRepositoryImpl` each perform a fast lookup in a Spring `Cache`
keyed by the same business fields the unique constraint covers, to skip an
avoidable round trip for a conflict that's already predictable. The unique
constraint in the database remains the actual source of truth
(`DataIntegrityViolationException` → `ConstraintViolationTranslator`); the
cache only reduces load on the hot path, it doesn't replace the guarantee.
This is a per-instance optimization — with more than one running instance the
cache won't be consistent across them, so the database constraint is what
actually prevents a duplicate, not the cache.

**Shared constraint-translation mechanics, per-entity mapping.** The
try/catch/translate flow lives once, in a generic
`ConstraintViolationTranslator<T>` (`infrastructure.command.support`) that
takes a `Map<String, ExceptionFactory<T>>` from Postgres constraint name to a
Java exception factory. `OwnerConstraintViolationTranslator`,
`PetConstraintViolationTranslator`, and `VisitConstraintViolationTranslator`
each supply only their own map — the part that's genuinely different per
entity (which constraint means what) stays local, the part that's identical
(catching the exception, reading the SQLState/constraint name) is shared.

**Identifiers are UUIDs, assigned by the application.** The id is generated in
code (`UUID.randomUUID().toString()`) before the row is written, not by the
database — the id is available to the caller immediately on success, without a
follow-up query for a generated key.

**Writes with no intermediate state.** Create/update operations write the data
and immediately return the result to the caller — on success as well as on
conflict — with no extra check queries before or after the write.

### Persistence

**No ORM — `JdbcClient`.** The persistence layer is built on `JdbcClient`
(Spring 6.1+) instead of `JdbcTemplate` or JPA/Hibernate. SQL lives in
`.sql` resource files under `src/main/resources/sql/{command,query}`, loaded
once into `static final String` constants via `SqlLoader`.

**Reading an aggregate — single-query hydration.** `OwnerDetailsView` (an
owner plus their pets plus each pet's visits) is assembled with one SQL query
using `LEFT JOIN LATERAL` + `jsonb_agg`/`jsonb_build_object`, and the resulting
JSON column is deserialized into the view's nested records by a shared
`ViewExtractor` (`infrastructure.query.support`) — no N+1 queries, no
hand-rolled in-memory graph assembly per aggregate. This intentionally couples
the read side to PostgreSQL-specific JSON functions; the trade-off accepted
here is fewer classes and one round trip, in exchange for not being portable
to another RDBMS — acceptable since PostgreSQL is the only target.

### Models & errors

**Models are records, exceptions are standard Java types.** `Owner`, `Pet`,
`Visit`, and every command/view object are immutable records. There are no
custom domain exceptions: standard Java exceptions
(`IllegalStateException`/`IllegalArgumentException`/`NoSuchElementException`)
plus Bean Validation exceptions are used to convey the cause of an error — the
consuming code branches on HTTP status / exception type in REST, and the
message text is what actually reaches the user in both REST and SSR. The
deliberate exception to "just records" is `presentation.ssr.dto`
(`OwnerFormDto`, `PetFormDto`, `VisitFormDto`): mutable JavaBeans with a no-arg
constructor and setters, required by Spring MVC form binding, which cannot
populate a record's canonical constructor from request parameters.

**Two UIs, one core.** REST and SSR controllers are independent consumers of
the same application use cases. Errors are handled separately, per protocol:
REST returns RFC 9457 Problem Details (`RestExceptionHandler`), SSR re-renders
the originating template with an error message, recovering the form state
from a request attribute the controller set before invoking the use case.

### API design

**REST responses are the query views, by design — not a temporary shortcut.**
`RestOwnerQueryController` serializes `OwnerDetailsView` / `PageResult<OwnerListView>`
directly as JSON, with no separate `presentation.rest.response` mapping layer.
This is deliberate, not an omission the DTO layer will "fix" later:

- Every query view already exists *because* of CQRS, not for REST's sake —
  it's a projection built for one specific screen/use case (`OwnerListView`
  has only what the search results table needs, `OwnerDetailsView` only what
  the detail page needs). That's exactly the job a REST response DTO would
  otherwise do — adding a second, hand-mapped copy of the same shape wouldn't
  decouple anything, it would just duplicate it.
- There is no API versioning or external-consumer contract to protect here
  (a single first-party client, the SSR pages, consumes the same use cases
  directly). An extra mapping layer earns its cost when the wire contract
  needs to evolve independently of the read model — that trigger hasn't
  happened.
- The boundary that actually matters — not leaking `Owner`/`Pet`/`Visit`
  domain records or infrastructure types (`JdbcClient`, SQL exceptions) across
  the wire — is already held: only `application.query.view` records ever
  reach a controller, REST or SSR.

If REST and SSR ever need genuinely different shapes for the same read (e.g.
a mobile client wanting a slimmer payload than the HTML page), that's the
signal to introduce a `presentation.rest.response` mapping layer — not before.

**Flat routes instead of nested paths.** Controllers avoid nesting like
`/owners/{ownerId}/pets/{petId}`: a pet is addressed as `/pets/{petId}/edit`,
a new pet as `/pets/new?ownerId=...`. The REST API has no standalone
`GET /api/pets/{id}` or `GET /api/visits/{id}` — pets and visits are only
readable as part of the owner aggregate.

## Project Structure

```
src/main/java/.../petclinic/
├── model/                          # Owner, Pet, Visit — records, no outward dependencies
├── application/
│   ├── command/
│   │   ├── usecase/                # OwnerCreateUseCase, PetUpdateUseCase, VisitCreateUseCase...
│   │   ├── model/                  # CreateOwnerCommand, UpdatePetCommand...
│   │   ├── repository/             # write ports: OwnerCreateRepository, PetUpdateRepository...
│   │   └── service/                # package-private impls, exposed via AllCommandServiceFactory
│   └── query/
│       ├── usecase/                # FindOwnerUseCase, FindOwnerListUseCase, SsrPetEditFormUseCase...
│       ├── view/                   # OwnerDetailsView, OwnerListView, SsrPetEditView, SsrVisitCreateView...
│       ├── repository/             # read ports: FindOwnerRepository, SsrOwnerEditFormRepository...
│       ├── catalog/                # PetTypeCatalog
│       └── service/                # package-private impls, exposed via AllQueryServiceFactory
├── infrastructure/
│   ├── command/
│   │   └── support/                # ConstraintViolationTranslator<T> + per-entity constraint maps
│   ├── query/
│   │   ├── support/                # ViewExtractor — shared ResultSet → view mapping
│   │   └── catalog/                # PetTypeCatalog JDBC adapter
│   └── bootstrap/                  # @Configuration: wires factories into Spring beans, PostgresConfig
├── presentation/
│   ├── rest/
│   │   ├── request/                # CreateOwnerRequest, UpdatePetRequest, CreateVisitRequest...
│   │   ├── command/                # RestOwnerCommandController, RestPetCommandController...
│   │   ├── query/                  # RestOwnerQueryController — returns application views directly as JSON
│   │   └── RestExceptionHandler.java
│   └── ssr/
│       ├── dto/                    # OwnerFormDto, PetFormDto, VisitFormDto (mutable, form binding)
│       ├── query/                  # SsrOwnerQueryController, SsrPetQueryController, SsrVisitQueryController
│       ├── command/                # SsrOwnerCommandController, SsrPetCommandController, SsrVisitCommandController
│       ├── SsrWelcomeController.java
│       └── SsrExceptionHandler.java
└── shared/
    ├── pagination/                 # PageQuery, PageResult, OwnerSearchCriteria
    └── SqlLoader.java              # classpath .sql resource loader
```

## Running the Application

```bash
./mvnw spring-boot:run
```

The application starts on port **8085**, backed by an embedded PostgreSQL
instance (`io.zonky.test:embedded-postgres`) — no external database needed to
run it locally. Schema and seed data are loaded on startup from
`schema.sql` / `data.sql`.

## API

### REST

| Method | URL | Description |
|---|---|---|
| GET | `/api/owners?lastName=...&page=1` | Paged search of owners by last-name prefix |
| GET | `/api/owners/{id}` | Full owner: data + pets + each pet's visits |
| POST | `/api/owners` | Create an owner (id in the response's `Location` header) |
| PUT | `/api/owners/{id}` | Update an owner |
| POST | `/api/pets` | Create a pet |
| PUT | `/api/pets/{id}` | Update a pet |
| POST | `/api/visits` | Create a visit |

There is no dedicated `GET` for a pet or a visit by id: they aren't standalone
resources, only part of the owner aggregate. Errors are returned as RFC 9457
Problem Details. Responses are the application's query views, serialized
directly — see *REST responses are the query views* above.

### SSR (HTML)

Entry point: `http://localhost:8085/owners` — the owner list with search by
last name (`/owners/search`). The owner detail page and the create/edit forms
for owner, pet, and visit are reached via links from this screen; there are no
direct links to a pet or visit by UUID anywhere in the interface.

## Dependencies

- Java 25, Spring Boot 4.0.7
- PostgreSQL (`postgresql` driver) + `io.zonky.test:embedded-postgres` — the
  sole datastore, embedded, no external DB required to run
- Spring Web MVC — REST and SSR controllers
- Spring `JdbcClient` — the sole persistence adapter, no ORM
- Spring Cache (`spring-boot-starter-cache`) — per-instance optimistic
  duplicate pre-check + read-model caching with explicit eviction
- JTE (`jte-spring-boot-starter-4`) — template engine for SSR
- Jackson `jsr310` datatype — `LocalDate` (de)serialization in REST JSON
- Jakarta Bean Validation — input validation at the controller boundary
- Lombok — `@RequiredArgsConstructor` on services/controllers
- ArchUnit — dependency present; layer/naming rules described above are not
  yet backed by automated tests

## Status

Not yet implemented: automated tests (unit/slice/integration/E2E/ArchUnit),
CI pipeline, containerized test database (Testcontainers planned to replace
embedded PostgreSQL for the run-time datasource).
