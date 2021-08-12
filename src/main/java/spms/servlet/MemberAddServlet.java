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

import spms.dao.MySqlMemberDao;
import spms.vo.Member;

//����Ʈ ��Ʈ�ѷ� ����

//HttpServlet Ŭ������ GenericServlet Ŭ������ ���� Ŭ�����̴�.
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	
	//HttpServlet�� ��ӹ��� �� service() �޼��带 ���� �����ϱ⺸�ٴ� Ŭ���̾�Ʈ�� ��û ��Ŀ� ���� doXXX() �޼��带 �������̵� �Ѵ�.
	//���⼭�� '�ű�ȸ��' ��ũ�� Ŭ���� �� GET ��û�� �߻��ϱ� ������ doGet() �޼��带 �������̵� �Ͽ���.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//MemberForm.jsp�� URL�� ServletRequest�� �����Ѵ�.
		request.setAttribute("viewUrl", "/member/MemberForm.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			ServletContext sc = this.getServletContext();
			MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
			
			//����Ʈ ��Ʈ�ѷ��� �غ��س��� Member ��ü�� ServletRequest �����ҿ��� �������� �ڵ� �ۼ�
			Member member = (Member)request.getAttribute("member");
			memberDao.insert(member);
			
			//�����̷�Ʈ �ؾ� �ϴ� ��쿡 �ݵ�� URL �պκп� "redirect:" ���ڿ��� �ٿ��� �Ѵ�.
			request.setAttribute("viewUrl", "redirect:list.do");
			
		} catch(Exception e) {
			throw new ServletException(e);
		} 
	}
}
