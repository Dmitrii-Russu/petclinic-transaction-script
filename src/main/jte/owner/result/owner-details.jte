@param dev.dmitriirussu.petclinic.application.query.view.owner.OwnerDetailsView owner
@param String message

@template.layout.main(title = "Owner Information", content = @`

    <h2>Owner Information</h2>

    @if(message != null && !message.isEmpty())
        <div class="alert alert-success" id="success-message">
            <span>${message}</span>
        </div>
    @endif

    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b>${owner.firstName()} ${owner.lastName()}</b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td>${owner.street()}</td>
        </tr>
        <tr>
            <th>City</th>
            <td>${owner.city()}</td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td>${owner.telephone()}</td>
        </tr>
    </table>

    <a href="/owners/${owner.id()}/edit" class="btn btn-primary">Edit Owner</a>
    <a href="/pets/new?ownerId=${owner.id()}" class="btn btn-primary">Add New Pet</a>

    <br/><br/><br/>

    <h2>Pets and Visits</h2>

    <table class="table table-striped">
        @for(var pet : owner.pets())
            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd>${pet.name()}</dd>
                        <dt>Birth Date</dt>
                        <dd>${pet.birthDate().toString()}</dd>
                        <dt>Type</dt>
                        <dd>${pet.type()}</dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        @for(var visit : pet.visits())
                            <tr>
                                <td>${visit.visitDate().toString()}</td>
                                <td>${visit.description()}</td>
                            </tr>
                        @endfor
                        <tr>
                            <td><a href="/pets/${pet.id()}/edit">Edit Pet</a></td>
                            <td><a href="/visits/new?petId=${pet.id()}">Add Visit</a></td>
                        </tr>
                    </table>
                </td>
            </tr>
        @endfor
    </table>

    <script>
        function hideMessages() {
            setTimeout(function () {
                var msg = document.getElementById("success-message");
                if (msg) msg.style.display = "none";
            }, 3000);
        }
        hideMessages();
    </script>

`)