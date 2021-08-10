package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MemberDao;
import spms.vo.Member;

//프런트 컨트롤러 적용

//@WebServlet("/member/list")
public class MemberListServlet extends HttpServlet{	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			//MemberDao를 사용하기 전에 셋터를 먼저 호출하여 ServletContext에서 꺼낸 DB 커넥션 객체를 주입하였다.
			ServletContext sc = this.getServletContext();
			
			MemberDao memberDao = (MemberDao) sc.getAttribute("memberDao");
			
			request.setAttribute("members", memberDao.selectList());
			
			request.setAttribute("viewUrl", "/member/MemberList.jsp");
			
		} catch(Exception e) {
			throw new ServletException(e);
		} 
	}
	
}	

	
