package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MemberDao;
import spms.vo.Member;

//애노테이션 이용하여 서블릿의 배치정보 설정
//@WebServlet("/member/list")
//서블릿을 만들기위해 GenericServlet 클래스를 상속받고 service() 메소드를 구현한다.
public class MemberListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//HTML을 출력하는 모든 코드를 제거한다.
	//회원목록 화면을 생성하고 출력하는 것은 MemberList.jsp가 담당할 것이다.
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try {
			//MemberDao를 사용하기 전에 셋터를 먼저 호출하여 ServletContext에서 꺼낸 DB 커넥션 객체를 주입하였다.
			ServletContext sc = this.getServletContext();
			Connection conn = (Connection) sc.getAttribute("conn");
			
			MemberDao memberDao = new MemberDao();
			memberDao.setConnection(conn);
			
			request.setAttribute("members", memberDao.selectList());
			
			response.setContentType("text/html; charset=UTF-8");
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberList.jsp");
			rd.include(request, response);
			
		} catch(Exception e) {
			//throw new ServletException(e);
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} 
	}
	
}	
//이제 데이터베이스와 관련된 코드는 모두 MemberDao로 이관되었다.
	
