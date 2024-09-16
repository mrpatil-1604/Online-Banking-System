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
 * Servlet implementation class TransactionServlet
 */
@WebServlet("/TransactionServlet")
public class TransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TransactionServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionType = request.getParameter("transactionType");
        Connection conn = DbConnection.getConnection();

        try {
            if ("Deposit".equals(transactionType) || "Withdrawal".equals(transactionType)) {
                String accountIDParam = request.getParameter("accountID");
                String amountParam = request.getParameter("amount");

                // Ensure accountID and amount are provided
                if (accountIDParam == null || amountParam == null || accountIDParam.trim().isEmpty() || amountParam.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Account ID and amount are required.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                int accountID = Integer.parseInt(accountIDParam);
                BigDecimal amount = new BigDecimal(amountParam);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("errorMessage", "Amount must be greater than zero.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                if ("Deposit".equals(transactionType)) {
                    deposit(conn, accountID, amount);
                } else if ("Withdrawal".equals(transactionType)) {
                    if (hasSufficientBalance(conn, accountID, amount)) {
                        withdraw(conn, accountID, amount);
                    } else {
                        request.setAttribute("errorMessage", "Insufficient balance.");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                        return;
                    }
                }

            } else if ("Transfer".equals(transactionType)) {
                String fromAccountIDParam = request.getParameter("fromAccountID");
                String toAccountIDParam = request.getParameter("toAccountID");
                String amountParam = request.getParameter("amount");

                // Ensure fromAccountID, toAccountID, and amount are provided
                if (fromAccountIDParam == null || toAccountIDParam == null || amountParam == null ||
                        fromAccountIDParam.trim().isEmpty() || toAccountIDParam.trim().isEmpty() || amountParam.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "From Account ID, To Account ID, and amount are required.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                int fromAccountID = Integer.parseInt(fromAccountIDParam);
                int toAccountID = Integer.parseInt(toAccountIDParam);
                BigDecimal amount = new BigDecimal(amountParam);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("errorMessage", "Amount must be greater than zero.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }

                if (hasSufficientBalance(conn, fromAccountID, amount)) {
                    transfer(conn, fromAccountID, toAccountID, amount);
                } else {
                    request.setAttribute("errorMessage", "Insufficient balance for transfer.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            }

            response.sendRedirect("dashboard.jsp");

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deposit(Connection conn, int accountID, BigDecimal amount) throws SQLException {
        String updateSql = "UPDATE Accounts SET Balance = Balance + ? WHERE AccountID = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setBigDecimal(1, amount);
            updateStmt.setInt(2, accountID);
            updateStmt.executeUpdate();

            logTransaction(conn, accountID, "Deposit", amount);
        }
    }

    private void withdraw(Connection conn, int accountID, BigDecimal amount) throws SQLException {
        String updateSql = "UPDATE Accounts SET Balance = Balance - ? WHERE AccountID = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setBigDecimal(1, amount);
            updateStmt.setInt(2, accountID);
            updateStmt.executeUpdate();

            logTransaction(conn, accountID, "Withdrawal", amount);
        }
    }

    private void transfer(Connection conn, int fromAccountID, int toAccountID, BigDecimal amount) throws SQLException {
        withdraw(conn, fromAccountID, amount);
        deposit(conn, toAccountID, amount);

        logTransaction(conn, fromAccountID, "Transfer Out", amount);
        logTransaction(conn, toAccountID, "Transfer In", amount);
    }

    private void logTransaction(Connection conn, int accountID, String transactionType, BigDecimal amount) throws SQLException {
        String insertSql = "INSERT INTO Transactions (AccountID, TransactionType, Amount, TransactionDate) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setInt(1, accountID);
            insertStmt.setString(2, transactionType);
            insertStmt.setBigDecimal(3, amount);
            insertStmt.executeUpdate();
        }
    }

    private boolean hasSufficientBalance(Connection conn, int accountID, BigDecimal amount) throws SQLException {
        String balanceSql = "SELECT Balance FROM Accounts WHERE AccountID = ?";
        try (PreparedStatement balanceStmt = conn.prepareStatement(balanceSql)) {
            balanceStmt.setInt(1, accountID);
            ResultSet rs = balanceStmt.executeQuery();
            if (rs.next()) {
                BigDecimal balance = rs.getBigDecimal("Balance");
                return balance.compareTo(amount) >= 0;
            }
        }
        return false;
    }
}
