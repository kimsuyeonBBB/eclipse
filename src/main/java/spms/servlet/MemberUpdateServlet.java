package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//���ؽ�Ʈ �ʱ�ȭ �Ű������� ���� ��� ���� ��ü�� �ʿ��ϴ�.
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			conn = DriverManager.getConnection(sc.getInitParameter("url"), sc.getInitParameter("username"),sc.getInitParameter("password"));

			/*
			//�ʱ�ȭ �Ű������� �̿��Ͽ� JDBC ����̹� �ε�
			//(Class.forName()�� ����Ͽ� JDBC ����̹� Ŭ����, �� java.sql.Driver�� ������ Ŭ�����̴�.)
			Class.forName(this.getInitParameter("driver"));
			//�����ͺ��̽��� ������ ���� �ʱ�ȭ �Ű����� �̿��Ѵ�.
			conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("username"),this.getInitParameter("password"));
			*/
			stmt = conn.createStatement();
			//��û �Ű������� �Ѿ�� ȸ�� ��ȣ�� ������ ȸ�� ������ �����Ѵ�.
			rs = stmt.executeQuery("select MNO, EMAIL,MNAME,CRE_DATE from join_db.MEMBERS" + " where MNO=" + request.getParameter("no"));
			//�Ѹ��� ������ �������� ������ next()�� �ѹ��� ȣ���Ѵ�.
			rs.next();
			
			
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>ȸ������</title></head>");
			out.println("<body><h1>ȸ������</h1>");
			out.println("<form action='update' method='post'>");
			//ȸ����ȣ�� PRIMARY KEY�̱� ������ �Է»��ڸ� readonly �Ӽ��� �߰��Ͽ� �б��������� ���� 
			out.println("��ȣ: <input type='text' name='no' value='" + request.getParameter("no") + "'readonly><br>");
			out.println("�̸�: *<input type='text' name='name'" + " value='" + rs.getString("MNAME") + "'><br>");
			out.println("�̸���: <input type='text' name='email'" + " value='" + rs.getString("EMAIL") + "'><br>");
			out.println("������: " + rs.getDate("CRE_DATE") + "<br>");
			out.println("<input type='submit' value='����'>");
			out.println("<input type='button' value='���'" + " onclick='location.href=\"list\"'>");
			out.println("<input type='button' value='����'" + " onclick='location.href=\"delete?no=" + request.getParameter("no") + "\";'>");
			out.println("</form>");
			out.println("</body></html>");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try { if (rs != null ) rs.close();} catch(Exception e) {}
			try { if (stmt != null ) stmt.close();} catch(Exception e) {}
			try { if (conn != null ) conn.close();} catch(Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//doGet()�޼���ó�� �ʱ�ȭ �Ű����� �̿��Ͽ� JDBC ����̹� �ε�
			Class.forName(this.getInitParameter("driver"));
			//�����ͺ��̽� ������ �� ���� �ʱ�ȭ �Ű����� �̿�
			conn = DriverManager.getConnection(this.getInitParameter("url"), this.getInitParameter("username"), this.getInitParameter("password"));
			////ȸ������ �����ϱ� ���� UPDATE�� ��� 
			stmt = conn.prepareStatement("UPDATE MEMBERS SET EMAIL=?,MNAME=?,MOD_DATE=now()" + " WHERE MNO=?");
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			stmt.executeUpdate();
			response.sendRedirect("list");
		} catch(Exception e) {
			throw new ServletException(e);
		} finally {
			try {if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
