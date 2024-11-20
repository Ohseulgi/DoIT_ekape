<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Base64" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>판매업소 선택</title>    
</head>
<body>
	<div id="serverData" style="display: none;">
        <%= request.getAttribute("jsonResponse") %>
    </div>
    
	<div class="SearchDiv">
		<span id="SearchLabel" class="SearchLabel"> 업소명<br>(사업자번호)</span>
		<input id="SearchText" class="SearchText" type="text">
		<button id="SearchBtn" class="SearchBtn">🔍</button>
	</div>
	<br><br>
	
	<div class="ResultSearchDiv">
		<div class="ResultField">
			<span class="ResultField_Subject">업소명</span>
			<span id="ResultField_StoreName" class="ResultField_StoreName"><%= session.getAttribute("storeName") != null ? session.getAttribute("storeName") : ""%></span>
		</div>
		<div class="ResultField">
			<span class="ResultField_Subject">사업자번호</span>
			<span id="ResultField_BusinessNumber" class="ResultField_BusinessNumber"><%= session.getAttribute("businessNumber") != null ? session.getAttribute("businessNumber") : ""%></span>
		</div>
		<div class="ResultField">
			<span class="ResultField_Subject">소재지</span>
			<span id="ResultField_Address" class="ResultField_Address"><%= session.getAttribute("address") != null ? session.getAttribute("address") : ""%></span>
		</div>
		<div class="ResultField">
			<span class="ResultField_Subject">대표자</span>
			<span id="ResultField_Name" class="ResultField_Name"><%= session.getAttribute("name") != null ? session.getAttribute("name") : ""%></span>
		</div>
		<div class="ResultField">
			<span class="ResultField_Subject">업태</span>
			<span id="ResultField_StoreType" class="ResultField_StoreType"><%= session.getAttribute("storeType") != null ? session.getAttribute("storeType") : ""%></span>

		</div>
	</div>
	<br><br>
            
	<div class="BtnDiv">
		<button id="HistoryBtn" class="HistoryBtn">점검 내역 조회</button>
		<button id="StartBtn" class="StartBtn">점검/조사 시작</button>
	</div>
	
	<!-- 스크립트 파일 연결 -->
    <script src="<%= request.getContextPath()%>/newSmart/ocr/js/SearchStore.js"></script>

</body>
</html>
