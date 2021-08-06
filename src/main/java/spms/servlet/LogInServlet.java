package spms.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import spms.vo.Member;

@WebServlet("/auth/login")
public class LogInServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = request.getRequestDispatcher("/auth/LogInForm.jsp");
		//웹 브라우저로 부터 GET 요청이 들어오면 doGet()이 호출되어 LogInForm.jsp로 포워딩 한다.
		//(JSP에서 다시 서블릿으로 돌아올 필요가 없어서 인클루딩 대신 포워딩으로 처리했다.)
		rd.forward(request, response);
	}
	
	//사용자가 이메일과 암호를 입력한 후 POST 요청을 하면 doPost()가 호출된다.
	//doPost()에서는 데이터베이스로부터 회원 정보를 조회한다.
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection) sc.getAttribute("conn");
			stmt = conn.prepareStatement("SELECT MNAME,EMAIL FROM MEMBERS" + " WHERE EMAIL=? AND PWD=?");
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("password"));
			rs = stmt.executeQuery();
			if(rs.next()) {
				//만약 이메일과 암호가 일치하는 회원을 찾는다면 값 객체 Member에 회원 정보를 담는다.
				Member member = new Member().setEmail(rs.getString("EMAIL")).setName(rs.getString("MNAME"));
				//Member 객체를 HttpSession에 보관한다.
				HttpSession session = request.getSession();
				session.setAttribute("member", member);
				//로그인 성공이면 /member/list로 리다이렉트한다.
				response.sendRedirect("../member/list");
			} else {
				//로그인에 실패한다면 /auth/LogInFail.jsp로 포워딩한다.
				RequestDispatcher rd = request.getRequestDispatcher("/auth/LogInFail.jsp");
				rd.forward(request, response);
			}
		} catch(Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (rs != null) rs.close();} catch (Exception e) {}
			try {if (rs != null) rs.close();} catch (Exception e) {}
		}
	}

}
