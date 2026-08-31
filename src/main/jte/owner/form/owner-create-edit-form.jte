@param dev.dmitriirussu.petclinic.presentation.ssr.dto.OwnerFormDto ownerForm
@param String ownerId
@param String error

@template.layout.main(title = "${ownerId != null ? \"Edit Owner\" : \"New Owner\"}", content = @`

    <h2>${ownerId != null ? "Edit Owner" : "New Owner"}</h2>

    @if(error != null && !error.isEmpty())
        <p class="text-danger">${error}</p>
    @endif

    <form action="${ownerId != null ? "/owners/" + ownerId + "/edit" : "/owners/new"}" class="form-horizontal" id="add-owner-form" method="post">
        <div class="form-group has-feedback">

            <div class="form-group">
                <label class="col-sm-2 control-label">First Name</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="firstName"
                           value="${ownerForm.getFirstName() != null ? ownerForm.getFirstName() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Last Name</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="lastName"
                           value="${ownerForm.getLastName() != null ? ownerForm.getLastName() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Address</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="street"
                           value="${ownerForm.getStreet() != null ? ownerForm.getStreet() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">City</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="city"
                           value="${ownerForm.getCity() != null ? ownerForm.getCity() : ""}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">Telephone</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="telephone"
                           value="${ownerForm.getTelephone() != null ? ownerForm.getTelephone() : ""}"/>
                </div>
            </div>

        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-primary" type="submit">
                    ${ownerId != null ? "Update Owner" : "Add Owner"}
                </button>
            </div>
        </div>
    </form>

    <hr/>
    @if(ownerId != null)
        <a href="/owners/${ownerId}">Back to owner</a>
        &nbsp;|&nbsp;
    @endif
`)