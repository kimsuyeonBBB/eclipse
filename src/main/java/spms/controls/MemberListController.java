package spms.controls;

import java.util.Map;

import spms.dao.MySqlMemberDao;

//페이지 컨트롤러가 되려면 Controller 규칙에 따라 클래스를 작성해야 한다.
public class MemberListController implements Controller {
	//MemberDao를 주입받기 위한 인스턴스 변수와 셋터 메서드를 추가하였다.
	MySqlMemberDao memberDao;
	
	public MemberListController setMemberDao(MySqlMemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}
	
	@Override
	public String execute(Map<String, Object> model) throws Exception {
		//페이지 컨트롤러가 작업한 결과물을 Map에 담는다. (Map 객체 model 매개변수에 담는다.)
		model.put("members", memberDao.selectList());
		
		//뷰 URL 반환 (페이지 컨트롤러의 반환값은 화면을 출력할 JSP의 URL이다.)
		return "/member/MemberList.jsp";
	}
}
