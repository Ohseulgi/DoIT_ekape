// 카메라 스트림과 관련된 변수들
let currentFacingMode = "environment"; // 기본 후면 카메라 모드
let stream = null;

// HTML 요소 가져오기
const bodyElement = document.getElementById('camera-page');
const videoElement = document.getElementById('video');
const canvasElement = document.getElementById('canvas');
const context = canvasElement.getContext('2d');
const imageDataInput = document.getElementById('imageData');
const snapButton = document.getElementById('snap');
const uploadForm = document.getElementById('uploadForm');

// 요소의 CSS 스타일을 가져옵니다.
const element = document.querySelector('.red-box');
const computedStyle = getComputedStyle(element);

// 너비와 높이를 px 단위로 가져옵니다.
const width = parseFloat(computedStyle.width);
const height = parseFloat(computedStyle.height);

// 부모 요소의 크기를 가져와 비율을 계산합니다.
const parentElement = element.parentElement;
const parentWidth = parentElement.clientWidth;
const parentHeight = parentElement.clientHeight;

// 비율 계산
const widthRatio = Math.round((width / parentWidth) * 100); // 백분율로 변환 후 반올림
const heightRatio = Math.round((height / parentHeight) * 100); // 백분율로 변환 후 반올림

console.log(`Red-Box Width Ratio: ${widthRatio}%`);
console.log(`Red-Box Height Ratio: ${heightRatio}%`);

// 카메라 시작 함수
function startCamera() {
    const constraints = {
        audio: false,
        video: { facingMode: { ideal: "environment" } }
    };

    navigator.mediaDevices.getUserMedia(constraints)
        .then(function(mediaStream) {
            stream = mediaStream;
            videoElement.srcObject = mediaStream;
            videoElement.play();
        })
        .catch(function(error) {
            console.error("Error accessing media devices: ", error);
        });
    console.log(`카메라 모드: ${currentFacingMode}%`);
}

// 사진을 캡처하고 업로드 폼에 데이터 전송 함수
function captureAndSubmit() {
    const formData = new FormData();
    context.clearRect(0, 0, canvasElement.width, canvasElement.height);
    context.drawImage(videoElement, 0, 0, canvasElement.width, canvasElement.height);
    
    const currentTime = new Date().toISOString();
    const deviceInfo = navigator.userAgent;

    canvasElement.toBlob(function(blob) {
    	formData.append('imageData', blob, 'canvas_image.png');
    	formData.append('widthRatio', widthRatio);
        formData.append('heightRatio', heightRatio);
        formData.append('currentTime', currentTime);
        formData.append('deviceInfo', deviceInfo);
        
        // URL에서 추출한 데이터 추가
        const urlParams = new URLSearchParams(window.location.search);
        formData.append('storeName', urlParams.get('storeName'));
        formData.append('businessNumber', urlParams.get('businessNumber'));
        formData.append('address', urlParams.get('address'));
        formData.append('name', urlParams.get('name'));
        formData.append('storeType', urlParams.get('storeType'));
        
        fetch('/DoIT_Mobile/newSmart/ocr/OCR_MeatLabel', {
        	method: 'POST',
            body: formData
        })
        .then(response => {
        	//console.log('response: ', response);
        	
        	if (response.ok) {
                console.log('Image uploaded successfully!');
                window.location.href = "/DoIT_Mobile/newSmart/ocr/OCR_Confirm"; // 업로드 성공 시 OCR_Confirm.jsp로 이동
            } else {
                console.error('Error uploading image:', response.statusText);
                alert('이미지 업로드 중 오류가 발생했습니다. 다시 시도해주세요.');
            }
        })
        .catch(error => {
        	console.error('Error:', error);
            alert('네트워크 오류가 발생했습니다. 다시 시도해주세요.');
        });
    }, 'image/png');
    
    

}

// 이벤트 설정
snapButton.addEventListener('click', function() {
    captureAndSubmit();
});

// 페이지 로드 시 카메라 초기화
startCamera();

window.onload = function() {
    const redBox = document.querySelector('.red-box');
    const videoContainer = document.querySelector('.video-container');

    const redBoxRect = redBox.getBoundingClientRect();
    const videoContainerRect = videoContainer.getBoundingClientRect();

    const topFilterHeight = redBoxRect.top;
    const bottomFilterHeight = videoContainerRect.height - (redBoxRect.top + redBoxRect.height);
    const leftFilterWidth = redBoxRect.left;
    const rightFilterWidth = videoContainerRect.width - (redBoxRect.left + redBoxRect.width);

    const topFilter = document.createElement('div');
    topFilter.classList.add('video-filter');
    topFilter.style.top = '0';
    topFilter.style.left = '0';
    topFilter.style.width = '100%';
    topFilter.style.height = `${topFilterHeight}px`;
    videoContainer.appendChild(topFilter);

    const bottomFilter = document.createElement('div');
    bottomFilter.classList.add('video-filter');
    bottomFilter.style.bottom = '0';
    bottomFilter.style.left = '0';
    bottomFilter.style.width = '100%';
    bottomFilter.style.height = `${bottomFilterHeight}px`;
    videoContainer.appendChild(bottomFilter);

    const leftFilter = document.createElement('div');
    leftFilter.classList.add('video-filter');
    leftFilter.style.top = `${redBoxRect.top}px`;
    leftFilter.style.left = '0';
    leftFilter.style.width = `${leftFilterWidth}px`;
    leftFilter.style.height = `${redBoxRect.height}px`;
    videoContainer.appendChild(leftFilter);

    const rightFilter = document.createElement('div');
    rightFilter.classList.add('video-filter');
    rightFilter.style.top = `${redBoxRect.top}px`;
    rightFilter.style.right = '0';
    rightFilter.style.width = `${rightFilterWidth}px`;
    rightFilter.style.height = `${redBoxRect.height}px`;
    videoContainer.appendChild(rightFilter);

    const labelText_Top = document.createElement('p');
    labelText_Top.textContent = "식육라벨을 사각형 안에 꽉 차도록 배치해주세요.";
    labelText_Top.style.position = 'absolute';
    labelText_Top.style.top = '15%';
    labelText_Top.style.width = '100%';
    labelText_Top.style.textAlign = 'center';
    labelText_Top.style.color = 'white';
    labelText_Top.style.fontWeight = 'bold';
    labelText_Top.style.fontSize = '1.2em';
    labelText_Top.style.zIndex = '25';
    bodyElement.appendChild(labelText_Top);

    const labelText_Bottom = document.createElement('p');
    labelText_Bottom.textContent = "빛이 반사되지 않도록 주의하세요.";
    labelText_Bottom.style.position = 'absolute';
    labelText_Bottom.style.top = '75%';
    labelText_Bottom.style.width = '100%';
    labelText_Bottom.style.textAlign = 'center';
    labelText_Bottom.style.color = 'white';
    labelText_Bottom.style.fontSize = '0.8em';
    labelText_Bottom.style.zIndex = '25';
    bodyElement.appendChild(labelText_Bottom);
};
