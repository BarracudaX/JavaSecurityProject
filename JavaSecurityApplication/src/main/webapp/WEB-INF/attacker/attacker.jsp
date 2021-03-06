<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Title</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
<script
        src="https://code.jquery.com/jquery-3.5.0.js"
        integrity="sha256-r/AaFHrszJtwpe+tHyNi/XCfMxYpbsRg2Uqn0x3s2zc="
        crossorigin="anonymous"></script>
<script>
    window.onload=function(){
        $.ajax("<c:url value="/bank" />",{
            type : 'POST',
            data :{
                username: 'ATTACKER',
                amount : 100
            }
        });
    }
</script>
<div class="container-fluid">

    <div class="row">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark w-100">
            <ul class="navbar-nav">
                <security:authorize access="hasRole('USER')" >
                    <li class="nav-item"><a href="<c:url value="/bank"/>" class="nav-link">Bank</a></li>
                    <li class="nav-item"><a href="<c:url value="/user/xss"/>" class="nav-link">XSS</a></li>
                    <li class="nav-item"><a href="<c:url value="/attacker/csrf"/>" class="nav-link">CSRF</a></li>
                    <li class="nav-item"><a href="<c:url value="/logout"/>" class="nav-link">Logout</a></li>
                </security:authorize>
                <security:authorize access="!isAuthenticated()">
                    <li class="nav-item"><a href="<c:url value="/login"/>" class="nav-link">Login</a></li>
                </security:authorize>
            </ul>
        </nav>
    </div>

</div>
</body>
</html>