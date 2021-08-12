package spms.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.dao.MySqlMemberDao;

//@WebServlet("/member/delete")
public class MemberDeleteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			ServletContext sc = this.getServletContext();
			MySqlMemberDao memberDao = (MySqlMemberDao)sc.getAttribute("memberDao");
			
			memberDao.delete(Integer.parseInt(request.getParameter("no")));
			request.setAttribute("viewUrl", "redirect:list.do");
			
		} catch(Exception e) {
			throw new ServletException(e);
		} 
	}
	
}
