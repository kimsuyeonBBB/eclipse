package spms.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(urlPatterns="/*", initParams= {
		@WebInitParam(name="encoding", value="UTF-8")
})

public class CharacterEncodingFilter implements Filter {
	FilterConfig config;

	@Override
	public void destroy() {
		
	}

	//필터와 연결된 URL에 대한 요청이 들어오면 doFilter()가 항상 호출된다.
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain nextFilter) throws IOException, ServletException{
		request.setCharacterEncoding(config.getInitParameter("encoding"));
		nextFilter.doFilter(request, response);
	}
			
	//init() 메서드는 필터 객체가 생성되고 준비 작업을 위해 딱 한번 호출된다.
	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		this.config = config;
	}
	
	
}
