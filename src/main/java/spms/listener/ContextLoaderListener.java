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
	
	//�� ���ø����̼��� ���۵� �� ȣ��ȴ�. ���� ��ü�� �غ��ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			
			InitialContext initialContext = new InitialContext();
			DataSource ds = (DataSource)initialContext.lookup("java:comp/env/jdbc/join_db");
			
			//�� ���ø����̼��� ���۵� �� MemberDao ��ü�� �غ��Ͽ� ServletContext�� �����Ѵ�.
			MemberDao memberDao = new MemberDao();
			memberDao.setDataSource(ds);
			
			sc.setAttribute("memberDao", memberDao);
		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	//�� ���ø����̼��� ����Ǳ� ���� ȣ��ȴ�. �ڿ� ������ �ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
