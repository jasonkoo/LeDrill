<%@ page contentType="text/html; charset=GB2312" %>
<%@ page import="java.util.*" %>
<%@ page import="com.alimama.web.*" %>
<html>
<head>
    <title>tablelist</title>
</head>

<body>
	<h1>�������ݱ��б�</h1>
<hr>
<table border=1>
	<tr><td>���ݱ���</td><td>���</td><td>��schema</td></tr>
<%
		String[] list=TableList.getTablelist();
		for(String tbl:list)
		{
				%>
					<tr><td><%=tbl%></td><td><a href="./tableshards.jsp?tablename=<%=tbl%>">�鿴</a></td><td><a href="./tableSchema.jsp?tablename=<%=tbl%>">�鿴</a></td></tr>
					
					<%
		}
%>

</table>
</body>
</html>
