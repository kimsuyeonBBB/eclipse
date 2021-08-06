package spms.servlet;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AppInitServlet extends HttpServlet {
	//init()�� ���� ��ü�� ������ �� �� �ѹ� ȣ��Ǳ� ������ ���� �ڿ��� �غ��ϴ� �ڵ尡 ���̱⿡ ������ ����̴�.
	@Override
	public void init(ServletConfig config) throws ServletException{
		//�� ���ø����̼��� ���۵� �� �� �޼��尡 ȣ��ȴٴ� ���� �˷��ִ� �ܼ�â ��¹�
		System.out.println("AppInitServlet �غ�...");
		////�������̵� �ϱ� ���� �޼��带 ȣ��
		super.init(config);
		
		//�����ͺ��̽� Ŀ�ؼ��� �غ��ϴ� ��ɾ�
		try {
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			Connection conn = DriverManager.getConnection(sc.getInitParameter("url"),sc.getInitParameter("username"),sc.getInitParameter("password"));
			sc.setAttribute("conn", conn);
		} catch(Throwable e) {
			throw new ServletException(e);
		}
	}
	
	//������ ��ε� �ɶ� ȣ��ȴ�.
	@Override 
	public void destroy() {
		System.out.println("AppInitServlet ������...");
		super.destroy();
		Connection conn = (Connection)this.getServletContext().getAttribute("conn");
		try {
			if(conn != null && conn.isClosed() == false) {
				conn.close();
			}
		} catch(Exception e) {}
	}
	
}
