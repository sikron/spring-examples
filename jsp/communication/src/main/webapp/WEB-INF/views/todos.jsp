<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>Todos</title>
</head>
<body>

<h1>Welcome to the Todos</h1>

<h3>Current Todos</h3>

<table border="1" style="width:100%">
    <col width="50%">
    <col width="50%">
    <tr>
        <th>Name</th>
        <th>Done?</th>
    </tr>
    <c:forEach items="${todos}" var="todo">
        <tr id="todo_<c:out value="${todo.id}"/>">
            <td>
                <c:out value="${todo.name}"/>
            </td>
            <td>
                <c:out value="${todo.done}"/>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
