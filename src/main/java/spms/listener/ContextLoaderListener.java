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
	
	//ContextLoaderListener���� ���� ApplicationContext ��ü�� ���� �� ����Ѵ�.
	//���� ����Ʈ ��Ʈ�ѷ����� ����ؾ� �ϸ� Ŭ���� �̸������� ȣ���� �� �ְ� static���� �����Ѵ�.
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	//�� ���ø����̼��� ���۵� �� ȣ��ȴ�. ���� ��ü�� �غ��ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			//������Ƽ ������ �̸��� ��� ������ web.xml ���Ϸκ��� �о���� ó���Ͽ���.
			//getInitParameter()�� ȣ���Ͽ� web.xml�� ������ �Ű������� �����´�.
			String propertiesPath = sc.getRealPath(sc.getInitParameter("contextConfigLocation"));
			//ApplicationContext ��ü�� ������ �� �������� �Ű������� �Ѱ��ش�.
			applicationContext = new ApplicationContext(propertiesPath);
			
		} catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	//�� ���ø����̼��� ����Ǳ� ���� ȣ��ȴ�. �ڿ� ������ �ؾ� �Ѵٸ�, �� �޼��忡 �ۼ��ϸ� �ȴ�.
	@Override
	public void contextDestroyed(ServletContextEvent event) {}

}
