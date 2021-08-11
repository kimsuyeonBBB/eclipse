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
	
	//�� ���ø����̼��� ���۵� �� ȣ��ȴ�. ���� ��ü�� �غ��ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			
			InitialContext initialContext = new InitialContext();
			DataSource ds = (DataSource)initialContext.lookup("java:comp/env/jdbc/join_db");
			
			MemberDao memberDao = new MemberDao();
			memberDao.setDataSource(ds);
			
			//LogOutController�� MemberDao�� �ʿ���� ������ ���� �޼��带 ȣ������ �ʴ´�.
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
	
	//�� ���ø����̼��� ����Ǳ� ���� ȣ��ȴ�. �ڿ� ������ �ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
