package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.vo.Member;

@SuppressWarnings("serial")
//@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//컨텍스트 초기화 매개변수의 값을 얻기 위해 객체가 필요하다.
			ServletContext sc = this.getServletContext();
			conn = (Connection) sc.getAttribute("conn");
			stmt = conn.createStatement();
			//요청 매개변수로 넘어온 회원 번호를 가지고 회원 정보를 질의한다.
			rs = stmt.executeQuery("select MNO, EMAIL,MNAME,CRE_DATE from join_db.MEMBERS" + " where MNO=" + request.getParameter("no"));
			//한명의 정보만 가져오기 때문에 next()를 한번만 호출한다.
			if(rs.next()) {
				request.setAttribute("member", 
						new Member()
						.setNo(rs.getInt("MNO"))
						.setEmail(rs.getString("EMAIL"))
						.setName(rs.getString("MNAME"))
						.setCreatedDate(rs.getDate("CRE_DATE")));
			} else {
				throw new Exception("");
			}
			
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberUpdateForm.jsp");
			rd.forward(request, response);
			
		} catch (Exception e) {
			//throw new ServletException(e);
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try { if (rs != null ) rs.close();} catch(Exception e) {}
			try { if (stmt != null ) stmt.close();} catch(Exception e) {}
			//try { if (conn != null ) conn.close();} catch(Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection) sc.getAttribute("conn");
			////회원정보 변경하기 위해 UPDATE문 사용 
			stmt = conn.prepareStatement("UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()" + " WHERE MNO=?");
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			stmt.executeUpdate();
			response.sendRedirect("list");
		} catch(Exception e) {
			//throw new ServletException(e);
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			//try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
