package spms.servlet;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AppInitServlet extends HttpServlet {
	//init()은 서블릿 객체가 생성될 때 딱 한번 호출되기 때문에 공유 자원을 준비하는 코드가 놓이기에 최적의 장소이다.
	@Override
	public void init(ServletConfig config) throws ServletException{
		//웹 애플리케이션이 시작될 때 이 메서드가 호출된다는 것을 알려주는 콘솔창 출력문
		System.out.println("AppInitServlet 준비...");
		////오버라이딩 하기 전의 메서드를 호출
		super.init(config);
		
		//데이터베이스 커넥션을 준비하는 명령어
		try {
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			Connection conn = DriverManager.getConnection(sc.getInitParameter("url"),sc.getInitParameter("username"),sc.getInitParameter("password"));
			sc.setAttribute("conn", conn);
		} catch(Throwable e) {
			throw new ServletException(e);
		}
	}
	
	//서블릿이 언로드 될때 호출된다.
	@Override 
	public void destroy() {
		System.out.println("AppInitServlet 마무리...");
		super.destroy();
		Connection conn = (Connection)this.getServletContext().getAttribute("conn");
		try {
			if(conn != null && conn.isClosed() == false) {
				conn.close();
			}
		} catch(Exception e) {}
	}
	
}
