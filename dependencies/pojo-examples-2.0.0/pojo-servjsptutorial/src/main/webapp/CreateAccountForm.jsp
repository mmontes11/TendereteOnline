<%@ page import="java.util.Map" %>

<html>
<head> 
  <title>Create Account Form</title>
</head>

<body text="#000000" bgcolor="#ffffff">

<%-- Get errors. --%>
	
<%
	String userIdErrorMessage = "";
	String balanceErrorMessage = "";
	Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
	
	if (errors != null) {
	
		String errorHeader = "<font color=\"red\"><b>";
		String errorFooter = "</b></font>";
	
		if (errors.containsKey("userId")) {
			userIdErrorMessage = errorHeader + errors.get("userId") + 
				errorFooter;
		}
		
		if (errors.containsKey("balance")) {
			balanceErrorMessage = errorHeader + errors.get("balance") + 
				errorFooter;
		}
	
	}
	
	String userId = request.getParameter("userId");
	if (userId==null) {
		userId="";
	}
	String balance = request.getParameter("balance");
	if (balance==null) {
		balance="";
	}

%>	
	  
<form method="POST" action="CreateAccount">
		
<table width="100%" border="0" align="center" cellspacing="12">

<%-- User Identifier --%>

	<tr>		
		<th align="right" width="50%">
			User Identifier 
		</th>
		<td align="left">	
			<input type="text" name="userId" 
			    value="<%= userId %>"
				size="16" maxlength="16">
			<%= userIdErrorMessage %>
		</td>
	</tr>
	
<%-- Balance --%>
	
	<tr>		
		<th align="right" width="50%">
			Balance 
		</th>
		<td align="left">	
			<input type="text" name="balance" 
			    value="<%= balance %>" 
				size="16" maxlength="16">
			<%= balanceErrorMessage %>
		</td>
	</tr>	

<%-- Create button --%>
	
	<tr>		
		<td width="50%"></td>
		<td align="left" width="50%">
			<input type="submit" value="Create Account">
		</td>
	</tr>
	
</table>

</form>

</body>

</html>
