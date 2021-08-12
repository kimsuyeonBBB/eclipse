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

import spms.bind.DataBinding;
import spms.bind.ServletRequestDataBinder;
import spms.context.ApplicationContext;
import spms.controls.Controller;
import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.listener.ContextLoaderListener;
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
			//이전에는 페이지 컨트롤러가 ServletContext에 저장되었기 때문에 객체를 준비해야 했지만, ApplicationContext를 도입하면서 필요가 없어졌다.
			//대신 ContextLoaderListener의 getApplicationContext()를 호출하여 ApplicationContext 객체를 꺼낸다.
			ApplicationContext ctx = ContextLoaderListener.getApplicationContext();
			
			HashMap<String,Object> model = new HashMap<String,Object>();
			//MemberListController는 회원목록을 가져오기 위해서 MemberDao 객체가 필요하다.
			//그래서 ServletContext 보관소에 저장된 MemberDao 객체를 꺼내서 Map 객체에 담는다.
			model.put("session", request.getSession());
			
			//ApplicationContext의 getBean()을 호출하여 페이지 컨트롤러를 찾는다.
			Controller pageController = (Controller)ctx.getBean(servletPath);
			
			if(pageController == null) {
				throw new Exception("요청한 서비스를 찾을 수 없습니다.");
			}
			
			if(pageController instanceof DataBinding) {
				prepareRequestData(request, model, (DataBinding)pageController);
			}
			
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
	private void prepareRequestData(HttpServletRequest request, HashMap<String, Object> model, DataBinding dataBinding) throws Exception{
		//페이지 컨트롤러에게 필요한 데이터가 무엇인지 물어본다.
		Object[] dataBinders = dataBinding.getDataBinders();
		String dataName = null;
		Class<?> dataType = null;
		Object dataObj = null;
		//데이터 이름과 데이터 타입을 꺼내기 쉽게 2씩 증가하며 반복문을 돌린다.
		for(int i = 0; i< dataBinders.length; i+=2) {
			dataName = (String)dataBinders[i];
			dataType = (Class<?>)dataBinders[i+1];
			dataObj = ServletRequestDataBinder.bind(request,dataType,dataName);
			model.put(dataName, dataObj);
		}
	}
}
