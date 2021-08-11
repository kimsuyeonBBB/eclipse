package spms.controls;

import java.util.Map;

import spms.dao.MemberDao;

//������ ��Ʈ�ѷ��� �Ƿ��� Controller ��Ģ�� ���� Ŭ������ �ۼ��ؾ� �Ѵ�.
public class MemberListController implements Controller {
	//MemberDao�� ���Թޱ� ���� �ν��Ͻ� ������ ���� �޼��带 �߰��Ͽ���.
	MemberDao memberDao;
	
	public MemberListController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}
	
	@Override
	public String execute(Map<String, Object> model) throws Exception {
		//������ ��Ʈ�ѷ��� �۾��� ������� Map�� ��´�. (Map ��ü model �Ű������� ��´�.)
		model.put("members", memberDao.selectList());
		
		//�� URL ��ȯ (������ ��Ʈ�ѷ��� ��ȯ���� ȭ���� ����� JSP�� URL�̴�.)
		return "/member/MemberList.jsp";
	}
}
