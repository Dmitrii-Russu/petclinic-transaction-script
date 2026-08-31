@param String ownerFirstName
@param String ownerLastName
@param dev.dmitriirussu.petclinic.presentation.ssr.dto.PetFormDto petForm
@param String petId
@param java.util.List<String> petTypes
@param String error

@template.layout.main(title = "${petId != null ? \"Edit Pet\" : \"New Pet\"}", content = @`

    <h2>${petId != null ? "Edit Pet" : "New Pet"}</h2>

    @if(error != null && !error.isEmpty())
        <div class="alert alert-danger">${error}</div>
    @endif

    <form action="${petId != null ? "/pets/" + petId + "/edit" : "/pets/new"}" class="form-horizontal" method="post">

        <input type="hidden" name="ownerId" value="${petForm.getOwnerId()}">
        <input type="hidden" name="ownerFirstName" value="${petForm.getOwnerFirstName()}">
        <input type="hidden" name="ownerLastName" value="${petForm.getOwnerLastName()}">

        <div class="form-group has-feedback">

            <div class="form-group">
                <label class="col-sm-2 control-label">Owner</label>
                <div class="col-sm-10">
                    <span>${ownerFirstName} ${ownerLastName}</span>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Name</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="name"
                           value="${petForm.getName() != null ? petForm.getName() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Birth Date</label>
                <div class="col-sm-10">
                    <input class="form-control" type="date" name="birthDate"
                           max="${java.time.LocalDate.now().toString()}"
                           value="${petForm.getBirthDate() != null ? petForm.getBirthDate().toString() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Type</label>
                <div class="col-sm-10">
                    <select class="form-control" name="type">
                        @for(var t : petTypes)
                            @if(petForm.getType() != null && petForm.getType().equals(t))
                                <option value="${t}" selected>${t}</option>
                            @else
                                <option value="${t}">${t}</option>
                            @endif
                        @endfor
                    </select>
                </div>
            </div>

        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-primary" type="submit">
                    ${petId != null ? "Update Pet" : "Add Pet"}
                </button>
            </div>
        </div>

    </form>

    <a href="/owners/${petForm.getOwnerId()}" class="btn btn-default">Back to Owner</a>

`)