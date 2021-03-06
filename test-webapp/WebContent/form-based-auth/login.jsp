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
<title>OWASP ZAP Test WebApp - Form Based Authentication</title>
</head>
<body>
	<H2>OWASP ZAP Test WebApp - Form Based Authentication</H2>
	<%
		if (session.getAttribute("username") != null) {
			response.sendRedirect("restricted/home.jsp");
			return;
		}
	%>
	<p>In order to enter the authenticated section of the website,
		please insert your credentials below:</p>
	<form action="loginCheck.jsp" method="post">
		<br />Username:<input type="text" name="username"> <br />Password:<input
			type="password" name="password"> <br /> <input type="submit"
			value="Submit">
	</form>
	<p>
		Valid username / password pairs: <b>user1 / user1</b>,<b>user2 /
			user2</b>,<b>user3 / user3</b>
	</p>
	<p>Unique Identifier usable in tests: fb-unrestricted-login-238934</p>
</body>
</html>
