package spms.controls;

import java.util.Map;

public interface Controller {
	//execute()�� ����Ʈ ��Ʈ�ѷ��� ������ ��Ʈ�ѷ����� ���� ��Ű�� ���� ȣ���ϴ� �޼����̴�.
	//����Ʈ ��Ʈ�ѷ��� execute()�� ȣ���Ϸ��� Map ��ü�� �Ű������� �Ѱ��־�� �Ѵ�.
	String execute(Map<String, Object> model) throws Exception;
}
