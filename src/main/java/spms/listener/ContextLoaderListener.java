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

import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
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
			
			MemberDao memberDao = new MemberDao();
			memberDao.setDataSource(ds);
			
			//LogOutController는 MemberDao가 필요없기 때문에 셋터 메서드를 호출하지 않는다.
			sc.setAttribute("/auth/login.do", new LogInController().setMemberDao(memberDao));
			sc.setAttribute("/auth/logout.do", new LogOutController());
			sc.setAttribute("/member/list.do", new MemberListController().setMemberDao(memberDao));
			sc.setAttribute("/member/add.do", new MemberAddController().setMemberDao(memberDao));
			sc.setAttribute("/member/update.do", new MemberUpdateController().setMemberDao(memberDao));
			sc.setAttribute("/member/delete.do", new MemberDeleteController().setMemberDao(memberDao));
			
		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	//웹 애플리케이션이 종료되기 전에 호출된다. 자원 해제를 해야 한다면, 이 메서드에 작성하면 된다.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
