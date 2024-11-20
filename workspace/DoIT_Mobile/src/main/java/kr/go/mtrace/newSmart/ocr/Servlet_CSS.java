package kr.go.mtrace.newSmart.ocr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/newSmart/ocr/css/*")
public class Servlet_CSS extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();        // 요청된 파일 경로 (예: /CSS_MeatLabel.css)
        String basePath = getServletContext().getRealPath("/WEB-INF/jsp/mtrace/newSmart/ocr/css"); // 서블릿 경로
        resp.setContentType("text/css; charset=UTF-8");    //// 응답 타입 설정
        
        
        // /CSS_MeatLabel
        if(path != null){
        //if ("/CSS_MeatLabel.css".equals(path)) {
            String filePath = basePath + path;
            loadCSSContent(resp, filePath);
        } else {
            // 지정되지 않은 파일 요청에 대한 응답
            resp.getWriter().write("/* CSS 파일이 없습니다 */");
        }
    }


    
    private void loadCSSContent(HttpServletResponse resp, String filePath) throws IOException {
    	try{
    		FileInputStream fis = new FileInputStream(filePath);
    		OutputStream os = resp.getOutputStream();
    		
    		byte[] buffer = new byte[4096];
    		int length;
    		
    		while((length = fis.read(buffer)) > 0){
    			os.write(buffer, 0, length);
    		}
    		
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    
    /*
    private void loadCSSContent(HttpServletResponse resp, String filePath) throws IOException {
        try {
            // 파일 내용을 읽어 반환
            String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            resp.getWriter().write(content);
        } catch (IOException e) {
            // 파일을 읽는 도중 에러가 발생할 경우
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(" 파일을 읽을 수 없습니다: " + filePath + " ");
        }
    }
    */
}
