package servlet;

import database.DatabaseHandler;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "PostIPServlet",
        urlPatterns = {"/postip"}
    )
public class PostIP extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DatabaseHandler dbh = new DatabaseHandler();
        String id = req.getParameter("id");
        String ip = req.getParameter("ip");
        String token = req.getParameter("token");
        String result = dbh.insertToDatabase(id, ip, token);
        ServletOutputStream out = resp.getOutputStream();
        out.write(result.getBytes());
        out.flush();
        out.close();
    }

}