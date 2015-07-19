<%@ page import="es.udc.pojo.servjsptutorial.model.account.Account" %>

<html>
<head> 
  <title>Account Data</title>
</head>

<body text="#000000" bgcolor="#ffffff">

<%
	Account account = (Account)request.getAttribute("account");
%>

<div align="center">
  <p>
    <font color="#000099" face="Arial, Helvetica, sans-serif">
	  <b>Account Information</b>
	</font>
  </p>
</div>	

<div align="center">

<%
if (account!=null) {
%>

	<table border="1" align="center" width="35%">

    	<tr>
        	<th width="60%">Account Identifier</th>
        	<td width="30%" align="center"><%= account.getAccountId() %></td>
    	</tr>

    	<tr>
        	<th width="60%">User Identifier</th>
        	<td width="30%" align="center"><%= account.getUserId() %></td>
    	</tr>

    	<tr>
        	<th width="60%">Balance</th>
        	<td width="30%" align="center"><%= account.getBalance() %></td>
    	</tr>

	</table>

<%  
} else { 
%>

	    <font color="#000099" face="Arial, Helvetica, sans-serif">
		  <b>Account not found</b>
		</font>
<%  
} 
%>

</div>

<br/>
<a href="Index.html">Home</a>
<br/>
</body>

</html>