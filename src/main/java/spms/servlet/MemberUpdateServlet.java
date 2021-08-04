package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//컨텍스트 초기화 매개변수의 값을 얻기 위해 객체가 필요하다.
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			conn = DriverManager.getConnection(sc.getInitParameter("url"), sc.getInitParameter("username"),sc.getInitParameter("password"));

			/*
			//초기화 매개변수를 이용하여 JDBC 드라이버 로딩
			//(Class.forName()을 사용하여 JDBC 드라이버 클래스, 즉 java.sql.Driver를 구현한 클래스이다.)
			Class.forName(this.getInitParameter("driver"));
			//데이터베이스에 연결할 때도 초기화 매개변수 이용한다.
			conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("username"),this.getInitParameter("password"));
			*/
			stmt = conn.createStatement();
			//요청 매개변수로 넘어온 회원 번호를 가지고 회원 정보를 질의한다.
			rs = stmt.executeQuery("select MNO, EMAIL,MNAME,CRE_DATE from join_db.MEMBERS" + " where MNO=" + request.getParameter("no"));
			//한명의 정보만 가져오기 때문에 next()를 한번만 호출한다.
			rs.next();
			
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>회원정보</title></head>");
			out.println("<body><h1>회원정보</h1>");
			out.println("<form action='update' method='post'>");
			//회원번호가 PRIMARY KEY이기 때문에 입력상자를 readonly 속성을 추가하여 읽기전용으로 설정 
			out.println("번호: <input type='text' name='no' value='" + request.getParameter("no") + "'readonly><br>");
			out.println("이름: *<input type='text' name='name'" + " value='" + rs.getString("MNAME") + "'><br>");
			out.println("이메일: <input type='text' name='email'" + " value='" + rs.getString("EMAIL") + "'><br>");
			out.println("가입일: " + rs.getDate("CRE_DATE") + "<br>");
			out.println("<input type='submit' value='저장'>");
			out.println("<input type='button' value='취소'" + " onclick='location.href=\"list\"'>");
			out.println("<input type='button' value='삭제'" + " onclick='location.href=\"delete?no=" + request.getParameter("no") + "\";'>");
			out.println("</form>");
			out.println("</body></html>");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try { if (rs != null ) rs.close();} catch(Exception e) {}
			try { if (stmt != null ) stmt.close();} catch(Exception e) {}
			try { if (conn != null ) conn.close();} catch(Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//doGet()메서드처럼 초기화 매개변수 이용하여 JDBC 드라이버 로딩
			Class.forName(this.getInitParameter("driver"));
			//데이터베이스 연결할 때 서블릿 초기화 매개변수 이용
			conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("username"), this.getInitParameter("password"));
			////회원정보 변경하기 위해 UPDATE문 사용 
			stmt = conn.prepareStatement("UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()" + " WHERE MNO=?");
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			stmt.executeUpdate();
			response.sendRedirect("list");
		} catch(Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
