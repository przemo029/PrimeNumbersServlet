package pl.polsl.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import javax.servlet.ServletException;
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
public class PrimeNumInRange extends HttpServlet {

    /**
     * Hashtable representing history of prime numbers in range
     */
    private Hashtable<String, String> stats;

    /**
     * begin of range
     */
    private int begin;

    /**
     * end of range
     */
    private int end;

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
        stats = new Hashtable<String, String>();
        PrimeNumbersModel model = (PrimeNumbersModel) session.getAttribute("model");
        boolean args = false;
        Connection conn = (Connection) session.getAttribute("conn");

        //check if the model has been initialized earlier
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

        //getting values from form
        try {
            begin = Integer.parseInt(request.getParameter("begin"));
            end = Integer.parseInt(request.getParameter("end"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserted value must be integer");
            args = true;
        } catch (NullPointerException e) {
            out.println("NullPointerException");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "NullPointerException");
            args = true;
        }

        //calculating the range of prime number
        if (!args) {
            model.setStartRange(begin);
            model.setEndRange(end);
            try {
                model.primeNumbersInRange();
            } catch (PrimeNumbersException e) {
                out.println("Range is invalid");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Range is invalid");

                args = true;
            }

            if (!args) {
                //transfering the result to the collection of data
                out.print("<h3>Prime numbers in range: " + model.getStartRange() + ", " + model.getEndRange() + ": </h3>");
                out.println("<h3>" + model.getArrayOfPrimeNumbers() + "</h3>");
                model.putStats(model.getStartRange() + ", " + model.getEndRange(), String.valueOf(model.getArrayOfPrimeNumbers()));

                //transfer data to the database
                try {
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("INSERT INTO PrimeNumInRangeDatabase VALUES ("
                            + model.getStartRange() + ","
                            + model.getEndRange() + ",'"
                            + String.valueOf(model.getArrayOfPrimeNumbers())
                            + "')");
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
