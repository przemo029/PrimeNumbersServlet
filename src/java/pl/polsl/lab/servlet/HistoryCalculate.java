package pl.polsl.lab.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab.model.PrimeNumbersModel;

/**
 * @author Przemysław Drążkiewicz
 * @version 2.0
 */
public class HistoryCalculate extends HttpServlet {

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
        Connection conn = (Connection) session.getAttribute("conn");

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

        //if the model hasn't been initialized earlier print the statement
        if (model == null) {
            out.println("<h3>The calculation history is empty<h3/>");
        } else {
            //use cookies to draw last result of nth prime number calculaltion
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastResult")) {
                    out.println("Last result in nth prime number (cookies): <br/>");
                    out.println(cookie.getValue() + "<br/>");
                    break;
                }
            }
            //printing the result from the data collection
            out.println("<h3>History of prime numbers in range (collection of data):</h3>");
            //read history of prime number in range from hashtable
            Hashtable<String, String> stats = model.getStats();
            for (Map.Entry<String, String> entry : stats.entrySet()) {
                String range = entry.getKey();
                String result = entry.getValue();
                out.println("<br/>Prime numbers in range: " + range);
                out.println("<br/>" + result + "<br/>");
            }
        }
        //if the connection hasn't been initialized earlier print the statement
        if (conn == null) {
            out.println("<h3>Databse is empty<h3/>");
        } else {
            try {
                out.println("<h3>DATABASE:</h3>");
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM IsPrimeTable");
                out.println("<h3>IsPrimeTable: <h3/>");
                out.println("Number &emsp; isPrime &emsp; &emsp; <br>");
                while (rs.next()) {
                    out.println(rs.getInt("Number") + "&emsp; &emsp;&emsp; &emsp;");
                    out.println(rs.getBoolean("isPrime") + "<br/>");
                }
                rs.close();
                ResultSet rs2 = statement.executeQuery("SELECT * FROM PrimeNumInRangeDatabase");
                out.println("<h3>PrimeNumInRangeTable: <h3/>");
                while (rs2.next()) {
                    out.println("Begin &emsp; End &emsp; &emsp; <br>");
                    out.println(rs2.getInt("start") + "&emsp; &emsp;&emsp;");
                    out.println(rs2.getInt("finish") + "<br/>");
                    out.println(rs2.getString("Range") + "<br/><br/>");
                }
                rs2.close();
                ResultSet rs3 = statement.executeQuery("SELECT * FROM NthPrimeNumberTable");
                out.println("<h3>NthPrimeNumberTable: <h3/>");
                out.println("Value of nth &emsp; Nth prime number &emsp; &emsp; <br>");
                while (rs3.next()) {
                    out.println(rs3.getInt("ValueOfNth") + "&emsp; &emsp;&emsp; &emsp; &emsp;");
                    out.println(rs3.getInt("NthPrimeNumber") + "<br/>");;
                }
                rs3.close();
            } catch (SQLException e) {
                out.println("SQL exception: " + e.getMessage());
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
