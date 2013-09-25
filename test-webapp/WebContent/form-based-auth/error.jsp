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
<title>OWASP ZAP Test WebApp - Form Based Authentication - Error</title>
</head>
<body>
	<H2>OWASP ZAP Test WebApp - Form Based Authentication - Error</H2>

	<%
		Object error = session.getAttribute("error");
		session.removeAttribute("error");
		if (error != null) {
	%>
	<p>An error has occurred:</p>
	<font color="red"> <%
 	out.println(error.toString());
 	}
 %>
	</font>
	<p>
		Back to <a href="index.jsp">home</a>
	</p>

	<p>Unique Identifier usable in tests: fb-unrestricted-error-498762</p>
</body>
</html>