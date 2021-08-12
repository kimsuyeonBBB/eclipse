package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.dao.MySqlMemberDao;
import spms.vo.Member;

public class MemberUpdateController implements Controller, DataBinding{
	MemberDao memberDao;
	
	public MemberUpdateController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Member member = (Member)model.get("member");
		
		if(member.getEmail() == null) {
			Integer no = (Integer)model.get("no");
			Member detailInfo = memberDao.selectOne(no);
			model.put("member", detailInfo);
			return "/member/MemberUpdateForm.jsp";
			
		} else {
			memberDao.update(member);
			return "redirect:list.do";
		}
	}

	//변경폼을 출력할 때 회원번호가 필요하고, 데이터를 변경할 때 Member 인스턴스가 필요하다.
	@Override
	public Object[] getDataBinders() {
		return new Object[] {
				"no",Integer.class,
				"member", spms.vo.Member.class
		};
	}

}
