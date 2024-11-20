
package kr.go.mtrace.newSmart.ocr;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/newSmart/ocr/SearchStore")
@MultipartConfig
public class Servlet_JSP_SearchStore extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("----- [[Servlet(doGet): SearchStore]] -----");
		
		String jsonResponse = null;
		try{
			jsonResponse = req.getParameter("jsonResponse");
			System.out.println("***** jsonResponse: " + jsonResponse);
			if(jsonResponse == null){
				req.getRequestDispatcher("/WEB-INF/jsp/mtrace/newSmart/ocr/SearchStore.jsp").forward(req, resp);
			}else{
				System.out.println("jsonResponse: " + jsonResponse);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String jsonResponse = null;
		
		System.out.println("----- [[Servlet(doPost): SearchStore]] -----");
	    String searchValue = req.getParameter("searchValue");
	    String storeList = req.getParameter("StoreList");
	    System.out.println("searchValue: " + searchValue);
	    System.out.println("searchValue: " + storeList);
	    Database db = new Database();

	    try {
	        ResultSet rs = db.executeQuery(db.connect(), searchValue);
	        int rowCount = db.getRowCount(rs);
	        System.out.println("rowCount: " + rowCount);

	        if (rowCount == 1) {
	            rs.next();
	            System.out.println("storeName: " + rs.getString("FIRST_NAME"));
	            System.out.println("businessNumber: " + rs.getString("EMPLOYEE_ID"));
	            System.out.println("address: " + rs.getString("EMAIL"));
	            System.out.println("name: " + rs.getString("LAST_NAME"));
	            System.out.println("storeType: " + rs.getString("JOB_ID"));
	            jsonResponse = "{"
	                + "\"resultCount\": " + rowCount + ","
	                + "\"storeName\": \"" + rs.getString("FIRST_NAME") + "\","
	                + "\"businessNumber\": \"" + rs.getString("EMPLOYEE_ID") + "\","
	                + "\"address\": \"" + rs.getString("EMAIL") + "\","
	                + "\"name\": \"" + rs.getString("LAST_NAME") + "\","
	                + "\"storeType\": \"" + rs.getString("JOB_ID") + "\""	
	                + "}";
	            
	            // 세션에 값 저장
	            HttpSession session = req.getSession();
	            session.setAttribute("storeName", rs.getString("FIRST_NAME"));
	            session.setAttribute("businessNumber", rs.getString("EMPLOYEE_ID"));
	            session.setAttribute("address", rs.getString("EMAIL"));
	            session.setAttribute("name", rs.getString("LAST_NAME"));
	            session.setAttribute("storeType", rs.getString("JOB_ID"));
	            
	            
	            if(storeList == null){
	            	System.out.println("##### storeList == null");
	            	resp.setContentType("application/json");
		            resp.getWriter().write(jsonResponse);
	            }else{
	            	System.out.println("##### storeList != null");
	            	req.getRequestDispatcher("/WEB-INF/jsp/mtrace/newSmart/ocr/SearchStore.jsp").forward(req, resp);
	            }
	        } else if (rowCount == 0) {
	            resp.getWriter().write("{\"resultCount\": 0}");
	        } else {
	            // 결과가 2개 이상일 때
	            resp.setContentType("application/json");
	            String redirectUrl = "/DoIT_Mobile/newSmart/ocr/StoreList?searchValue=" + searchValue;
	            resp.getWriter().write("{\"resultCount\": " + rowCount + ", \"redirect\": \"" + redirectUrl + "\"}");
	        }


	    } catch (SQLException e) {
	        e.printStackTrace();
	        resp.getWriter().write("{\"resultCount\": 0}");
	        //req.setAttribute("jsonResponse", "{\"resultCount\": 0}");
	    } finally {
	        db.closeConnection(db.connect());
	    }
	}

}
