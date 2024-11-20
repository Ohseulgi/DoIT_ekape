// js/confirm.js

// 초기화 버튼
function resetInput(index) {
    document.getElementById("input_" + index).value = "";
}

// 등급 선택 옵션 업데이트 함수
function updateGradeOptions() {
    const meatType = document.getElementById("meatType").value;
    const gradeSelect = document.getElementById("grade");

    // 등급 드롭다운 옵션 초기화
    gradeSelect.innerHTML = '';
    
    // 기본 옵션 "---" 추가
    const defaultOption = document.createElement("option");
    defaultOption.value = "Not Found";
    defaultOption.text = "---";
    gradeSelect.appendChild(defaultOption);

    // 쇠고기와 돼지고기 등급 옵션 정의
    const beefGrades = ["1++", "1+", "1", "2", "3", "등외"];
    const porkGrades = ["1+", "1", "2", "등외"];

    // "식육의종류" 조건에 따른 옵션 추가
    if (meatType.includes("쇠고기")) {
        beefGrades.forEach(grade => {
            const option = document.createElement("option");
            option.value = grade;
            option.text = grade;
            gradeSelect.appendChild(option);
        });
    } else if (meatType.includes("돼지고기")) {
        porkGrades.forEach(grade => {
            const option = document.createElement("option");
            option.value = grade;
            option.text = grade;
            gradeSelect.appendChild(option);
        });
    }

    // 기본 선택 옵션 설정
    gradeSelect.value = "Not Found";
}

// 고기 부위 선택 옵션 업데이트 함수
function updateMeatOptions() {
    const meatType = document.getElementById("meatType").value;
    const meatPartSelect = document.getElementById("meatPart");
    const subMeatPartSelect = document.getElementById("subMeatPart");

    // 기본 옵션 설정
    function setDefaultOptions(selectElement) {
        selectElement.innerHTML = '';
        const defaultOption = document.createElement("option");
        defaultOption.value = "Not Found";
        defaultOption.text = "---";
        selectElement.appendChild(defaultOption);
    }

    // 부위 옵션 초기화
    setDefaultOptions(meatPartSelect);
    setDefaultOptions(subMeatPartSelect);

    // 쇠고기 및 돼지고기 부위 맵
    const beefCutsMap = {
		"안심": ["안심살"],
        "등심": ["윗등심살", "꽃등심살", "아래등심살", "살치살"],
        "목심": ["목심살"],
        "채끝": ["채끝살"],
        "앞다리": ["꾸리살", "부채살", "앞다리살", "갈비덧살", "부채덮개살"],
        "우둔": ["우둔살", "홍두깨살"],
        "설도": ["보섭살", "설깃머리살", "도가니살", "삼각살"],
        "양지": ["양지머리", "차돌박이", "업진살", "업진안살", "치마양지", "치마살", "앞치마살"],
        "사태": ["앞사태", "뒷사태", "뭉치사태", "아롱사태", "상박살"],
        "갈비": ["본갈비", "꽃갈비", "참갈비", "갈비살", "마구리", "토시살", "안창살", "제비추리"]
    };

    const porkCutsMap = {
		"안심": ["안심살"],
        "등심": ["등심살", "알등심살", "등심덧살"],
        "목심": ["목심살"],
        "앞다리": ["앞다리살", "앞사태살", "항정살", "꾸리살", "부채살", "주걱살"],
        "뒷다리": ["볼기살", "설깃살", "도가니살", "홍두깨살", "보섭살", "뒷사태살"],
        "삼겹살": ["삼겹살", "갈매기살", "등갈비", "토시살", "오돌삼겹"],
        "갈비": ["갈비", "갈비살", "마구리"]
    };

    let selectedMap = null;
    if (meatType.includes("쇠고기")) {
        selectedMap = beefCutsMap;
    } else if (meatType.includes("돼지고기")) {
        selectedMap = porkCutsMap;
    }

    if (selectedMap) {
        for (const part in selectedMap) {
            const option = document.createElement("option");
            option.value = part;
            option.text = part;
            meatPartSelect.appendChild(option);
        }

        meatPartSelect.addEventListener("change", function () {
            const selectedPart = meatPartSelect.value;
            setDefaultOptions(subMeatPartSelect);

            if (selectedPart && selectedMap[selectedPart]) {
                selectedMap[selectedPart].forEach(subPart => {
                    const option = document.createElement("option");
                    option.value = subPart;
                    option.text = subPart;
                    subMeatPartSelect.appendChild(option);
                });
            }
            subMeatPartSelect.value = "Not Found";
        });
    } else {
        setDefaultOptions(meatPartSelect);
        setDefaultOptions(subMeatPartSelect);
    }
}

// 부위명칭(대), 부위명칭(소) 설정
function setMeatOptions(selectedMeatPart, selectedSubMeatPart) {
    const meatPartSelect = document.getElementById("meatPart");
    const subMeatPartSelect = document.getElementById("subMeatPart");

    if (selectedMeatPart !== "Not Found") {
        meatPartSelect.value = selectedMeatPart;
        const event = new Event("change");
        meatPartSelect.dispatchEvent(event);
    }

    if (selectedSubMeatPart !== "Not Found") {
        subMeatPartSelect.value = selectedSubMeatPart;
        const event = new Event("change");
        subMeatPartSelect.dispatchEvent(event);
    }
}


// 페이지가 로드될 때 호출되는 초기 설정 함수
function initializePage(arrValue) {
    updateGradeOptions();
    updateMeatOptions();
    setMeatOptions(arrValue[3], arrValue[4]);
    /*
    console.log("원산지: ", arrValue[1]);
    console.log("식육의종류: ", arrValue[2]);
    console.log("대분할: ", arrValue[3]);
    console.log("소분할: ", arrValue[4]);
    */
}
