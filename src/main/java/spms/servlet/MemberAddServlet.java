package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MySqlMemberDao;
import spms.vo.Member;

//프런트 컨트롤러 적용

//HttpServlet 클래스는 GenericServlet 클래스의 하위 클래스이다.
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	
	//HttpServlet을 상속받을 때 service() 메서드를 직접 구현하기보다는 클라이언트의 요청 방식에 따라 doXXX() 메서드를 오버라이딩 한다.
	//여기서는 '신규회원' 링크를 클릭할 때 GET 요청이 발생하기 때문에 doGet() 메서드를 오버라이딩 하였다.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//MemberForm.jsp의 URL을 ServletRequest에 저장한다.
		request.setAttribute("viewUrl", "/member/MemberForm.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			ServletContext sc = this.getServletContext();
			MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
			
			//프런트 컨트롤러가 준비해놓은 Member 객체를 ServletRequest 보관소에서 꺼내도록 코드 작성
			Member member = (Member)request.getAttribute("member");
			memberDao.insert(member);
			
			//리다이렉트 해야 하는 경우에 반드시 URL 앞부분에 "redirect:" 문자열을 붙여야 한다.
			request.setAttribute("viewUrl", "redirect:list.do");
			
		} catch(Exception e) {
			throw new ServletException(e);
		} 
	}
}
