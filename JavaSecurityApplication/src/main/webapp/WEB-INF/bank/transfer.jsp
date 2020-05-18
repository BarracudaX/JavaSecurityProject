<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!doctype html>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Home</title>
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
    <div class="row justify-content-center">
        <div class="alert alert-info w-100">Your current amount : ${currentAmount} </div>
        <form class="w-50 mt-2" method="post">
            <security:csrfInput />
            <div class="group-control mt-2">
                <label for="username">To(Username):</label>
                <input type="text" class="form-control" name="username" id="username" placeholder="Username..." required>
            </div>
            <div class="group-control mt-2">
                <label for="amount">Amount:</label>
                <input type="number" class="form-control" name="amount" id="amount" placeholder="Amount" required>
            </div>
            <div class="mt-2">
                <button class="btn btn-primary w-100 p-1">Transfer</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
