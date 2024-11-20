package kr.go.mtrace.newSmart.ocr;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/newSmart/ocr/StoreList")
public class Servlet_JSP_StoreList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("----- [[Servlet(doGet): StoreList]] -----");
    	
        Database db = new Database();
        String searchValue = req.getParameter("searchValue"); // 필요하면 파라미터 받기
        System.out.println("searchValue: " + searchValue);

        try {
            ResultSet rs = db.executeQuery(db.connect(), searchValue);
            List<Map<String, String>> storeList = new ArrayList<>();

            while (rs.next()) {
                Map<String, String> store = new HashMap<>();
                store.put("storeName", rs.getString("FIRST_NAME"));
                store.put("businessNumber", rs.getString("EMPLOYEE_ID"));
                store.put("address", rs.getString("EMAIL"));
                store.put("name", rs.getString("LAST_NAME"));
                store.put("storeType", rs.getString("JOB_ID"));
                storeList.add(store);
                //System.out.println("EMPLOYEE_ID: " + rs.getString("EMPLOYEE_ID"));
            }

            req.setAttribute("storeList", storeList); // 데이터를 JSP에 전달

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection(db.connect());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/mtrace/newSmart/ocr/StoreList.jsp").forward(req, resp);
    }
}
