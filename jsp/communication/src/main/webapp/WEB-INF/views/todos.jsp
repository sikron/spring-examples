<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Todos</title>
</head>
<body>

<h1>Welcome to the Todos</h1>

<h3>Current Todos</h3>

<form>
    <label>
        <input type="checkbox" name="only_open_checkbox" checked="<c:out value="${checked}"/>">
        Nur offene anzeigen
    </label>
</form>

<table border="1" style="width:100%">
    <col width="25%">
    <col width="25%">
    <col width="25%">
    <col width="25%">
    <tr>
        <th>Issue</th>
        <th>Done?</th>
        <th>Creation Date</th>
        <th>Resolve</th>
    </tr>
    <c:forEach items="${todos}" var="todo">
        <tr>
            <td>
                <c:out value="${todo.issue}"/>
            </td>
            <td>
                <c:out value="${todo.done}"/>
            </td>
            <td>
                <c:out value="${todo.creationDate}"/>
            </td>
            <td>
                <form method="POST">
                    <input type="hidden" name="id" value="<c:out value="${todo.id}"/>"/>
                    <input type="submit" value="OK" name="resolve"/>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<form method="POST">
    New Todo: <input type="text" name="issue"/>
    <input type="submit" value="Create"/>
</form>

</body>
</html>
