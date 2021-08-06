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

import spms.vo.Member;

//애노테이션 이용하여 서블릿의 배치정보 설정
//@WebServlet("/member/list")
//서블릿을 만들기위해 GenericServlet 클래스를 상속받고 service() 메소드를 구현한다.
public class MemberListServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	//HTML을 출력하는 모든 코드를 제거한다.
	//회원목록 화면을 생성하고 출력하는 것은 MemberList.jsp가 담당할 것이다.
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			ServletContext sc = this.getServletContext();
			/*
			//DB 커넥션을 준비하는 코드 삭제 
			//(서블릿에서 DB 커넥션을 직접 준비할 필요가 없기 때문이다. ServletContext 보관소에 저장된 DB 커넥션 객체를 꺼내쓰면 된다.)
			Class.forName(sc.getInitParameter("driver"));
			conn = DriverManager.getConnection(sc.getInitParameter("url"),sc.getInitParameter("username"),sc.getInitParameter("password"));  */
			
			//ServletContext 보관소에서 DB 커넥션을 꺼낸다.
			conn = (Connection) sc.getAttribute("conn");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT MNO,MNAME,EMAIL,CRE_DATE" + " FROM MEMBERS" + " ORDER BY MNO ASC");
			
			response.setContentType("text/html; charset=UTF-8");
			ArrayList<Member> members = new ArrayList<Member>();
			
			//데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			//그리고 Member 객체를 ArrayList에 추가한다.
			//객체를 생성한 즉시 셋터 메서드를 연속으로 호출하여 값을 할당한다.
			while(rs.next()) {
				members.add(new Member().setNo(rs.getInt("MNO")).setName(rs.getString("MNAME")).setEmail(rs.getString("EMAIL")).setCreatedDate(rs.getDate("CRE_DATE")));
			}
			
			//request에 회원 목록 데이터 보관한다.
			request.setAttribute("members", members);
			
			//JSP로 출력을 위임한다.
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberList.jsp");
			rd.include(request, response);
		} catch(Exception e) {
			//throw new ServletException(e);
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}
			try {if (stmt != null) rs.close();} catch(Exception e) {}
			//try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}
	
}	
	/*
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		//JDBC 객체 주소를 보관할 참조변수 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			//사용할 JDBC 드라이버를 등록
			//com.mysql.jdbc.Driver() 클래스는 WEB-INF/lib 폴더에 있는 jar에 들어있다.
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			//드라이버를 사용하여 MySQL 서버와 연결
			conn = DriverManager.getConnection("jdbc:mysql://localhost/join_db",
					"root","Ksy29396135!");
			//커넥션 구현체 이용하여 SQL문을 실행할 객체 준비
			stmt = conn.createStatement();
			//executeQuery()는 결과가 만들어지는 SQL문을 실행할 때 사용한다.
			rs = stmt.executeQuery("select MNO,MNAME,EMAIL,CRE_DATE" + " from join_db.MEMBERS" + " order by MNO ASC");
			//서버에서 가져온 데이터를 사용하여 HTML 만들어서 웹브라우저로 출력
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>회원목록</title></head>");
			out.println("<body><h1>회원목록</h1>");
			out.println("<p><a href='add'>신규 회원</a></p>");
			
			//select 실행 결과를 가져오는 부분
			while(rs.next()) {
				out.println(rs.getInt("MNO") + "," + "<a href='update?no=" + rs.getInt("MNO") + "'>"
			+ rs.getString("MNAME") + "</a>," + rs.getString("EMAIL") + ","
						+ rs.getDate("CRE_DATE") + "<a href='delete?no=" + rs.getInt("MNO") + "'>" + "[삭제]" + "</a>"+ "<br>");
			}
			out.println("</body></html>");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			//자원을 해제할 때는 역순으로 처리한다.
			try { if (rs != null) rs.close();} catch(Exception e) {}
			try { if (stmt != null) stmt.close();} catch(Exception e) {}
			try {if (conn != null) conn.close();} catch(Exception e) {}
		}
	}

}
*/
