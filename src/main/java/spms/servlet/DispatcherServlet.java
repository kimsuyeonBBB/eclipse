package spms.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.controls.Controller;
import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.vo.Member;

//프런트 컨트롤러

//프런트 컨트롤러의 배치 URL을 "*.do"로 지정한다.
//즉, 클라이언트의 요청 중에서 서블릿 경로 이름이 .do로 끝나는 경우는 DispatcherServlet이 처리하겠다는 의미이다.
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html; charset=UTF-8");
		//클라이언트가 요청한 서블릿의 경로를 알기위한 코드
		String servletPath = request.getServletPath();
		try {
			//프런트 컨트롤러와 페이지 컨트롤러 사이에 데이터나 객체를 주고 받을 때 사용할 Map 객체를 준비한다.
			//즉 MemberListController가 사용할 객체를 준비하여 Map 객체에 담아서 전달해준다.
			ServletContext sc = this.getServletContext();
			
			HashMap<String,Object> model = new HashMap<String,Object>();
			//MemberListController는 회원목록을 가져오기 위해서 MemberDao 객체가 필요하다.
			//그래서 ServletContext 보관소에 저장된 MemberDao 객체를 꺼내서 Map 객체에 담는다.
			model.put("session", request.getSession());
			
			Controller pageController = (Controller)sc.getAttribute(servletPath);
			
			if("/member/add.do".equals(servletPath)) {
				if(request.getParameter("email") != null) {
					model.put("member", new Member()
							.setEmail(request.getParameter("email"))
							.setPassword(request.getParameter("password"))
							.setName(request.getParameter("name")));
				}
			} else if("/member/update.do".equals(servletPath)) {
				if(request.getParameter("email") != null) {
					model.put("member", new Member()
							.setNo(Integer.parseInt(request.getParameter("no")))
							.setEmail(request.getParameter("email"))
							.setName(request.getParameter("name")));
				} else {
					model.put("no", new Integer(request.getParameter("no")));
				}
			} else if("/member/delete.do".equals(servletPath)) {
				model.put("no", new Integer(request.getParameter("no")));
			} else if("/auth/login.do".equals(servletPath)) {
				if(request.getParameter("email") != null) {
					model.put("loginInfo", new Member()
							.setEmail(request.getParameter("email"))
							.setPassword(request.getParameter("password")));
				}
			} 
			
			//MemberListController가 일반 클래스이기 때문에 Controller 인터페이스에 정해진대로 execute() 메서드를 호출해야 한다.
			//execute()를 호출할 때 페이지 컨트롤러를 위해 준비한 Map 객체를 매개변수로 넘긴다.
			String viewUrl = pageController.execute(model);
			for(String key : model.keySet()) {
				request.setAttribute(key, model.get(key));
			}
			
			if(viewUrl.startsWith("redirect:")) {
				response.sendRedirect(viewUrl.substring(9));
				return;
			} else {
				RequestDispatcher rd = request.getRequestDispatcher(viewUrl);
				rd.include(request, response);
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
}
