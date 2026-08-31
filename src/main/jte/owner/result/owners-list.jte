@param java.util.List<dev.dmitriirussu.petclinic.application.query.view.owner.OwnerListView> owners
@param int currentPage
@param int totalPages
@param String lastName

@template.layout.main(title = "Owners", content = @`

    <h2>Owners</h2>

    @if(owners.isEmpty())
        <p>No owners found.</p>
    @else
        <table id="owners" class="table table-striped">
            <thead>
            <tr>
                <th style="width: 150px;">Name</th>
                <th style="width: 200px;">Address</th>
                <th>City</th>
                <th style="width: 120px;">Telephone</th>
                <th>Pets</th>
            </tr>
            </thead>
            <tbody>
            @for(var o : owners)
                <tr>
                    <td><a href="/owners/${o.id()}">${o.firstName()} ${o.lastName()}</a></td>
                    <td>${o.street()}</td>
                    <td>${o.city()}</td>
                    <td>${o.telephone()}</td>
                    <td>${String.join(", ", o.pets())}</td>
                </tr>
            @endfor
            </tbody>
        </table>

        @if(totalPages > 1)
            <div>
                <span>Pages:</span>
                <span>[</span>
                @for(int i = 1; i <= totalPages; i++)
                    @if(currentPage == i)
                        <span>${i}</span>
                    @else
                        <a href="/owners/search?page=${i}&lastName=${lastName}">${i}</a>
                    @endif
                @endfor
                <span>]&nbsp;</span>

                @if(currentPage > 1)
                    <a href="/owners/search?page=1&lastName=${lastName}" title="first" class="fa fa-fast-backward"></a>
                    <a href="/owners/search?page=${currentPage - 1}&lastName=${lastName}" title="previous" class="fa fa-step-backward"></a>
                @else
                    <span title="first" class="fa fa-fast-backward"></span>
                    <span title="previous" class="fa fa-step-backward"></span>
                @endif

                @if(currentPage < totalPages)
                    <a href="/owners/search?page=${currentPage + 1}&lastName=${lastName}" title="next" class="fa fa-step-forward"></a>
                    <a href="/owners/search?page=${totalPages}&lastName=${lastName}" title="last" class="fa fa-fast-forward"></a>
                @else
                    <span title="next" class="fa fa-step-forward"></span>
                    <span title="last" class="fa fa-fast-forward"></span>
                @endif
            </div>
        @endif
    @endif

`)