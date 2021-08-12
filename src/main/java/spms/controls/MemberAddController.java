package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.dao.MySqlMemberDao;
import spms.vo.Member;

//�Ϲ� Ŭ�����̱� ������ Ŭ���̾�Ʈ�� ��û�� ���� GET�� POST�� ������ �� ����.
//�׷��� Map ��ü�� VO��ü "Member"�� ��������� POST ��û���� �����ϰ�, �׷��� ������ GET ��û���� �����Ͽ���.
//MemberAddController�� Ŭ���̾�Ʈ�� ���� �����͸� ����Ʈ ��Ʈ�ѷ��κ��� �޾ƾ� �ϱ� ������ DataBinding �������̽��� �����Ѵ�.
public class MemberAddController implements Controller, DataBinding {
	MemberDao memberDao;
	
	public MemberAddController setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
		return this;
	}

	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Member member = (Member)model.get("member");
		//����Ʈ ��Ʈ�ѷ��� VO ��ü�� ������ ������ ���̱� ������ Member�� ������ �ƴ� Member�� �̸����� ����ִ��� ���θ� �˻��ؾ� �Ѵ�.
		if(member.getEmail() == null) {  //�Է����� ��û�� ��
			return "/member/MemberForm.jsp";
		} else {  //ȸ������� ��û�� ��
			memberDao.insert(member);
			return "redirect:list.do";
		}
	}

	@Override
	public Object[] getDataBinders() {
		//Ŭ���̾�Ʈ�� ���� �Ű����� ���� Member �ν��Ͻ��� ��Ƽ� "member"��� �̸����� Map ��ü�� ������ �޶�� �ǹ��̴�.
		return new Object[] {
				"member",spms.vo.Member.class
		};
	}

}
