package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.dao.MySqlMemberDao;
import spms.vo.Member;

//일반 클래스이기 때문에 클라이언트의 요청에 대해 GET과 POST를 구분할 수 없다.
//그래서 Map 객체에 VO객체 "Member"가 들어있으면 POST 요청으로 간주하고, 그렇지 않으면 GET 요청으로 간주하였다.
//MemberAddController는 클라이언트가 보낸 데이터를 프런트 컨트롤러로부터 받아야 하기 때문에 DataBinding 인터페이스를 구현한다.
public class MemberAddController implements Controller, DataBinding {
	MemberDao memberDao;
	
	public MemberAddController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Member member = (Member)model.get("member");
		//프런트 컨트롤러가 VO 객체를 무조건 생성할 것이기 때문에 Member의 유무가 아닌 Member에 이메일이 들어있는지 여부를 검사해야 한다.
		if(member.getEmail() == null) {  //입력폼을 요청할 때
			return "/member/MemberForm.jsp";
		} else {  //회원등록을 요청할 때
			memberDao.insert(member);
			return "redirect:list.do";
		}
	}

	@Override
	public Object[] getDataBinders() {
		//클라이언트가 보낸 매개변수 값을 Member 인스턴스에 담아서 "member"라는 이름으로 Map 객체에 저장해 달라는 의미이다.
		return new Object[] {
				"member",spms.vo.Member.class
		};
	}

}
