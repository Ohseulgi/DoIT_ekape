

function selectRow(row) {
    const cells = row.getElementsByTagName('td');
    const businessNumber = cells[1].innerText;

    console.log('선택된 사업자번호:', businessNumber);

    // POST 요청을 보내고 화면 전환
    fetch('/DoIT_Mobile/newSmart/ocr/SearchStore', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'searchValue=' + encodeURIComponent(businessNumber) + 
        '&StoreList=' + encodeURIComponent('StoreList')
    })
    .then(() => {
        // POST 요청 완료 후 바로 페이지 전환
        window.location.href = '/DoIT_Mobile/newSmart/ocr/SearchStore';
    })
    .catch((error) => {
        console.error('오류 발생:', error);
    });
}
