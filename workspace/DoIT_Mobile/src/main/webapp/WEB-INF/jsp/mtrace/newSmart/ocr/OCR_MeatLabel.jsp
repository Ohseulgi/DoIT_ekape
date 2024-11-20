<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Camera Capture</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/newSmart/ocr/css/CSS_MeatLabel.css"> <!-- CSS 파일 연결 -->
</head>
<body id="camera-page" class="camera-page">

    <!-- Video 컨테이너 추가 -->
    <div class="video-container">
        <!-- 빨간색 테두리 박스 -->
        <div class="red-box"></div>	
        
        <!-- 비디오 요소 -->
        <video id="video" class="camera-video" autoplay playsinline></video>
    </div>
    
	<!-- 버튼 컨테이너 추가 -->
    <div class="button-container">
        <button id="snap" class="camera-button">📷</button>
        <button id="CheckResultBtn" class="CheckResultBtn">결과 확인</button>
    </div>
    
    
	<!-- 서버에 전송할 숨겨진 폼 -->
    <form id="uploadForm" method="post" enctype="multipart/form-data" action="upload" class="upload-form">
        <input type="hidden" id="imageData" name="imageData">
    </form>
	 
	<canvas id="canvas" width="1280" height="1280" class="camera-canvas" style="display: none;"></canvas>

	<!-- 스크립트 파일 연결 -->
    <script src="<%= request.getContextPath()%>/newSmart/ocr/js/camera.js"></script>
    
</body>
</html>
