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

//HttpServlet 클래스는 GenericServlet 클래스의 하위 클래스이다.
@WebServlet("/member/add")
public class MemberAddServlet extends HttpServlet {
	//HttpServlet을 상속받을 때 service() 메서드를 직접 구현하기보다는 클라이언트의 요청 방식에 따라 doXXX() 메서드를 오버라이딩 한다.
	//여기서는 '신규회원' 링크를 클릭할 때 GET 요청이 발생하기 때문에 doGet() 메서드를 오버라이딩 하였다.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = request.getRequestDispatcher("/member/MemberForm.jsp");
		rd.forward(request, response);
		/*
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<html><head><title>회원등록</title></head>");
		out.println("<body><h1>회원등록</h1>");
		out.println("<form action='add' method='post'>");
		out.println("이름: <input type='text' name='name'><br>");
		out.println("이메일: <input type='text' name='email'><br>");
		out.println("암호: <input type='password' name='password'><br>");
		out.println("<input type='submit' value='추가'>");
		out.println("<input type='reset' value='취소'>");
		out.println("</form>");
		out.println("</body></html>");
		*/
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		//JDBC 객체를 보관할 참조 변수를 선언한다.
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
			//MEMBERS 테이블에 회원 정보를 입력하는 SQL문을 준비한다.
			stmt = conn.prepareStatement("INSERT INTO MEMBERS(EMAIL,PWD,MNAME,CRE_DATE,MOD_DATE)" + "VALUES(?,?,?,NOW(),NOW())");
			//입력 매개변수의 번호는 1부터 시작한다.
			//입력 항목의 값은 SQL문을 실행하기 전에 setXXX() 메서드를 호출하여 설정한다.
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("password"));
			stmt.setString(3, request.getParameter("name"));
			//SQL 실행
			stmt.executeUpdate();
			
			response.sendRedirect("list");
			
			//클라이언트에게 실행결과 출력
			/*
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>회원등록결과</title>");
			//<meta> 태그는 반드시 <head>태그 안에 해야한다. 절대 <body> 태그 안에 선언해서는 안된다.
			out.println("<meta http-equiv='Refresh' content='1; url=list'>");
			out.println("</head>");
			out.println("<body>");
			out.println("<p>등록 성공입니다.</p>");
			out.println("</body></html>");
			*/
			
			//리프레시 정보를 응답 헤더에 추가
			//addHeader()는 HTTP 응답 정보에 헤더를 추가하는 메서드이다.
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
