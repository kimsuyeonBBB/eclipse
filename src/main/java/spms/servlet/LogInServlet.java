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

import spms.dao.MemberDao;
import spms.vo.Member;

@WebServlet("/auth/login")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
		try {
			ServletContext sc = this.getServletContext();
			Connection conn = (Connection) sc.getAttribute("conn");
			
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(conn);
			
			Member member = memberDao.exist(
					request.getParameter("email"),
					request.getParameter("password"));
			if(member != null) {
				HttpSession session = request.getSession();
				session.setAttribute("member", member);
				response.sendRedirect("../member/list");
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/auth/LogInFail.jsp");
				rd.forward(request, response);
			}
		} catch(Exception e) {
			throw new ServletException(e);
		} 
	}
}
