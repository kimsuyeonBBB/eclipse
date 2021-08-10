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

import spms.dao.MemberDao;
import spms.util.DBConnectionPool;

public class ContextLoaderListener implements ServletContextListener{
	
	//웹 애플리케이션이 시작될 때 호출된다. 공용 객체를 준비해야 한다면, 이 메서드에 작성하면 된다.
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			
			InitialContext initialContext = new InitialContext();
			DataSource ds = (DataSource)initialContext.lookup("java:comp/env/jdbc/join_db");
			
			//웹 애플리케이션이 시작될 때 MemberDao 객체를 준비하여 ServletContext에 보관한다.
			MemberDao memberDao = new MemberDao();
			memberDao.setDataSource(ds);
			
			sc.setAttribute("memberDao", memberDao);
		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	//웹 애플리케이션이 종료되기 전에 호출된다. 자원 해제를 해야 한다면, 이 메서드에 작성하면 된다.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
