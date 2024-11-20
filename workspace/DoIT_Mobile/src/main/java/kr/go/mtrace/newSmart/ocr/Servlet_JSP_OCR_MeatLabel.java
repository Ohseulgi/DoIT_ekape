package kr.go.mtrace.newSmart.ocr;

import com.synap.ocr.sdk.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/newSmart/ocr/OCR_MeatLabel")
@MultipartConfig
public class Servlet_JSP_OCR_MeatLabel extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.getRequestDispatcher("/WEB-INF/jsp/mtrace/newSmart/ocr/OCR_MeatLabel.jsp").forward(req,  resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	resp.setHeader("Access-Control-Allow-Origin", "*");
    	System.out.println("----- [[doPost]] -----");
    	
    	Part imagePart = req.getPart("imageData");
    	System.out.println("imagePart.getName(): " + imagePart.getName());
    	System.out.println("imagePart.getSize(): " + imagePart.getSize());

        if (imagePart != null) {
        	String[] arrStore = {req.getParameter("storeName"), 
        							req.getParameter("businessNumber"), 
        							req.getParameter("address"), 
        							req.getParameter("name"), 
        							req.getParameter("storeType")
        						};
        	
            //System.out.println("storeName: " + req.getParameter("storeName"));
            //System.out.println("businessNumber: " + req.getParameter("businessNumber"));
            //System.out.println("address: " + req.getParameter("address"));
            //System.out.println("name: " + req.getParameter("name"));
            //System.out.println("storeType: " + req.getParameter("storeType"));
        	
            // Red-Box 비율
            double redBoxWidthRatio = Double.parseDouble(req.getParameter("widthRatio")) / 100;
            double redBoxHeightRatio = Double.parseDouble(req.getParameter("heightRatio")) / 100;
            
            // 현재 시간과 장치 정보
            String currentTime = req.getParameter("currentTime").replaceAll("[^가-힣a-zA-Z0-9]", "");
            String deviceInfo = req.getParameter("deviceInfo").replaceAll("[^가-힣a-zA-Z0-9]", "");
            
            // 이미지를 저장할 파일 경로 정의
            String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIR;
            String tempFilePath = uploadPath + File.separator + currentTime + deviceInfo + "_temp_photo.png";
            String filePath = uploadPath + File.separator + currentTime + deviceInfo + "_photo.png";
            
            System.out.println("tempFilePath: " + tempFilePath);
            System.out.println("filePath: " + filePath);
            
            // 업로드 디렉토리가 존재하지 않으면 생성
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); 
            }

            
            // 이미지를 임시 파일로 저장
            try (InputStream ins = imagePart.getInputStream()) {
                File tempFile = new File(tempFilePath);
                try(OutputStream os = new FileOutputStream(tempFile)){
                	byte[] buffer = new byte[4096];
                	int byteRead;
                	while((byteRead = ins.read(buffer)) != -1){
                		os.write(buffer, 0, byteRead);
                	}
                }
            }catch(IOException e){
            	e.printStackTrace();
            }

            // 이미지 자르기
            BufferedImage originalImage = ImageIO.read(new File(tempFilePath));

            // 이미지 크기 확인
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();            
        
            // 잘라낼 영역의 너비와 높이 계산
            int redBoxWidth = (int) (originalWidth * redBoxWidthRatio);
            int redBoxHeight = (int) (originalHeight * redBoxHeightRatio);            

            // 중앙 기준으로 잘라낼 좌표 계산
            int redBoxX = (originalWidth - redBoxWidth) / 2;
            int redBoxY = (originalHeight - redBoxHeight) / 2; 
        
            // 이미지 자르기
            BufferedImage croppedImage = originalImage.getSubimage(redBoxX, redBoxY, redBoxWidth, redBoxHeight);

            // 저장할 파일 경로
            ImageIO.write(croppedImage, "png", new File(filePath));

            // 임시 파일 삭제
            new File(tempFilePath).delete();

            // 세션에 변수 저장
            HttpSession session = req.getSession();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(croppedImage, "png", baos);
            session.setAttribute("croppedImage", baos.toByteArray());
            
            
            // confirm.jsp로 리다이렉트
            String[] arrKey = {"이력번호", "원산지", "식육의종류", "부위명칭(대)", "부위명칭(소)", "등급", "판매가격", "도축장명", "포장일자", "유통기한", "보관방법"};
            String[] arrValue = new String[10];
            
            
            arrValue = OCR_RestAPI.ocr_KVT(filePath);
            String strOCR = OCR_RestAPI.ocr_Basic(filePath);

            // ------------------------------------------------------------------------------------------------------------
            String[] newArray = new String[arrValue.length + 1];
            System.arraycopy(arrValue, 0, newArray, 0, arrValue.length);
            arrValue = newArray;
            
            arrValue[10] = arrValue[9];
            arrValue[9] = arrValue[8];
            arrValue[8] = arrValue[7];
            arrValue[7] = arrValue[6];
            arrValue[6] = arrValue[5];
            arrValue[5] = arrValue[4];
            //arrValue[12] = new SimpleDateFormat("yyyyMMdd").format(new Date());
            
            // 원산지
            arrValue[1] = etcMethod.defineCountryOfOrigin(arrValue[1], strOCR);
            
            // 식육의종류
            arrValue[2] = etcMethod.defineMeatType(arrValue[2], strOCR);
            
            // 부위명칭(대), 부위명칭(소)
            String[] arrMeatPart = etcMethod.findMeatPart(arrValue[2], strOCR);
            arrValue[3] = arrMeatPart[0];
            arrValue[4] = arrMeatPart[1];
            
            // 보관방법
            arrValue[10] = etcMethod.defineStorageMethod(arrValue[10], strOCR);            
            
            System.out.println("-------------------------------------------");
            System.out.println("-------------------------------------------");
            for(int i=0; i<arrValue.length; i++){
            	System.out.println(i+1 + "/" + arrValue.length + "\t" + arrKey[i] + ": " + arrValue[i]);
            }
            
            for(int i=0; i<arrStore.length; i++){
            	System.out.println(i+1 + "/" + arrStore.length + "\t" + arrStore[i]);
            }
            
            session.setAttribute("arrKey", arrKey);
            session.setAttribute("arrValue", arrValue);
            session.setAttribute("arrStore", arrStore);
           
        } else {
            resp.getWriter().println("이미지 데이터가 수신되지 않았습니다.");
            System.out.println("이미지 데이터가 수신되지 않았습니다.");	
        }
    }
}


