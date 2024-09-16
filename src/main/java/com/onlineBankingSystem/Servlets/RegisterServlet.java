package com.onlineBankingSystem.Servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        try {
        	Connection conn = DbConnection.getConnection();
            String checkEmailSql = "SELECT * FROM Customers WHERE Email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();

            if (emailRs.next()) {
                // Email already exists
                response.sendRedirect("register.jsp?error=Email already exists. Please use a different email.");
                return;
            }
            // Step 1: Insert Customer into the Customers table
            String insertCustomerSql = "INSERT INTO Customers (Name, Email, Phone, Address, Password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertCustomerStmt = conn.prepareStatement(insertCustomerSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertCustomerStmt.setString(1, name);
            insertCustomerStmt.setString(2, email);
            insertCustomerStmt.setString(3, phone);
            insertCustomerStmt.setString(4, address);
            insertCustomerStmt.setString(5, password);
            insertCustomerStmt.executeUpdate();

            // Step 2: Retrieve the generated CustomerID
            ResultSet generatedKeys = insertCustomerStmt.getGeneratedKeys();
            int customerID = 0;
            if (generatedKeys.next()) {
                customerID = generatedKeys.getInt(1);
            }

            // Step 3: Insert an account for the new customer into the Accounts table
            String accountType = "Savings"; // Default account type or based on user selection
            BigDecimal startingBalance = new BigDecimal("0.00"); // Starting balance for new account
            String insertAccountSql = "INSERT INTO Accounts (CustomerID, AccountType, Balance) VALUES (?, ?, ?)";
            PreparedStatement insertAccountStmt = conn.prepareStatement(insertAccountSql);
            insertAccountStmt.setInt(1, customerID);
            insertAccountStmt.setString(2, accountType);
            insertAccountStmt.setBigDecimal(3, startingBalance);
            insertAccountStmt.executeUpdate();

            // Redirect to a success page or login page
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Registration failed");
        }
		doGet(request, response);
	}

}
