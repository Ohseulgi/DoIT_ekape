package kr.go.mtrace.newSmart.ocr;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.synap.ocr.sdk.OCRResult;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;



import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OCR_RestAPI {
	
	public static String[] ocr_KVT(String imagePath) {
		// OCR Key, Value
		String[] arrKey = {"이력번호", "원산지", "식육의종류", "부위명칭", "등급", "판매가격_100g당", "도축장명", "포장일자", "유통기한", "보관방법"};
    	String[] arrValue = new String[10];
    	
    	// Rest API 설정
        //String baseUrl = "https://ailab.synap.co.kr/sdk/ocr";			// 축평원
        String baseUrl = "http://192.168.0.13:8888/api/MeatLabel";		// 개발
        
        //String apiKey = "SNOCR-fc20958163624c6898d479d33d319f19";		// 축평원
        String apiKey = "SNOCR-3a8143972dac4f5992f6b54b7e5f89ff";		// 개발
        String type = "upload";
    	
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            File imageFile = new File(imagePath);			// 업로드할 이미지 파일 설정
            HttpPost uploadFile = new HttpPost(baseUrl);	// HTTP POST 요청 설정

            // MultipartEntityBuilder를 사용하여 multipart/form-data 엔티티 생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("api_key", new StringBody(apiKey, StandardCharsets.UTF_8));
            builder.addPart("type", new StringBody(type, StandardCharsets.UTF_8));
            builder.addPart("image_file", new FileBody(imageFile));

            // HttpEntity 생성 및 요청에 설정
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            
            // POST 요청 실행
            try(CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // JSON 응답 처리
                    String jsonResponse = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    
                    // "result" 객체 가져오기
                    JSONObject result = jsonObject.getJSONObject("result");
                    
                    for(int i=0; i<arrKey.length; i++){
                    	try{
                    		//JSONObject item = result.getJSONObject(arrKey[i]);
                    		if(result.getJSONObject(arrKey[i]).has("text")){
                    			arrValue[i] = result.getJSONObject(arrKey[i]).getString("text");
                    		}
                    	}catch(Exception e){
                    		
                    	}finally{
                    		System.out.println(arrKey[i] + ": " + arrValue[i]);
                    	}
                    }

                }
            }            
        }catch (IOException e) {
            e.printStackTrace();
        }
    	
		return arrValue;
	}
	
	
	
	public static String ocr_Basic(String imagePath) {

    	String strOCR = "";
    	// Rest API 설정
        String baseUrl = "https://ailab.synap.co.kr/sdk/ocr";			// 축평원
        //String baseUrl = "http://192.168.0.13:8888/sdk/ocr";		// 개발
        
        //String apiKey = "SNOCR-fc20958163624c6898d479d33d319f19";		// 축평원
        String apiKey = "SNOCR-3a8143972dac4f5992f6b54b7e5f89ff";		// 개발
        String type = "upload";
        String boxes_type = "line";		// raw/block/line/all
        boolean textout = true;
        
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            File imageFile = new File(imagePath);			// 업로드할 이미지 파일 설정
            HttpPost uploadFile = new HttpPost(baseUrl);	// HTTP POST 요청 설정

            // MultipartEntityBuilder를 사용하여 multipart/form-data 엔티티 생성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("api_key", new StringBody(apiKey, StandardCharsets.UTF_8));
            builder.addPart("type", new StringBody(type, StandardCharsets.UTF_8));
            builder.addPart("image", new FileBody(imageFile));
            
            builder.addPart("boxes_type", new StringBody(boxes_type, StandardCharsets.UTF_8));
            builder.addPart("textout", new StringBody(String.valueOf(textout), StandardCharsets.UTF_8));

            // HttpEntity 생성 및 요청에 설정
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            
            // POST 요청 실행
            try(CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // JSON 응답 처리
                    String jsonResponse = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    
                    // "result" 객체 가져오기
                    JSONObject result = jsonObject.getJSONObject("result");
                    
                    //System.out.println("----- OCR Basic -----");
                    //System.out.println(result.toString());
                    //System.out.println(result.getString("full_text"));
                    
                    strOCR = result.getString("full_text");
                }
            }            
        }catch (IOException e) {
            e.printStackTrace();
        }
        return strOCR;
	}
	
	/*
	public static void main(String[] args){
		String dir = "C:\\Users\\seulgi\\Desktop\\샘플\\새 폴더";
		String path = dir + "\\cow04.jpg";
		
		System.out.println(path);
		System.out.println(ocr_Basic(path));
	}
	*/
	
}
