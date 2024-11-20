// HTML 요소 가져오기
const SearchText = document.getElementById('SearchText');
const SearchBtn = document.getElementById('SearchBtn');
const StartBtn = document.getElementById('StartBtn');

// 검색 함수
function search() {
    const searchValue = SearchText.value.trim();
    
    if (!searchValue) {
        alert("업소명 혹은 사업자번호를 입력하세요.");
        return;
    }

    // 서버로 입력값 전송
    fetch('/DoIT_Mobile/newSmart/ocr/SearchStore', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'searchValue=' + encodeURIComponent(searchValue)
    })
    .then(response => response.json())
    .then(data => {
        if (data.resultCount === 1) {
            // 1개의 결과가 있는 경우
            document.getElementById('ResultField_StoreName').innerText = data.storeName;
            document.getElementById('ResultField_BusinessNumber').innerText = data.businessNumber;
            document.getElementById('ResultField_Address').innerText = data.address;
            document.getElementById('ResultField_Name').innerText = data.name;
            document.getElementById('ResultField_StoreType').innerText = data.storeType;
            console.log('ResultField_StoreName: ', data.storeName);
        } else if (data.resultCount === 0) {
            // 결과가 없는 경우
            alert('올바른 업소명 혹은 사업자번호를 입력하세요');
        } else {
            // 결과가 2개 이상인 경우 리다이렉트 또는 처리
            window.location.href = data.redirect;	// StoreList.jsp로 리다이렉션
        }
    })
    .catch(error => console.error('Error:', error));
}

function startInspection() {
    const storeName = document.getElementById('ResultField_StoreName').innerText;
    const businessNumber = document.getElementById('ResultField_BusinessNumber').innerText;
    const address = document.getElementById('ResultField_Address').innerText;
    const name = document.getElementById('ResultField_Name').innerText;
    const storeType = document.getElementById('ResultField_StoreType').innerText;

    console.log('storeName:', storeName);
    console.log('businessNumber:', businessNumber);
    console.log('address:', address);
    console.log('name:', name);
    console.log('storeType:', storeType);

    if (!businessNumber) {
        alert('점검 판매업소를 지정해주세요.');
    } else {
        const baseUrl = '/DoIT_Mobile/newSmart/ocr/OCR_MeatLabel';
        const queryParams = `?storeName=${encodeURIComponent(storeName)}&businessNumber=${encodeURIComponent(businessNumber)}&address=${encodeURIComponent(address)}&name=${encodeURIComponent(name)}&storeType=${encodeURIComponent(storeType)}`;
        window.location.href = baseUrl + queryParams;
    }
}



// 이벤트 설정
SearchBtn.addEventListener('click', function() {
    search();
});

StartBtn.addEventListener('click', function() {
	startInspection();
});