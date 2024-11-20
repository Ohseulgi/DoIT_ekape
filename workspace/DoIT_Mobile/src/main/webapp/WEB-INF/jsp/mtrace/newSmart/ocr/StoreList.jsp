<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>Store List</title>
    <script src="js/StoreList.js"></script> <!-- JS 파일 포함 -->
</head>
<body>
    <h1>검색 결과</h1>
    <table border="1" id="storeListTable">
        <thead>
            <tr>
                <th>상호명</th>
                <th>사업자번호</th>
                <th>주소</th>
                <th>대표자명</th>
                <th>업종</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="store" items="${storeList}">
                <tr onclick="selectRow(this)">
                    <td>${store.storeName}</td>
                    <td>${store.businessNumber}</td>
                    <td>${store.address}</td>
                    <td>${store.name}</td>
                    <td>${store.storeType}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>