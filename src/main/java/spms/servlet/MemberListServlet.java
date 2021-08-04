package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

//�ֳ����̼� �̿��Ͽ� ������ ��ġ���� ����
//@WebServlet("/member/list")
//������ ��������� GenericServlet Ŭ������ ��ӹް� service() �޼ҵ带 �����Ѵ�.
public class MemberListServlet extends GenericServlet{

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		//JDBC ��ü �ּҸ� ������ �������� ����
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			//����� JDBC ����̹��� ���
			//com.mysql.jdbc.Driver() Ŭ������ WEB-INF/lib ������ �ִ� jar�� ����ִ�.
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//����̹��� ����Ͽ� MySQL ������ ����
			conn = DriverManager.getConnection("jdbc:mysql://localhost/join_db",
					"root","Ksy29396135!");
			//Ŀ�ؼ� ����ü �̿��Ͽ� SQL���� ������ ��ü �غ�
			stmt = conn.createStatement();
			//executeQuery()�� ����� ��������� SQL���� ������ �� ����Ѵ�.
			rs = stmt.executeQuery("select MNO,MNAME,EMAIL,CRE_DATE" + " from join_db.MEMBERS" + " order by MNO ASC");
			//�������� ������ �����͸� ����Ͽ� HTML ���� ���������� ���
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>ȸ�����</title></head>");
			out.println("<body><h1>ȸ�����</h1>");
			out.println("<p><a href='add'>�ű� ȸ��</a></p>");
			
			//select ���� ����� �������� �κ�
			while(rs.next()) {
				out.println(rs.getInt("MNO") + "," + "<a href='update?no=" + rs.getInt("MNO") + "'>"
			+ rs.getString("MNAME") + "</a>," + rs.getString("EMAIL") + ","
						+ rs.getDate("CRE_DATE") + "<a href='delete?no=" + rs.getInt("MNO") + "'>" + "[����]" + "</a>"+ "<br>");
			}
			out.println("</body></html>");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			//�ڿ��� ������ ���� �������� ó���Ѵ�.
			try { if (rs != null) rs.close();} catch(Exception e) {}
			try { if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}

}
