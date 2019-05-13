package pl.polsl.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.model.PrimeNumbersException;
import pl.polsl.lab.model.PrimeNumbersModel;

/**
 *
 * @author Przemysław Drążkiewicz
 * @version 2.0
 */
public class NthPrimeNum extends HttpServlet {

    /**
     * field representing value of nth prime number
     */
    private int nthValue;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        PrimeNumbersModel model = (PrimeNumbersModel) session.getAttribute("model");
        boolean args = false;
        Connection conn = (Connection) session.getAttribute("conn");

        //check if the model has been initialized earlier
        if (model == null) {
            model = new PrimeNumbersModel();
            session.setAttribute("model", model);
        }

        //check if the coonection has been initialized earlier
        try {
            if (conn == null) {
                Class.forName(this.getInitParameter("driver"));
                conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("user"), this.getInitParameter("password"));
                session.setAttribute("conn", conn);
            }
        } catch (SQLException e) {
            out.println("SQL exception: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            out.println("ClassNotFound exception: " + ex.getMessage());
        }

        //getting value from form
        try {
            nthValue = Integer.parseInt(request.getParameter("nthNumber"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserted value must be integer");
            args = true;
        } catch (NullPointerException e) {
            out.println("NullPointerException");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NullPointerException");
            args = true;
        }

        //calculation of nth prime number
        if (!args) {
            model.setNthPrimeNumber(nthValue);
            try {
                model.nthPrimeNumber();
            } catch (PrimeNumbersException e) {
                out.println("Value must be positive");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Value must be positive");
                args = true;
            }
            if (!args) {
                out.println("<h3>" + model.getNthPrimeNumber() + "-th prime number is " + model.getNthPrimeNumberResult() + "</h3>");
                String stat = model.getNthPrimeNumber() + "-th prime number is " + model.getNthPrimeNumberResult();
                //creating cookies
                Cookie cookie = new Cookie("lastResult", stat);
                response.addCookie(cookie);

                //transfer data to the database
                try {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("INSERT INTO NthPrimeNumberTable VALUES("
                            + model.getNthPrimeNumber() + ", "
                            + model.getNthPrimeNumberResult()
                            + ")");
                } catch (SQLException e) {
                    out.println("SQL exception: " + e.getMessage());
                }
                session.setAttribute("connection", conn);
            }
        }

    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
