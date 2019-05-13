package pl.polsl.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.model.PrimeNumbersModel;

/**
 *
 * @author Przemysław Drążkiewicz
 * @version 2.0
 */
public class CheckNumber extends HttpServlet {

    /**
     * field representing number to check if it is prime number
     */
    private int number;

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

        //check if the model has been previously created
        if (model == null) {
            model = new PrimeNumbersModel();
            session.setAttribute("model", model);
        }
        //check if the connection has been initialized earlier
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

        //getting parameter from the form
        try {
            number = Integer.parseInt(request.getParameter("number"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserted value must be integer");
            args = true;
        } catch (NullPointerException e) {
            out.println("NullPointerException");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NullPointerException");
            args = true;
        }

        //check if the number is prime number
        if (!args) {
            model.setNumber(number);
            if (model.isPrime(number)) {
                out.println("<h3>" + "Value " + model.getNumber() + " is prime number" + "</h3");
            } else {
                out.println("<h3>" + "Value " + model.getNumber() + " isn't prime number" + "</h3>");
            }
        }
        //transfer data to the database
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO IsPrimeTable VALUES ("
                    + model.getNumber() + ","
                    + model.isPrime(number)
                    + ")");
        } catch (SQLException e) {
            out.println("SQL exception: " + e.getMessage());
        }
        session.setAttribute("connection", conn);

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
