<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
    This file is part of the OWASP Zed Attack Proxy (ZAP) project (http://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project)
    ZAP is an HTTP/HTTPS proxy for assessing web application security.
    
    Licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 
    You may obtain a copy of the License at 
    
      http://www.apache.org/licenses/LICENSE-2.0 
      
    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License. 
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OWASP ZAP Test WebApp - Form Based Authentication -
	Authenticated Page AC</title>
</head>
<body>
	<H2>OWASP ZAP Test WebApp - Authenticated Session</H2>
	<%
		if (session.getAttribute("username") == null) {
			session.setAttribute("error", "Unauthorized");
			response.sendRedirect("../error.jsp");
			return;
		}
	%>
	<p>
		Welcome:
		<%=session.getAttribute("username").toString()%>
		- <a href="/zap-test-webapp/form-based-auth/logout.jsp">Logout</a>
	</p>
	<p>This section is accessible only to authenticated users.</p>
	
	<p>
		Back <a href="home.jsp">Home</a>
	</p>
	<p>Unique Identifier usable in tests: restricted-ac-863602</p>
</body>
</html>