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
		//�� �������� ���� GET ��û�� ������ doGet()�� ȣ��Ǿ� LogInForm.jsp�� ������ �Ѵ�.
		//(JSP���� �ٽ� �������� ���ƿ� �ʿ䰡 ��� ��Ŭ��� ��� ���������� ó���ߴ�.)
		rd.forward(request, response);
	}
	
	//����ڰ� �̸��ϰ� ��ȣ�� �Է��� �� POST ��û�� �ϸ� doPost()�� ȣ��ȴ�.
	//doPost()������ �����ͺ��̽��κ��� ȸ�� ������ ��ȸ�Ѵ�.
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
				//���� �̸��ϰ� ��ȣ�� ��ġ�ϴ� ȸ���� ã�´ٸ� �� ��ü Member�� ȸ�� ������ ��´�.
				Member member = new Member().setEmail(rs.getString("EMAIL")).setName(rs.getString("MNAME"));
				//Member ��ü�� HttpSession�� �����Ѵ�.
				HttpSession session = request.getSession();
				session.setAttribute("member", member);
				//�α��� �����̸� /member/list�� �����̷�Ʈ�Ѵ�.
				response.sendRedirect("../member/list");
			} else {
				//�α��ο� �����Ѵٸ� /auth/LogInFail.jsp�� �������Ѵ�.
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
