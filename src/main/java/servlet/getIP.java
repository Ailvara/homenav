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
        name = "GetIPServlet",
        urlPatterns = {"/getip"}
    )
public class getIP extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        DatabaseHandler dbh = new DatabaseHandler();
        String id = req.getParameter("id");
        String token = req.getParameter("token");
        String ip = dbh.getFromDatabase(id,token);
        if(ip!=null) out.write(ip.getBytes());
        else out.write("Error".getBytes());
        out.flush();
        out.close();
    }

}