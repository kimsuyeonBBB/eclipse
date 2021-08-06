package spms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/auth/logout")
public class LogOutServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession();
		//HttpSession 객체를 무효화 하기 위해 invalidate()를 호출한다.
		//세션 객체가 무효화 된다는 것은 HttpSession 객체가 제거된다는 것을 의미한다.
		session.invalidate();
		
		response.sendRedirect("login");
	}
}
