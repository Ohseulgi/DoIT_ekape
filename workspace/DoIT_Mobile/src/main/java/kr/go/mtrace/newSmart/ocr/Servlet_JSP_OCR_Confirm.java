package kr.go.mtrace.newSmart.ocr;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/newSmart/ocr/OCR_Confirm")
public class Servlet_JSP_OCR_Confirm extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.getRequestDispatcher("/WEB-INF/jsp/mtrace/newSmart/ocr/OCR_Confirm.jsp").forward(req,  resp);
    }
}
