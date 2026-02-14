package com.example.laba2;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "ServletDel", value = "/t2")
public class ServletDel extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {


            String sql = " delete from t1 where id = '"+id+"'";


            int rows = stmt.executeUpdate(sql);

            response.sendRedirect(request.getContextPath() + "/t1");
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/t1?error=1");
        }
    }
}
