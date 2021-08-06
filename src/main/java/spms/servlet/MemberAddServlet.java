package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//HttpServlet Ŭ������ GenericServlet Ŭ������ ���� Ŭ�����̴�.
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	//HttpServlet�� ��ӹ��� �� service() �޼��带 ���� �����ϱ⺸�ٴ� Ŭ���̾�Ʈ�� ��û ��Ŀ� ���� doXXX() �޼��带 �������̵� �Ѵ�.
	//���⼭�� '�ű�ȸ��' ��ũ�� Ŭ���� �� GET ��û�� �߻��ϱ� ������ doGet() �޼��带 �������̵� �Ͽ���.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = request.getRequestDispatcher("/member/MemberForm.jsp");
		rd.forward(request, response);
		/*
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>ȸ�����</title></head>");
		out.println("<body><h1>ȸ�����</h1>");
		out.println("<form action='add' method='post'>");
		out.println("�̸�: <input type='text' name='name'><br>");
		out.println("�̸���: <input type='text' name='email'><br>");
		out.println("��ȣ: <input type='password' name='password'><br>");
		out.println("<input type='submit' value='�߰�'>");
		out.println("<input type='reset' value='���'>");
		out.println("</form>");
		out.println("</body></html>");
		*/
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		//JDBC ��ü�� ������ ���� ������ �����Ѵ�.
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			/*
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			*/
			ServletContext sc = this.getServletContext();
			conn = (Connection) sc.getAttribute("conn");
			/*
			conn = DriverManager.getConnection("jdbc:mysql://localhost/join_db", "root","Ksy29396135!");
			*/
			//MEMBERS ���̺� ȸ�� ������ �Է��ϴ� SQL���� �غ��Ѵ�.
			stmt = conn.prepareStatement("INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)" + "VALUES(?,?,?,NOW(),NOW())");
			//�Է� �Ű������� ��ȣ�� 1���� �����Ѵ�.
			//�Է� �׸��� ���� SQL���� �����ϱ� ���� setXXX() �޼��带 ȣ���Ͽ� �����Ѵ�.
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("password"));
			stmt.setString(3, request.getParameter("name"));
			//SQL ����
			stmt.executeUpdate();
			
			response.sendRedirect("list");
			
			//Ŭ���̾�Ʈ���� ������ ���
			/*
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>ȸ����ϰ��</title>");
			//<meta> �±״� �ݵ�� <head>�±� �ȿ� �ؾ��Ѵ�. ���� <body> �±� �ȿ� �����ؼ��� �ȵȴ�.
			out.println("<meta http-equiv='Refresh' content='1; url=list'>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>��� �����Դϴ�.</p>");
			out.println("</body></html>");
			*/
			
			//�������� ������ ���� ����� �߰�
			//addHeader()�� HTTP ���� ������ ����� �߰��ϴ� �޼����̴�.
			//response.addHeader("Refresh", "1;url=list");
			
		} catch(Exception e) {
			//throw new ServletException(e);
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try { if (stmt != null) stmt.close();} catch(Exception e) {}
			//try { if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
}
