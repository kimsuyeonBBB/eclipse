package spms.controls;

import java.util.Map;

import spms.dao.MemberDao;
import spms.vo.Member;

//일반 클래스이기 때문에 클라이언트의 요청에 대해 GET과 POST를 구분할 수 없다.
//그래서 Map 객체에 VO객체 "Member"가 들어있으면 POST 요청으로 간주하고, 그렇지 않으면 GET 요청으로 간주하였다.
public class MemberAddController implements Controller {
	MemberDao memberDao;
	
	public MemberAddController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		if(model.get("member")==null) { //입력폼을 요청할 때
			return "/member/MemberForm.jsp";
			
		} else {  //회원 등록을 요청할 때			
			Member member = (Member)model.get("member");
			memberDao.insert(member);
			
			return "redirect:list.do";
		}
	}

}
