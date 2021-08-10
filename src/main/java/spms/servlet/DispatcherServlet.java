package spms.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			//서블릿 경로에 따라 조건문을 사용하여 적절한 페이지 컨트롤러를 인클루딩 한다.
			String pageControllerPath = null;
			
			if("/member/list.do".equals(servletPath)) {
				pageControllerPath = "/member/list";
			} else if ("/member/add.do".equals(servletPath)) {
				pageControllerPath = "/member/add";
				if(request.getParameter("email") != null) {
					//사용자가 입력한 데이터를 페이지 컨트롤러에게 전달하기 위해 요청 매개변수의 값을 꺼내서 VO객체에 담고, 'member'라는 키로 ServletRequest에 보관하였다.
					request.setAttribute("member", new Member()
							.setEmail(request.getParameter("email"))
							.setPassword(request.getParameter("password"))
							.setName(request.getParameter("name")));
				}
			} else if("/member/update.do".equals(servletPath)) {
				pageControllerPath = "/member/update";
				if(request.getParameter("email") != null) {
					request.setAttribute("member", new Member()
							.setNo(Integer.parseInt(request.getParameter("no")))
							.setEmail(request.getParameter("email"))
							.setName(request.getParameter("name")));
				}
			} else if("/member/delete.do".equals(servletPath)) {
				pageControllerPath = "/member/delete";
			} else if("/auth/login.do".equals(servletPath)) {
				pageControllerPath = "/auth/login";
			} else if("/auth/logout.do".equals(servletPath)) {
				pageControllerPath = "/auth/logout";
			}
			RequestDispatcher rd = request.getRequestDispatcher(pageControllerPath);
			rd.include(request, response);
			//페이지 컨트롤러의 실행이 끝나면 화면 출력을 위해 ServletRequest에 보관된 뷰 URL로 실행을 위임한다.
			//단 뷰 URL이 "redirect:"로 시작한다면 인클루딩 하는 대신에 sendRedirect()를 호출한다.
			String viewUrl = (String)request.getAttribute("viewUrl");
			if(viewUrl.startsWith("redirect:")) {
				response.sendRedirect(viewUrl.substring(9));
				return;
			} else {
				rd = request.getRequestDispatcher(viewUrl);
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
