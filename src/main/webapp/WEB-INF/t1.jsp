<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Таблица t1</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/t1.css">
</head>
<body>
    <h1>Студенты: </h1>

    <table border="1">
        <tr>
            <% for (String col : (List<String>) request.getAttribute("columnNames")) { %>
                <th><%= col %></th>
            <% } %>
            <th></th>
        </tr>
        <% for (Object[] row : (List<Object[]>) request.getAttribute("rows")) { %>
            <tr>
                <% for (Object cell : row) { %>
                    <td><%= cell != null ? cell : "" %></td>
                <% } %>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/t2">
                        <button type="submit" value="<%= row[row.length - 1] %>" name="id">x</button>
                    </form>
                </td>

            </tr>

        <% } %>
    </table>

    <h2>INSERT INTO t1</h2>
    <form method="post" action="${pageContext.request.contextPath}/t1">
        <p>Возраст: <input type="text" name="age" required></p>
        <p>Фамилия: <input type="text" name="fam" required></p>
        <p>Имя: <input type="text" name="nam" required></p>
        <p>Отчетсво: <input type="text" name="pat" required></p>
        <p>Номер группы: <input type="text" name="groop" required></p>
        <input type="submit" value="Добавить">
    </form>
    <form method="post" action="${pageContext.request.contextPath}/t2">
        <p> чето удалить :<input type="text" name="fam" required></p>
        <input type="submit" value="Удалить">
    </form>
</body>
</html>
