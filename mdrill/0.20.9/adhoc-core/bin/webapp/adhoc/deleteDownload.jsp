<%@ page contentType="text/html; charset=utf-8" %><%@ page import="java.io.*" %><%@ page import="javax.servlet.*" %><%@ page import="java.net.*" %><%@ page import="java.util.*" %><%@ page import="java.util.regex.*" %><%@ page import="java.text.*" %><%@ page import="com.alimama.web.*" %><%
		String uuid = request.getParameter("uuid");
		String callback = request.getParameter("callback");
String resultstr=TableJoin.DeleteDownload(uuid,callback);
%><%=resultstr%>
