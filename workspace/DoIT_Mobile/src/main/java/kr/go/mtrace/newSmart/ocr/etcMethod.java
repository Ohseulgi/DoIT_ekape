package kr.go.mtrace.newSmart.ocr;

import java.util.*;


public class etcMethod {

    private static final Map<String, String[]> beefCutsMap = new HashMap<>();
    static {
    	beefCutsMap.put("안심", new String[]{"안심살"});
        beefCutsMap.put("등심", new String[]{"윗등심살", "꽃등심살", "아래등심살", "살치살"});
        beefCutsMap.put("목심", new String[]{"목심살"});
        beefCutsMap.put("채끝", new String[]{"채끝살"});
        beefCutsMap.put("앞다리", new String[]{"꾸리살", "부채살", "앞다리살", "갈비덧살", "부채덮개살"});
        beefCutsMap.put("우둔", new String[]{"우둔살", "홍두깨살"});
        beefCutsMap.put("설도", new String[]{"보섭살", "설깃머리살", "도가니살", "삼각살"});
        beefCutsMap.put("양지", new String[]{"양지머리", "차돌박이", "업진살", "업진안살", "치마양지", "치마살", "앞치마살"});
        beefCutsMap.put("사태", new String[]{"앞사태", "뒷사태", "뭉치사태", "아롱사태", "상박살"});
        beefCutsMap.put("갈비", new String[]{"본갈비", "꽃갈비", "참갈비", "갈비살", "마구리", "토시살", "안창살", "제비추리"});
    }
    
    private static final Map<String, String[]> porkCutsMap = new HashMap<>();
    static {
    	porkCutsMap.put("안심", new String[]{"안심살"});
    	porkCutsMap.put("등심", new String[]{"등심살", "알등심살", "등심덧살"});
    	porkCutsMap.put("목심", new String[]{"목심살"});
    	porkCutsMap.put("앞다리", new String[]{"앞다리살", "앞사태살", "항정살", "꾸리살", "부채살", "주걱살"});
        porkCutsMap.put("뒷다리", new String[]{"볼기살", "설깃살", "도가니살", "홍두깨살", "보섭살", "뒷사태살"});
        porkCutsMap.put("삼겹살", new String[]{"삼겹살", "갈매기살", "등갈비", "토시살", "오돌삼겹"});
        porkCutsMap.put("갈비", new String[]{"갈비", "갈비살", "마구리"});
    }

    // 부위명칭(대), 부위명칭(소)
    public static String[] findMeatPart(String strMeatType, String strOCR) {
    	Map<String, String[]> primaryCutsMap = new HashMap<>();
    	
    	if(strMeatType.contains("쇠고기") || strMeatType.contains("한우") || strMeatType.contains("육우") || strMeatType.contains("젖소")){
    		primaryCutsMap = beefCutsMap;
    		//System.out.println("식육의종류: 쇠고기");
    	}else if(strMeatType.contains("돼지")){
    		primaryCutsMap = porkCutsMap;
    		//System.out.println("식육의종류: 돼지고기");
    	}else{
    		return new String[]{"", ""};
    	}
        // 부위명칭(소)에서 먼저 탐색
        for (Map.Entry<String, String[]> entry : primaryCutsMap.entrySet()) {
            for (String subPart : entry.getValue()) {
                if (strOCR.contains(subPart)) {
                	System.out.println("부위명칭(대): " + entry.getKey() + ", 부위명칭(소): " + subPart);
                    return new String[]{entry.getKey(), subPart}; // 부위명칭(대), 부위명칭(소) 반환
                }
            }
        }

        // 부위명칭(소)에서 찾을 수 없으면 부위명칭(대)에서 탐색
        for (String primaryPart : primaryCutsMap.keySet()) {
            if (strOCR.contains(primaryPart)) {
            	System.out.println("부위명칭(대): " + primaryPart + ", 부위명칭(소): Not Found");
                return new String[]{primaryPart, "Not Found"}; // 부위명칭(대), "Not Found" 반환
            }
        }

        return new String[]{"Not Found", "Not Found"}; // 둘 다 찾을 수 없는 경우
    }
    
    // 원산지
    public static String defineCountryOfOrigin(String text, String strOCR) {
    	String strCountryOfOrigin = "";
    	if(text == null) text = "";
    	if(strOCR == null) strOCR = "";
    	
    	if(text.contains("국내산") || strOCR.contains("국내산")){
    		strCountryOfOrigin = "국내산";
    	}else if(text.contains("수입") || text.contains("외국산") || text.contains("해외") || strOCR.contains("수입") || strOCR.contains("외국산") || strOCR.contains("해외")){
    		strCountryOfOrigin = "외국산";
    	}else{
    		strCountryOfOrigin = "Not Found";
    	}
    	return strCountryOfOrigin;	
    }
    
    // 식육의종류
    public static String defineMeatType(String text, String strOCR) {
    	String strMeatType = "";
    	
    	if(text == null) text = "";
    	if(strOCR == null) strOCR = "";
    	
    	if(text.contains("닭") || strOCR.contains("닭")){
    		strMeatType = "닭고기";
    	}else if(text.contains("오리") || strOCR.contains("오리")){
    		strMeatType = "오리고기";
    	}else if(text.contains("돼지") || strOCR.contains("돼지") || text.contains("돈육") || strOCR.contains("돈육") || text.contains("한돈") || strOCR.contains("한돈")){
    		strMeatType = "돼지고기";
    	}else if(text.contains("한우") || strOCR.contains("한우")){
    		strMeatType = "쇠고기(한우)";
    	}else if(text.contains("젖소") || strOCR.contains("젖소")){
    		strMeatType = "쇠고기(젖소)";
    	}else if(text.contains("육우") || strOCR.contains("육우")){
    		strMeatType = "쇠고기(육우)";
    	}else if(text.contains("소고기") || strOCR.contains("소고기") || text.contains("쇠고기") || strOCR.contains("쇠고기")){
    		strMeatType = "쇠고기";
    	}else{
    		strMeatType = "Not Found";
    	}
    	return strMeatType;
    }
    
    // 보관방법 StorageMethod
    public static String defineStorageMethod(String text, String strOCR) {
    	String strStorageMethod = "";
    	if(text == null) text = "";
    	if(strOCR == null) strOCR = "";
    	
    	if(text.contains("냉장") || strOCR.contains("냉장")){
    		strStorageMethod = "냉장";
    	}else if(text.contains("냉동") || text.contains("냉동")){
    		strStorageMethod = "냉동";
    	}else{
    		strStorageMethod = "Not Found";
    	}
    	return strStorageMethod;	
    }
    
    /*
    public static void main(String[] args) {
    	String strOCR = "한돈 생삼겹살\n"
                + "비닐류\n"
                + "OTHER\n"
                + "원산지\n"
                + "국내산\n"
                + "이력번호\n"
                + "(묶음번호)\n"
                + "002127724207\n"
                + "등급: 1++\n"
                + "1+\n"
                + "1\n"
                + "3\n"
                + "등외\n"
                + "*보관방법:냉장육 (-3C-5C냉장보관 )\n"
                + "*포장재질\n";
        String[] result = findMeatPart("돼지", strOCR);
        System.out.println("부위명칭(대): " + result[0] + ", 부위명칭(소): " + result[1]);
    }
    */
    
}
