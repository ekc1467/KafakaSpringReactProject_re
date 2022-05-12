<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	ArrayList<ResultDB> resultList =  request.getAttribute("resultList")
</script>
</head>
<body>
	<h1>index</h1>
	<a href="t1">링크1</a>
	
	<table>
		<thead>
			<td>id</td>
			<td>이름</td>
			<td>문제번호1</td>
			<td>문제번호2</td>
			<td>정답</td>
		</thead>
		<tbody id="mulrank">
		
		</tbody>
	</table>
</body>
<script>
	let mulresult = document.getElementById("mulrank");
	mulresult.innerHTML = 
</script>
</html>