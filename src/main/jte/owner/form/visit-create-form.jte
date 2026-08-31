@param dev.dmitriirussu.petclinic.application.query.view.visit.SsrVisitCreateView visitForm
@param dev.dmitriirussu.petclinic.presentation.ssr.dto.VisitFormDto form
@param String error

@template.layout.main(title = "New Visit", content = @`

    <h2>New Visit</h2>

    @if(error != null && !error.isEmpty())
        <div class="alert alert-danger">${error}</div>
    @endif

    <b>Pet</b>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Birth Date</th>
            <th>Type</th>
            <th>Owner</th>
        </tr>
        </thead>
        <tr>
            <td>${visitForm.petName()}</td>
            <td>${visitForm.birthDate().toString()}</td>
            <td>${visitForm.type()}</td>
            <td>${visitForm.ownerFirstName()} ${visitForm.ownerLastName()}</td>
        </tr>
    </table>

    <form action="/visits/new" method="post">
        <input type="hidden" name="petId" value="${visitForm.petId()}">
        <input type="hidden" name="ownerId" value="${visitForm.ownerId()}">

        <div class="form-group has-feedback">

            <div class="form-group">
                <label class="col-sm-2 control-label">Date</label>
                <div class="col-sm-10">
                    <input class="form-control" type="date" name="date"
                           min="${java.time.LocalDate.now().toString()}"
                           value="${form != null && form.getDate() != null ? form.getDate().toString() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Description</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="description"
                           value="${form != null && form.getDescription() != null ? form.getDescription() : ""}"/>
                </div>
            </div>

        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-primary" type="submit">Add Visit</button>
            </div>
        </div>

    </form>

    <br/>

    <b>Previous Visits</b>
    <table class="table table-striped">
        <tr>
            <th>Date</th>
            <th>Description</th>
        </tr>
        @for(var visit : visitForm.visits())
            <tr>
                <td>${visit.date().toString()}</td>
                <td>${visit.description()}</td>
            </tr>
        @endfor
    </table>

`)