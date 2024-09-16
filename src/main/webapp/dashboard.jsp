<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*, com.onlineBankingSystem.Servlets.DbConnection" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
    <link rel="stylesheet" href="css/dashStyles.css">
</head>
<body>
	<header>
        <h1>Welcome, <%= session.getAttribute("userName") %></h1>
        <nav>
            <ul>
                <li><a href="index.jsp">Home</a></li>
                <li><a href="Log_outServlet">Logout</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <h2>Your Accounts</h2>
        <table>
            <thead>
                <tr>
                    <th>Account ID</th>
                    <th>Account Type</th>
                    <th>Balance</th>
                </tr>
            </thead>
            <tbody>
                <%
                    Connection conn = DbConnection.getConnection();
                    String sql = "SELECT * FROM Accounts WHERE CustomerID = (SELECT CustomerID FROM Customers WHERE Email = ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, (String) session.getAttribute("user"));
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                %>
                <tr>
                    <td><%= rs.getInt("AccountID") %></td>
                    <td><%= rs.getString("AccountType") %></td>
                    <td><%= rs.getBigDecimal("Balance") %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <h3>Transaction History</h3>
	<table>
	    <thead>
	        <tr>
	            <th>Transaction ID</th>
	            <th>Account ID</th>
	            <th>Type</th>
	            <th>Amount</th>
	            <th>Date</th>
	        </tr>
	    </thead>
	    <tbody>
	        <% 
	            // Fetch and display transaction history
	            String historySql = "SELECT * FROM Transactions WHERE AccountID IN (SELECT AccountID FROM Accounts WHERE CustomerID = (SELECT CustomerID FROM Customers WHERE Email = ?))";
	            PreparedStatement historyStmt = conn.prepareStatement(historySql);
	            historyStmt.setString(1, (String) session.getAttribute("user"));
	            ResultSet historyRs = historyStmt.executeQuery();
	
	            while (historyRs.next()) {
	        %>
	            <tr>
	                <td><%= historyRs.getInt("TransactionID") %></td>
	                <td><%= historyRs.getInt("AccountID") %></td>
	                <td><%= historyRs.getString("TransactionType") %></td>
	                <td><%= historyRs.getBigDecimal("Amount") %></td>
	                <td><%= historyRs.getTimestamp("TransactionDate") %></td>
	            </tr>
	        <% } %>
	    </tbody>
	</table>

        <!-- Transaction Links -->
        <h3>Perform Transactions</h3>
        <ul class="transaction-links">
            <li><a href="deposit.jsp">Deposit Money</a></li>
            <li><a href="withdrawal.jsp">Withdraw Money</a></li>
            <li><a href="transfer.jsp">Transfer Money</a></li>
        </ul>
    </main>

    <footer>
        <p>&copy; 2024 Our Bank. All rights reserved.</p>
    </footer>
    <script src="../js/scripts.js"></script>
</body>
</html>