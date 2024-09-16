<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*, com.onlineBankingSystem.Servlets.DbConnection" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Deposit</title>
    <link rel="stylesheet" href="css/TransStyles.css">
</head>
<body>
	<header>
        <h1>Deposit Money</h1>
    </header>
    <main>
        <form action="TransactionServlet" method="post">
            <label for="accountID">Select Account:</label>
            <select id="accountID" name="accountID">
                <% 
                    // Fetch user's accounts and display them
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

            <label for="amount">Amount:</label>
            <input type="number" id="amount" name="amount" step="0.01" min="0" required>

            <input type="hidden" name="transactionType" value="Deposit">
            <button type="submit">Deposit</button>
        </form>
    </main>
    <footer>
        <p>&copy; 2024 PATIL'S Bank. All rights reserved.</p>
    </footer>
</body>
</html>