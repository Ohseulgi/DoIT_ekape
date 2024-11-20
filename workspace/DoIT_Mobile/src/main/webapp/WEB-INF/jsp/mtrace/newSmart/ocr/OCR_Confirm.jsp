<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Base64" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    // 세션에서 arrKey와 arrValue 가져오기
    String[] arrKey = (String[]) session.getAttribute("arrKey");
    String[] arrValue = (String[]) session.getAttribute("arrValue");

    // null 처리: 세션에 값이 없는 경우 빈 배열로 초기화
    if (arrKey == null) {
        arrKey = new String[0];
    }
    if (arrValue == null) {
        arrValue = new String[0];
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>축산물 이력제 점검</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/newSmart/ocr/css/CSS_Confirm.css"> <!-- CSS 파일 연결 -->
    <script>	    
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
		    const beefGrades = ["1++(9)", "1++(8)", "1++(7)", "1+", "1", "2", "3", "등외"];
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
	            // meatPartSelect에 부위 추가
	            for (const part in selectedMap) {
	                const option = document.createElement("option");
	                option.value = part;
	                option.text = part;
	                meatPartSelect.appendChild(option);
	            }
	
	            // meatPart 변경 시 subMeatPart 업데이트
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
		function setMeatOptions() {
			const meatPartSelect = document.getElementById("meatPart");
	        //const subMeatPartSelect = document.getElementById("subMeatPart");
	        //subMeatPart
	        const subMeatPartSelect = document.getElementById("subMeatPart");
	        
	        const selectedMeatPart = "<%= arrValue[3] %>"; // 대분할
            const selectedSubMeatPart = "<%= arrValue[4] %>"; // 소분할
            
			if (selectedMeatPart !== "Not Found") {
				//meatPartSelect
				meatPartSelect.value = selectedMeatPart;
	            const event = new Event("change");
	            meatPartSelect.dispatchEvent(event); // 선택된 meatPart에 따라 subMeatPart 갱신
			}
			
			if (selectedSubMeatPart !== "Not Found") {
				//subMeatPartSelect
				subMeatPartSelect.value = selectedSubMeatPart;
	            const event = new Event("change");
	            subMeatPartSelect.dispatchEvent(event); // 선택된 meatPart에 따라 subMeatPart 갱신
			}
		}
		


        
		// ----------------------------------------------------------------------------------------------------------------
		// ----------------------------------------------------------------------------------------------------------------
        // 페이지가 로드될 때 기본값에 맞는 등급 목록을 설정
        window.onload = function() {
            updateGradeOptions(); // 등급 목록 표시
            updateMeatOptions(); // 고기 부위 옵션 초기 설정
         	// 부위명칭(대), 부위명칭(소) 설정
	        setMeatOptions();
            const var1 = "<%= arrValue[1] %>";
            const var2 = "<%= arrValue[2] %>";
            
            const selectedMeatPart = "<%= arrValue[3] %>"; // 대분할
            const selectedSubMeatPart = "<%= arrValue[4] %>"; // 소분할
            
            console.log("원산지: ", var1);
            console.log("식육의종류: ", var2);
            console.log("대분할: ", selectedMeatPart);
            console.log("소분할: ", selectedSubMeatPart);
            
        };

     	// ----------------------------------------------------------------------------------------------------------------
     	// ----------------------------------------------------------------------------------------------------------------
    </script>
</head>

<body>
    <h2>축산물 이력제 점검</h2>
    
    <!-- 잘린 이미지 미리보기 -->
    <div id="imagePreview" class="imagePreview">
        <%
            byte[] croppedImageBytes = (byte[]) session.getAttribute("croppedImage");
            if (croppedImageBytes != null) {
                String base64Image = Base64.getEncoder().encodeToString(croppedImageBytes);
        %>
                <img class="img" src="data:image/png;base64,<%= base64Image %>" alt="잘린 이미지" style="max-width: 100%;">
        <%
            } else {
        %>
                <p>잘린 이미지가 없습니다.</p>
        <%
            }
        %>
    </div>

    <!-- OCR 결과 표시 -->
    <div id="ocrResult" class="ocrResult">
	    <%
	        //String[] arrKey = (String[]) session.getAttribute("arrKey");
	        //String[] arrValue = (String[]) session.getAttribute("arrValue");
	
	        if (arrKey != null && arrValue != null) {
	            for (int i = 0; i < arrKey.length; i++) {
	                String key = arrKey[i];
	    %>
	                <div class="ocr-field">
	                    <label for="input_<%= i %>"><%= key %>:</label>
	                    
	                    <% if ("원산지".equals(key)) { %>
	                        <select id="origin">
	                            <option value="국내산" <%= "국내산".equals(arrValue[i]) ? "selected" : "" %>>국내산</option>
	                            <option value="외국산" <%= "외국산".equals(arrValue[i]) ? "selected" : "" %>>외국산</option>
	                        </select>
	                    <% } else if ("식육의종류".equals(key)) { %>
	                        <select id="meatType" onchange="updateGradeOptions(); updateMeatOptions();">
							    <option value="Not Found" <%= (arrValue[i] == null || 
							                                  (!"쇠고기".equals(arrValue[i]) && 
							                                   !"쇠고기(한우)".equals(arrValue[i]) && 
							                                   !"쇠고기(젖소)".equals(arrValue[i]) && 
							                                   !"쇠고기(육우)".equals(arrValue[i]) && 
							                                   !"돼지고기".equals(arrValue[i]) && 
							                                   !"닭고기".equals(arrValue[i]) && 
							                                   !"오리고기".equals(arrValue[i]))) ? "selected" : "" %>>---</option>
							    <option value="쇠고기" <%= "쇠고기".equals(arrValue[i]) ? "selected" : "" %>>쇠고기</option>
							    <option value="쇠고기(한우)" <%= "쇠고기(한우)".equals(arrValue[i]) ? "selected" : "" %>>쇠고기(한우)</option>
							    <option value="쇠고기(젖소)" <%= "쇠고기(젖소)".equals(arrValue[i]) ? "selected" : "" %>>쇠고기(젖소)</option>
							    <option value="쇠고기(육우)" <%= "쇠고기(육우)".equals(arrValue[i]) ? "selected" : "" %>>쇠고기(육우)</option>
							    <option value="돼지고기" <%= "돼지고기".equals(arrValue[i]) ? "selected" : "" %>>돼지고기</option>
							    <option value="닭고기" <%= "닭고기".equals(arrValue[i]) ? "selected" : "" %>>닭고기</option>
							    <option value="오리고기" <%= "오리고기".equals(arrValue[i]) ? "selected" : "" %>>오리고기</option>
							</select>
	                    <% } else if ("등급".equals(key)) { %>
	                        <select id="grade">
	                            <!-- JavaScript에서 옵션이 동적으로 설정됨 -->
	                        </select>
	                    <% } else if ("부위명칭(대)".equals(key)) { %>
	                        <select id="meatPart">
	                            <!-- JavaScript에서 옵션이 동적으로 설정됨 -->
	                        </select>
	                    <% } else if ("부위명칭(소)".equals(key)) { %>
	                        <select id="subMeatPart">
	                            <!-- JavaScript에서 옵션이 동적으로 설정됨 -->
	                        </select>
	                    <% } else if ("보관방법".equals(key)) { %>
	                        <select id="storage">
							    <option value="Not Found" <%= (arrValue[i] == null || "".equals(arrValue[i])) ? "selected" : "" %>>---</option>
							    <option value="냉장" <%= "냉장".equals(arrValue[i]) ? "selected" : "" %>>냉장</option>
							    <option value="냉동" <%= "냉동".equals(arrValue[i]) ? "selected" : "" %>>냉동</option>
							</select>
	                    <% } else { %>
	                        <input type="text" id="input_<%= i %>" value="<%= arrValue[i] %>" />
	                    <% } %>
	
	                    <button type="button" onclick="resetInput(<%= i %>)">초기화</button>
	                </div>
	    <%
	            }
	        } else {
	    %>
	            <p>데이터가 없습니다.</p>
	    <%
	        }
	    %>
	</div>



    <!-- 다른 동작으로 이동할 수 있는 버튼 -->
    <div class="button-container">
    	<button id="Btn_Save" class="Btn_Save">저장</button>
    	
    	<button id="Btn_MisInput" class="Btn_Breach">위반<br>(오기)</button>
    	<button id="Btn_falsehood" class="Btn_Breach">위반<br>(허위)</button>
    	<button id="Btn_Missing" class="Btn_Breach">위반<br>(누락)</button>
    	<button id="Btn_Etc" class="Btn_Breach">위반<br>(기타)</button>
    
        <button class="Btn_Return" onclick="location.href='/DoIT_Mobile/newSmart/ocr/OCR_MeatLabel'">↩︎</button>
    </div>

</body>
</html>
