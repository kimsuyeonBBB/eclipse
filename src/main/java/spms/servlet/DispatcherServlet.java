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

//����Ʈ ��Ʈ�ѷ�

//����Ʈ ��Ʈ�ѷ��� ��ġ URL�� "*.do"�� �����Ѵ�.
//��, Ŭ���̾�Ʈ�� ��û �߿��� ���� ��� �̸��� .do�� ������ ���� DispatcherServlet�� ó���ϰڴٴ� �ǹ��̴�.
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("text/html; charset=UTF-8");
		//Ŭ���̾�Ʈ�� ��û�� ������ ��θ� �˱����� �ڵ�
		String servletPath = request.getServletPath();
		try {
			//�������� ������ ��Ʈ�ѷ��� ServletContext�� ����Ǿ��� ������ ��ü�� �غ��ؾ� ������, ApplicationContext�� �����ϸ鼭 �ʿ䰡 ��������.
			//��� ContextLoaderListener�� getApplicationContext()�� ȣ���Ͽ� ApplicationContext ��ü�� ������.
			ApplicationContext ctx = ContextLoaderListener.getApplicationContext();
			
			HashMap<String,Object> model = new HashMap<String,Object>();
			//MemberListController�� ȸ������� �������� ���ؼ� MemberDao ��ü�� �ʿ��ϴ�.
			//�׷��� ServletContext �����ҿ� ����� MemberDao ��ü�� ������ Map ��ü�� ��´�.
			model.put("session", request.getSession());
			
			//ApplicationContext�� getBean()�� ȣ���Ͽ� ������ ��Ʈ�ѷ��� ã�´�.
			Controller pageController = (Controller)ctx.getBean(servletPath);
			
			if(pageController == null) {
				throw new Exception("��û�� ���񽺸� ã�� �� �����ϴ�.");
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
		//������ ��Ʈ�ѷ����� �ʿ��� �����Ͱ� �������� �����.
		Object[] dataBinders = dataBinding.getDataBinders();
		String dataName = null;
		Class<?> dataType = null;
		Object dataObj = null;
		//������ �̸��� ������ Ÿ���� ������ ���� 2�� �����ϸ� �ݺ����� ������.
		for(int i = 0; i< dataBinders.length; i+=2) {
			dataName = (String)dataBinders[i];
			dataType = (Class<?>)dataBinders[i+1];
			dataObj = ServletRequestDataBinder.bind(request,dataType,dataName);
			model.put(dataName, dataObj);
		}
	}
}
