package com.example.laba2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Примеры: SELECT * FROM t1 и INSERT INTO t1
 *
 * КАК ДАННЫЕ ПОПАДАЮТ В ПЕРЕМЕННЫЕ:
 *
 * 1. ЯВНЫЕ ПЕРЕМЕННЫЕ — мы сами создаём и заполняем:
 *    String nomer = "101";
 *    String fam = "Петров";
 *    Данные «сидят» прямо в коде.
 *
 * 2. ИЗ ФОРМЫ — пользователь вводит, браузер отправляет POST:
 *    <input name="nomer"> → request.getParameter("nomer") → String nomer
 *    <input name="fam">   → request.getParameter("fam")   → String fam
 *
 * 3. ДАЛЕЕ — в обоих случаях переменные подставляются в SQL:
 *    sql = "INSERT INTO t1 (nomer, fam) VALUES ('" + nomer + "', '" + fam + "')"
 *    Если nomer="101", fam="Петров" → sql = "INSERT INTO t1 (nomer, fam) VALUES ('101', 'Петров')"
 *    stmt.executeUpdate(sql) отправляет этот запрос в БД.
 */
@WebServlet(name = "tableT1Servlet", value = "/t1")
public class TableT1Servlet extends HttpServlet {

    /**
     * Пример INSERT с явными переменными — данные заданы прямо в коде.
     * Показывает цепочку: переменные → SQL-строка → БД.
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM t1")) {

            int colCount = rs.getMetaData().getColumnCount();
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= colCount; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }

            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }

            request.setAttribute("columnNames", columnNames);
            request.setAttribute("rows", rows);
        } catch (SQLException e) {
            request.setAttribute("errorMsg", e.getMessage());
            request.setAttribute("columnNames", new ArrayList<String>());
            request.setAttribute("rows", new ArrayList<Object[]>());
        }

        request.getRequestDispatcher("/WEB-INF/t1.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        // === 1. СОЗДАНИЕ ПЕРЕМЕННЫХ ===
        // Берём данные из формы (поля с name="nomer" и name="fam").
        // request.getParameter() — то место, где переменные «рождаются» из HTTP-запроса.
        String age = request.getParameter("age");  // например: "123"
        String fam = request.getParameter("fam");      // например: "Иванов"
        String nam = request.getParameter("nam");
        String pat = request.getParameter("pat");
        String groop = request.getParameter("groop");
        // Можно и явно задать переменные (для теста без формы):
        // String nomer = "101";
        // String fam = "Петров";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // === 2. ВСТАВКА ДАННЫХ ИЗ ПЕРЕМЕННЫХ В SQL ===
            // Собираем строку SQL, подставляя переменные в нужные места.
            // В VALUES должны быть строки в кавычках: 'значение'
            //
            // Было бы:    INSERT INTO t1 (nomer, fam) VALUES ('123', 'Иванов')
            // Собираем:   "INSERT INTO t1 (nomer, fam) VALUES ('" + nomer + "', '" + fam + "')"
            //
            // .replace("'", "''") — экранируем одинарные кавычки в данных
            String sql = "INSERT INTO t1 (age, fam, nam, pat, groop) VALUES ('" + age + "', '" + fam + "', '"+ nam + "', '"+ pat + "', '" + groop + "')";

            // === 3. ВЫПОЛНЕНИЕ ЗАПРОСА ===
            // executeUpdate() выполняет INSERT и возвращает кол-во добавленных строк
            int rows = stmt.executeUpdate(sql);

            response.sendRedirect(request.getContextPath() + "/t1");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/t1?error=1");
        }
    }
}
