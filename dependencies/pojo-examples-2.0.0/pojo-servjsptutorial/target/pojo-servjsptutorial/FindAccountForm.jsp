<%@ page import="java.util.Map" %>

<html>
<head> 
  <title>Find Account Form</title>
</head>

<body text="#000000" bgcolor="#ffffff">

<%-- Get errors. --%>
    
<%
    String accountIdErrorMessage = "";
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    
    if (errors != null) {
    
        String errorHeader = "<font color=\"red\"><b>";
        String errorFooter = "</b></font>";
    
        if (errors.containsKey("accountId")) {
            accountIdErrorMessage = errorHeader + errors.get("accountId") + 
                errorFooter;
        }
        
    }
    
    String accountId = request.getParameter("accountId");
    if (accountId==null) {
        accountId="";
    }

%>  
      
<form method="GET" action="FindAccount">
        
<table width="100%" border="0" align="center" cellspacing="12">

<%-- Account Identifier --%>

    <tr>        
        <th align="right" width="50%">
            Account Identifier 
        </th>
        <td align="left">   
            <input type="text" name="accountId" 
                value="<%= accountId %>"
                size="16" maxlength="16">
            <%= accountIdErrorMessage %>
        </td>
    </tr>
    
<%-- Search button --%>
    
    <tr>        
        <td width="50%"></td>
        <td align="left" width="50%">
            <input type="submit" value="Find Account">
        </td>
    </tr>
    
</table>

</form>

</body>

</html>
