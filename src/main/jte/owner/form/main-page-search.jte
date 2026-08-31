@param String error
@param String message

@template.layout.main(title = "Find Owners", content = @`

    <h2>Find Owners</h2>

    @if(message != null && !message.isEmpty())
        <p class="text-success">${message}</p>
    @endif

    <form action="/owners/search" method="get" class="form-horizontal" id="search-owner-form">
        <div class="form-group">
            <div class="control-group">
                <label class="col-sm-2 control-label">Last name</label>
                <div class="col-sm-10">
                    <input class="form-control" type="text" name="lastName" size="30" maxlength="80"/>
                    @if(error != null && !error.isEmpty())
                        <p class="text-danger">${error}</p>
                    @endif
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-primary">Find Owner</button>
            </div>
        </div>
        <a class="btn btn-primary" href="/owners/new">Add Owner</a>
    </form>

`)