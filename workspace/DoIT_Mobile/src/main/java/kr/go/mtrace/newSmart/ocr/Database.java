package kr.go.mtrace.newSmart.ocr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

	
    public static void main(String[] args) {
        // 테스트 데이터
        String testInput1 = "103333450"; // 숫자인 경우
        String testInput2 = "John"; // 문자인 경우
        
        Connection conn = connect();

        // 숫자 기준으로 EMPLOYEE_ID 조회
        try (ResultSet rs = executeQuery(conn, testInput1)) {
            int rowCount = getRowCount(rs); // 결과 행 수
            System.out.println("Number of rows: " + rowCount); // 결과 출력
            //printResults(rs); // 결과 출력
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        closeConnection(conn);
    }

    // DB Connect
    public static Connection connect() {
        String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
        String username = "hr";
        String password = "hr";
        Connection conn = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, username, password);
            //System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    // 문자열이 모두 숫자면 true
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false; // null 또는 빈 문자열은 false 반환
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false; // 숫자가 아닌 문자가 하나라도 있으면 false 반환
            }
        }
        return true; // 모든 문자가 숫자일 경우 true 반환
    }

    // 주어진 문자열을 받아서 EMPLOYEE_ID 또는 FIRST_NAME을 포함하는 결과 반환
    public static ResultSet executeQuery(Connection conn, String input) throws SQLException {
        // 쿼리 문자열 초기화
        String query = "";

        // 숫자이면 EMPLOYEE_ID로 조회, 아니면 FIRST_NAME으로 조회
        if (isNumeric(input)) {
            query = "SELECT * FROM HR.EMPLOYEES WHERE EMPLOYEE_ID = ?";
        } else {
            query = "SELECT * FROM HR.EMPLOYEES WHERE FIRST_NAME LIKE ?";
            input = "%" + input + "%"; // LIKE 검색을 위한 부분 문자열 추가
        }

        // PreparedStatement 생성
        //PreparedStatement stmt = conn.prepareStatement(query);
        // PreparedStatement 생성 (ResultSet.TYPE_SCROLL_INSENSITIVE로 설정하여 스크롤 가능하도록 함)
        PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        
        // PreparedStatement로 값 설정
        stmt.setString(1, input);  // 숫자일 경우 EMPLOYEE_ID로 검색 또는 문자일 경우 FIRST_NAME으로 검색

        // 쿼리 실행하여 결과 반환
        return stmt.executeQuery();
    }

    // 결과 행의 수를 반환하는 메서드
    public static int getRowCount(ResultSet rs) throws SQLException {
        int rowCount = 0;
        // ResultSet의 처음으로 이동
        while (rs.next()) {
            rowCount++; // 행 수 증가
        }
        rs.beforeFirst();
        return rowCount;
    }

    // ResultSet 결과 출력
    public static void printResults(ResultSet rs) {
        try {
            // 결과가 있을 때
            while (rs != null && rs.next()) {
                // 결과 출력
                System.out.println("EMPLOYEE_ID: " + rs.getInt("EMPLOYEE_ID"));
                System.out.println("FIRST_NAME: " + rs.getString("FIRST_NAME"));
                System.out.println("LAST_NAME: " + rs.getString("LAST_NAME"));
                System.out.println("EMAIL: " + rs.getString("EMAIL"));
                System.out.println("PHONE_NUMBER: " + rs.getString("PHONE_NUMBER"));
                System.out.println("HIRE_DATE: " + rs.getDate("HIRE_DATE"));
                System.out.println("JOB_ID: " + rs.getString("JOB_ID"));
                System.out.println("SALARY: " + rs.getDouble("SALARY"));
                System.out.println("COMMISSION_PCT: " + rs.getDouble("COMMISSION_PCT"));
                System.out.println("MANAGER_ID: " + rs.getInt("MANAGER_ID"));
                System.out.println("DEPARTMENT_ID: " + rs.getInt("DEPARTMENT_ID"));
                System.out.println("---------------------------------");
            }
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Connection 닫기
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); // Connection 닫기
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
