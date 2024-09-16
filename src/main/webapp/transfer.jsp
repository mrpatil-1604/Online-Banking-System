<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*, com.onlineBankingSystem.Servlets.DbConnection" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transfer Money</title>
    <link rel="stylesheet" href="css/TransStyles.css">
    <script src="js/validation.js" defer></script> <!-- For client-side validation -->
</head>
<body>
    <header>
        <h1>Transfer Money</h1>
    </header>
    <main>
        <form id="transferForm" action="TransactionServlet" method="post">
            <label for="fromAccount">From Account:</label>
            <select id="fromAccount" name="fromAccountID" required>
                <option value="">--Select Account--</option>
                <% 
                    Connection conn = DbConnection.getConnection();
                    String sql = "SELECT * FROM Accounts WHERE CustomerID = (SELECT CustomerID FROM Customers WHERE Email = ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, (String) session.getAttribute("user"));
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                %>
                    <option value="<%= rs.getInt("AccountID") %>">Account <%= rs.getInt("AccountID") %> - <%= rs.getString("AccountType") %></option>
                <% } %>
            </select>

            <label for="toAccount">To Account:</label>
            <input type="number" id="toAccount" name="toAccountID" required>

            <label for="amount">Amount:</label>
            <input type="number" id="amount" name="amount" step="0.01" min="0" required>

            <input type="hidden" name="transactionType" value="Transfer">
            <button type="submit">Transfer</button>
        </form>
    </main>
    <footer>
        <p>&copy; 2024 PATIL's Bank. All rights reserved.</p>
    </footer>
</body>
</html>
