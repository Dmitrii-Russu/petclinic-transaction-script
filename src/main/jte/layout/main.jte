@param String title
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${title} - PetClinic</title>
    <link rel="shortcut icon" type="image/x-icon" href="/images/favicon.png"/>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Varela+Round&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/css/petclinic.css"/>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark" role="navigation">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">
            <img src="/images/spring-logo-dataflow-mobile.png" alt="Spring" height="30"/>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-navbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="main-navbar">
            <ul class="nav navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link text-white" href="/" title="home page">
                        <i class="fa fa-home"></i> Home
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/owners" title="find owners">
                        <i class="fa fa-search"></i> Find Owners
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/vets" title="veterinarians">
                        <i class="fa fa-list"></i> Veterinarians
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/oups" title="trigger a RuntimeException">
                        <i class="fa fa-exclamation-triangle"></i> Error
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="container xd-container">

        ${content}

        <br/>
        <br/>
        <div class="container">
            <div class="row">
                <div class="col-12 text-center">
                    <img src="/images/spring-logo.svg" alt="VMware Tanzu Logo" class="logo" height="40"/>
                </div>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>