package spms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class DBConnectionPool {
	String url;
	String username;
	String password;
	ArrayList<Connection> connList = new ArrayList<Connection>();
	
	//�����ڿ����� DB Ŀ�ؼ� ������ �ʿ��� ���� �Ű������� �޴´�.
	public DBConnectionPool(String driver, String url, String username, String password) throws Exception{
		this.url = url;
		this.username = username;
		this.password = password;
		
		Class.forName(driver);
	}
	
	//DB Ŀ�ؼ��� �޶�� ��û������ ArrayList�� ��� �ִ� ���� �����ش�.
	public Connection getConnection() throws Exception{
		if(connList.size()>0) {
			Connection conn = connList.get(0);
			//DB Ŀ�ؼ� ��ü�� ���� �ð��� ������ �������� ������ �������� ������ ��ȿ�� üũ�� �� ���� ��ȯ���ش�.
			if(conn.isValid(10)) {
				return conn;
			}
		}
		//ArrayList�� ������ ��ü�� ���ٸ� DriverManager�� ���� ���� ����� ��ȯ�Ѵ�.
		return DriverManager.getConnection(url,username,password);
	}
	//Ŀ�ؼ� ��ü�� ���� �� �������� �� �޼��带 ȣ���Ͽ� Ŀ�ؼ� Ǯ�� ��ȯ�Ѵ�.
	//�׷��߸� ������ �ٽ� ����� �� �ִ�.
	public void returnConnection(Connection conn) throws Exception{
		connList.add(conn);
	}
	
	//�� ���ø����̼��� �����ϱ� ���� �� �޼��带 ȣ���Ͽ� �����ͺ��̽��� ����� ���� ��� ����� �Ѵ�.
	public void closeAll() {
		for(Connection conn: connList) {
			try {conn.close();} catch(Exception e) {}
		}
	}
}
