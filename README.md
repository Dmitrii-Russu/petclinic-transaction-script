# PetClinic — Transaction Script + CQRS with Spring JDBC

**A deliberately simple PetClinic clone** designed to demonstrate architectural discipline: strict layer boundaries enforced *by the compiler*, not just convention.

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
invariants and behavior worth encapsulating in an entity — specifically, when
an operation needs the aggregate's current state to decide whether a new
state is valid. None of the three invariants here require that: each is a
structural existence/uniqueness check, fully determined by input, not by
state already held in memory. Complicating the model for its own sake
contradicts the "simple code" goal, so a Transaction Script was chosen
instead: `Owner`, `Pet`, `Visit` are plain `record`s, with no methods and no
state. See ["The service is getting bloated" — move the logic into the entity? Here's the real criterion](https://dmitrii-russu.github.io/posts/aggregate-loading-criterion/)
for the full reasoning behind this criterion.

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
| Application / command | `application.command.usecase` / `.model` / `.repository` / `.service` | Write use cases (`OwnerCreateUseCase`, `PetUpdateUseCase`, `VisitCreateUseCase`, ...), commands (`OwnerCreateCommand`, `PetUpdateCommand`, ...), write ports, and package-private service impls. No SSR/REST split — writes are shared by both UIs. |
| Application / query | `application.query.usecase(.ssr)` / `.repository(.ssr)` / `.view.{owner,pet,visit}(.ssr)` / `.catalog.ssr` / `.service(.ssr)` | Read use cases, read ports, and view models tailored to a specific screen. The base packages hold what both UIs share (`OwnerFindUseCase`, `OwnerSearchUseCase`, `OwnerDetailsView`, `OwnerListView`); the `.ssr` sub-packages hold what only the SSR forms need (`OwnerEditFormUseCase`, `PetEditFormUseCase`, `VisitCreateFormUseCase`, `OwnerNameUseCase`, and their matching views/repositories/catalog). |
| Infrastructure | `infrastructure.command(.support)`, `infrastructure.query(.ssr)(.support)`, `infrastructure.query.catalog.ssr`, `infrastructure.bootstrap` | JDBC adapters via `JdbcClient` (no ORM), mirroring the same base/`.ssr` split as the query side, plus Spring wiring (`@Configuration` classes exposing only interfaces as beans). |
| Presentation | `presentation.rest.*`, `presentation.ssr.*` | REST controllers (JSON) and SSR controllers (JTE templates), both on top of the same use cases. |
| Shared | `shared.pagination`, `shared.ValidationMessages`, `shared.SqlLoader` | `PageQuery`, `PageResult`, `OwnerSearchCriteria` — cross-cutting pagination/search types — shared Bean Validation regex/message constants, and a small loader that reads `.sql` resources into constants. |

`PageQuery` and `PageResult` are plain records owned by the application
layer, not `org.springframework.data.domain.Pageable` — use-case and
controller code describe what the application needs (a page number, a page
size, a total) rather than the API of the persistence framework underneath.
See [Why I stopped exposing Spring Pageable in application layer contracts](https://dmitrii-russu.github.io/posts/spring-pageable/)
for the reasoning behind this choice.

**What's already guaranteed vs. what's planned.** Layer isolation is enforced
today, physically, by package-private `*ServiceImpl` / `*RepositoryImpl`
classes: nothing outside `application.*.service` / `infrastructure.*` can even
compile a reference to an implementation — only the interface, exposed through
the package's `*Factory`. This holds regardless of tooling. ArchUnit is
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
`OwnerFindUseCase` and `OwnerCreateUseCase` know nothing about each other.

**Layer isolation via package visibility, not discipline alone.** Following a
zero-trust principle toward developer discipline, `*ServiceImpl` and
`*RepositoryImpl` are declared without `public`. Only the interface is visible
outside the package — exposed by one public factory class per package, with a
private constructor: `CommandRepositoryFactory`, `CommandServiceFactory`,
`QueryRepositoryFactory`, `QueryServiceFactory`, and — because the query side
is itself split into a base part and an SSR-only part —
`SsrQueryRepositoryFactory`, `SsrQueryServiceFactory`, and
`PetTypeCatalogFactory` for the catalog. One factory covers every entity in
its package (all command repositories together, all base query repositories
together, all SSR query repositories together, and so on) rather than one
factory per entity — this keeps the physical compile-time barrier without one
extra class per aggregate. `@Bean` methods in `infrastructure.bootstrap`
(`CommandRepositoryConfig`, `CommandServiceConfig`, `QueryRepositoryConfig`,
`QueryServiceConfig`) return only interfaces, never implementations.

**Use cases split into packages by action, not by a CRUD facade.** SSR exposes
more operations than REST (separate create/edit forms, name-lookup helpers for
forms), so instead of one "fat" service per entity, use cases are one
interface per action (`OwnerCreateUseCase`, `OwnerUpdateUseCase`,
`OwnerSearchUseCase`, `OwnerEditFormUseCase`, ...). No implementation drags in
methods it doesn't need. The SSR-only use cases (`OwnerEditFormUseCase`,
`OwnerNameUseCase`, `PetEditFormUseCase`, `VisitCreateFormUseCase`) live in
`application.query.usecase.ssr`, physically separate from the ones REST also
consumes — the package they're in is what marks them as SSR-specific, not a
naming prefix on the class.

### Data & consistency

**Validation and three invariants.** Input validation (format, required
fields) is delegated to controllers via Jakarta Bean Validation — deliberately
not duplicated in services. The domain has exactly three meaningful
constraints, each an existence/uniqueness check rather than real business
logic:
- an owner's phone number must be unique (`owners.telephone UNIQUE`);
- an owner cannot have two identical pets — equality by fields, not id
  (`uq_pet_identity UNIQUE(owner_id, name, birth_date, type)`);
- a pet cannot have two visits on the same date
  (`uq_visit_pet_date UNIQUE(pet_id, visit_date)`).

All three are delegated to unique constraints in the database rather than
in-code existence checks: an in-memory or extra-query check degrades at scale
and doesn't protect against TOCTOU. The SQL exception is caught at the
`infrastructure.command` boundary and translated into a standard Java
exception — only that exception is visible further up the call stack. All
three are structural invariants under concurrent-write risk, which is
precisely the case where database-level protection is not optional — see
[Criteria for Placing Validation in an Application](https://dmitrii-russu.github.io/posts/validation-placement/)
for the general framework behind this decision.

**Optimistic duplicate pre-check via cache.** Before writing to the database,
`OwnerCreateRepositoryImpl`, `OwnerUpdateRepositoryImpl`,
`PetCreateRepositoryImpl`, `PetUpdateRepositoryImpl`, and
`VisitCreateRepositoryImpl` each perform a fast lookup in a Spring `Cache`
keyed by the same business fields the unique constraint covers, to skip an
avoidable round trip for a conflict that's already predictable. The unique
constraint in the database remains the actual source of truth
(`DataIntegrityViolationException` → `ConstraintViolationTranslator`); the
cache only reduces load on the hot path, it doesn't replace the guarantee.
This is a per-instance optimization — with more than one running instance the
cache won't be consistent across them, so the database constraint is what
actually prevents a duplicate, not the cache.

On the update paths (`OwnerUpdateRepositoryImpl`, `PetUpdateRepositoryImpl`),
the cache is keyed both by id and by the business key, so the previous
business key is available from the cache without an extra DB read, letting
the stale entry under the old key be evicted after a successful write.
`Visit` has no update use case, so `VisitCreateRepositoryImpl` only needs the
single business-key entry.

For the full reasoning, the failure mode of a stale positive cache entry, and
when this pattern is a poor fit, see: [Cache as a Pre-Filter Before a
Database Constraint](https://dmitrii-russu.github.io/posts/cache-pre-filter/).

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

**No ORM — JdbcClient.** ORMs handle full entity graphs well via
identity-graph loading (e.g. `@EntityGraph`), but that only controls *how*
the graph is loaded, not *what shape* the result takes. `OwnerListView` needs
pet names only, not full `Pet`/`Visit` entities — no `@EntityGraph`
configuration produces that shape directly, because loading and projection
are different responsibilities. Rather than fight that mismatch on top of an
ORM, the persistence layer is built on JdbcClient (Spring 6.1+) instead of
JdbcTemplate or JPA/Hibernate, with the application assembling exactly the
shape each read model needs. For the full reasoning — and how this compares
to jOOQ's MULTISET and Blaze Persistence's Entity Views as alternative ways
to solve the same problem — see [JPA handles full graphs well. What about partial ones?](https://dmitrii-russu.github.io/posts/jpa-partial-graphs/).

SQL lives in `.sql` resource files under `src/main/resources/sql/{command,query}`,
loaded once into `static final String` constants via `SqlLoader`.

**No collection in the parent model.** `Owner` does not hold a `List<Pet>`,
nor does `Pet` hold a `List<Visit>` — removing such a collection and
replacing it with a separate query breaks no invariant here, since nothing
requires atomic consistency across the group at the model level. The nested
shape needed for `OwnerDetailsView` is assembled only on the read side, via
SQL, not carried as model state. See
[One-to-Many in Java: When a Collection in the Parent Is Justified — and When It Is Not](https://dmitrii-russu.github.io/posts/fk-mapping/).

**Reading an aggregate — single-query hydration.** `OwnerDetailsView` (an
owner plus their pets plus each pet's visits) is assembled with one SQL query
using `LEFT JOIN LATERAL` + `jsonb_agg`/`jsonb_build_object`, and the resulting
JSON column is deserialized into the view's nested records by a shared
`ViewExtractor` (`infrastructure.query.support`) — no N+1 queries, no
hand-rolled in-memory graph assembly per aggregate. This intentionally couples
the read side to PostgreSQL-specific JSON functions; the trade-off accepted
here is fewer classes and one round trip, in exchange for not being portable
to another RDBMS — acceptable since PostgreSQL is the only target.
A portable alternative exists — assembling the graph manually in application
code via an accumulator pattern (`computeIfAbsent`/`LinkedHashMap` at each
graph level) works identically on any RDBMS, at the cost of intermediate
accumulator classes and manual deduplication. It wasn't chosen here: with
PostgreSQL as the only target, paying for portability that will never be
exercised isn't worth trading away a single round trip for a multi-query or
extra-class alternative. See the JDBC section of
[JPA handles full graphs well. What about partial ones?](https://dmitrii-russu.github.io/posts/jpa-partial-graphs/)
for that pattern in full.

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
REST and SSR each have their own `OwnerCommandController`/`PetCommandController`/
`VisitCommandController`/`OwnerQueryController` — same simple class name on
both sides, disambiguated by package (`presentation.rest.*` vs.
`presentation.ssr.*`) and by the Spring bean name given in the `@Controller`/
`@RestController` annotation (e.g. `"restOwnerCommandController"` vs.
`"ssrOwnerCommandController"`), rather than by a `Rest`/`Ssr` class-name
prefix.

### API design

**REST responses are the query views, by design — not a temporary shortcut.**
`presentation.rest.query.OwnerQueryController` serializes `OwnerDetailsView` /
`PageResult<OwnerListView>` directly as JSON, with no separate
`presentation.rest.response` mapping layer. This is deliberate, not an
omission the DTO layer will "fix" later:

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
  the wire — is already held: only `application.query.view.*` records ever
  reach a controller, REST or SSR.

If REST and SSR ever need genuinely different shapes for the same read (e.g.
a mobile client wanting a slimmer payload than the HTML page), that's the
signal to introduce a `presentation.rest.response` mapping layer — not before.

**One request record shared by create and update.** `OwnerRequest`,
`PetRequest`, and `VisitRequest` (`presentation.rest.request`) are each used
for both the create and the update endpoint of their entity — there's no
separate `CreateOwnerRequest`/`UpdateOwnerRequest` pair, since the two
operations validate and carry the same fields; only the command built from the
request differs (`OwnerCreateCommand` vs. `OwnerUpdateCommand`).

**Flat routes instead of nested paths.** Controllers avoid nesting like
`/owners/{ownerId}/pets/{petId}`: a pet is addressed as `/pets/{petId}/edit`,
a new pet as `/pets/new?ownerId=...`. The REST API has no standalone
`GET /api/pets/{id}` or `GET /api/visits/{id}` — pets and visits are only
readable as part of the owner aggregate.

## Project Structure

```
src/main/java/.../petclinic/
├── PetclinicApplication.java       # @SpringBootApplication + @EnableCaching, main entry point
├── model/                          # Owner, Pet, Visit — records, no outward dependencies
├── application/
│   ├── command/
│   │   ├── usecase/                # OwnerCreateUseCase, OwnerUpdateUseCase, PetCreateUseCase, PetUpdateUseCase, VisitCreateUseCase
│   │   ├── model/                  # OwnerCreateCommand, OwnerUpdateCommand, PetCreateCommand, PetUpdateCommand, VisitCreateCommand
│   │   ├── repository/             # write ports: OwnerCreateRepository, OwnerUpdateRepository, PetCreateRepository, PetUpdateRepository, VisitCreateRepository
│   │   └── service/                # package-private impls, exposed via CommandServiceFactory
│   └── query/
│       ├── usecase/                # OwnerFindUseCase, OwnerSearchUseCase (shared by REST + SSR)
│       │   └── ssr/                # OwnerEditFormUseCase, OwnerNameUseCase, PetEditFormUseCase, VisitCreateFormUseCase (SSR forms only)
│       ├── view/
│       │   ├── owner/              # OwnerListView, OwnerDetailsView
│       │   │   └── ssr/            # OwnerNameView, OwnerEditView
│       │   ├── pet/                # PetDetailsView
│       │   │   └── ssr/            # PetEditView
│       │   └── visit/              # VisitView
│       │       └── ssr/            # VisitCreateView
│       ├── repository/             # read ports: OwnerFindRepository, OwnerSearchRepository
│       │   └── ssr/                # OwnerEditFormRepository, OwnerNameRepository, PetEditFormRepository, VisitCreateFormRepository
│       ├── catalog/
│       │   └── ssr/                # PetTypeCatalog
│       └── service/                # package-private impls, exposed via QueryServiceFactory
│           └── ssr/                # package-private impls, exposed via SsrQueryServiceFactory
├── infrastructure/
│   ├── command/                     # JdbcClient impls: OwnerCreateRepositoryImpl, OwnerUpdateRepositoryImpl, PetCreateRepositoryImpl, PetUpdateRepositoryImpl, VisitCreateRepositoryImpl — exposed via CommandRepositoryFactory
│   │   └── support/                 # ConstraintViolationTranslator<T> (+ nested ExceptionFactory<T>) + per-entity constraint maps
│   ├── query/                      # JdbcClient impls: OwnerFindRepositoryImpl, OwnerSearchRepositoryImpl — exposed via QueryRepositoryFactory
│   │   ├── ssr/                    # OwnerEditFormRepositoryImpl, OwnerNameRepositoryImpl, PetEditFormRepositoryImpl, VisitCreateFormRepositoryImpl — exposed via SsrQueryRepositoryFactory
│   │   ├── support/                # ViewExtractor — shared ResultSet → view mapping
│   │   └── catalog/
│   │       └── ssr/                # PetTypeCatalogImpl — exposed via PetTypeCatalogFactory
│   └── bootstrap/                  # @Configuration: QueryServiceConfig, QueryRepositoryConfig, CommandServiceConfig, CommandRepositoryConfig
├── presentation/
│   ├── rest/
│   │   ├── request/                # OwnerRequest, PetRequest, VisitRequest (shared by create + update)
│   │   ├── command/                # OwnerCommandController, PetCommandController, VisitCommandController
│   │   ├── query/                  # OwnerQueryController — returns application views directly as JSON
│   │   └── RestExceptionHandler.java
│   └── ssr/
│       ├── dto/                    # OwnerFormDto, PetFormDto, VisitFormDto (mutable, form binding)
│       ├── query/                  # WelcomeController, OwnerQueryController, PetQueryController, VisitQueryController
│       ├── command/                # OwnerCommandController, PetCommandController, VisitCommandController
│       └── SsrExceptionHandler.java
└── shared/
    ├── pagination/                 # PageQuery, PageResult, OwnerSearchCriteria
    ├── ValidationMessages.java     # shared regex/message constants used by REST requests + SSR DTOs
    └── SqlLoader.java              # classpath .sql resource loader
```

> REST and SSR controllers with the same simple name (e.g. `OwnerCommandController`)
> live in different packages (`presentation.rest.command` vs.
> `presentation.ssr.command`) and are registered under different Spring bean
> names — see *Two UIs, one core* above.

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
