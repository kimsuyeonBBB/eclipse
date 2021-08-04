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
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/member/delete")
public class MemberDeleteServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		Statement stmt = null;
		try {
			ServletContext sc = this.getServletContext();
			//doGet()메서드처럼 초기화 매개변수 이용하여 JDBC 드라이버 로딩
			Class.forName(this.getInitParameter("driver"));
			//데이터베이스 연결할 때 서블릿 초기화 매개변수 이용
			conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("username"), this.getInitParameter("password"));
			////회원정보 변경하기 위해 UPDATE문 사용 
			stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM join_db.MEMBERS WHERE MNO=" + request.getParameter("no"));
			response.sendRedirect("list");
		} catch(Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
	
}
