package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.dao.MySqlMemberDao;

public class MemberDeleteController implements Controller, DataBinding {
	MemberDao memberDao;
	
	public MemberDeleteController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Integer no = (Integer)model.get("no");
		memberDao.delete(no);
		
		return "redirect:list.do";
	}

	//회원정보를 삭제할 때 회원 번호가 필요하다.
	@Override
	public Object[] getDataBinders() {
		return new Object[] {
				"no",Integer.class
		};
	}

}
