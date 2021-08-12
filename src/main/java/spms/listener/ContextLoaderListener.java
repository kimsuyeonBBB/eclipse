package spms.listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import spms.context.ApplicationContext;
import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.dao.MySqlMemberDao;
import spms.util.DBConnectionPool;

public class ContextLoaderListener implements ServletContextListener{
	static ApplicationContext applicationContext;
	
	//ContextLoaderListener에서 만든 ApplicationContext 객체를 얻을 때 사용한다.
	//당장 프런트 컨트롤러에서 사용해야 하며 클래스 이름만으로 호출할 수 있게 static으로 선언한다.
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	//웹 애플리케이션이 시작될 때 호출된다. 공용 객체를 준비해야 한다면, 이 메서드에 작성하면 된다.
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			//프로퍼티 파일의 이름과 경로 정보도 web.xml 파일로부터 읽어오게 처리하였다.
			//getInitParameter()를 호출하여 web.xml에 설정된 매개변수를 가져온다.
			String propertiesPath = sc.getRealPath(sc.getInitParameter("contextConfigLocation"));
			//ApplicationContext 객체를 생성할 때 생성자의 매개변수로 넘겨준다.
			applicationContext = new ApplicationContext(propertiesPath);
			
		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	//웹 애플리케이션이 종료되기 전에 호출된다. 자원 해제를 해야 한다면, 이 메서드에 작성하면 된다.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
