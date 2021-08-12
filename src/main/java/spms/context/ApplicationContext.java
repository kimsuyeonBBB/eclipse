package spms.context;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;



//ApplicationContext�� ���� ������ ������ ��Ʈ�ѷ��� DAO�� �߰��Ǵ��� ContextLoaderListener�� �������� �ʱ� �����̴�.

//������Ƽ ���Ͽ� ������ ��ü�� �غ��ϴ� ���� �Ѵ�.
public class ApplicationContext {
	//������Ƽ�� ������ ��� ��ü�� �غ��ϸ� ��ü�� ������ �����Ұ� �ʿ��ϴ�. �̸� ���� �ؽ� ���̺��� �غ��Ѵ�.
	Hashtable<String,Object> objTable = new Hashtable<String,Object>();
	
	//�ؽ� ���̺��� ��ü�� ���� �޼��嵵 �����Ѵ�.
	public Object getBean(String key) {
		return objTable.get(key);
	}
	
	public ApplicationContext(String propertiesPath) throws Exception{
		//ApplicationContext �����ڰ� ȣ��Ǹ� �Ű������� ������ ������Ƽ ������ ������ �ε��ؾ��Ѵ�.
		Properties props = new Properties();
		props.load(new FileReader(propertiesPath));
		
		prepareObjects(props);
		injectDependency();
	}
	//������Ƽ ������ ������ �ε������� �׿� ���� ��ü�� �غ��ؾ��Ѵ�.
	private void prepareObjects(Properties props) throws Exception{
		//JDNI ��ü�� ã�� �� ����� InitialContext�� �غ��Ѵ�.
		Context ctx = new InitialContext();
		String key = null;
		String value = null;
		
		//�ݺ����� ���� ������Ƽ�� ����ִ� ������ ������ ��ü�� �����Ѵ�.
		for(Object item : props.keySet()) {
			key = (String) item;
			value = props.getProperty(key);
			//���� ������Ƽ�� Ű�� "jndi."�� �����Ѵٸ� ��ü�� �������� �ʰ� InitialContext�� ���� ��´�.
			//InitialContext�� lookup() �޼���� JNDI �������̽��� ���� ��Ĺ ������ ��ϵ� ��ü�� ã�´�.
			if(key.startsWith("jndi.")) {
				objTable.put(key, ctx.lookup(value));
			} else {
				//�� ���� ��ü�� Class.forName()�� ȣ���Ͽ� Ŭ������ �ε��ϰ� newInstance()�� ����Ͽ� �ν��Ͻ��� �����Ѵ�.
				objTable.put(key, Class.forName(value).newInstance());
			}
		}
	}
	
	//��Ĺ �����κ��� ��ü�� �������ų�(DataSource) ���� ��ü�� ����������(MemberDao) ������ �� ��ü�� �ʿ�� �ϴ� ���� ��ü�� �Ҵ��� �־�� �Ѵ�.
	private void injectDependency() throws Exception{
		for(String key: objTable.keySet()) {
			//��ü �̸��� "jndi."�� �����ϴ� ��� ��Ĺ �������� ������ ��ü�̹Ƿ� ���� ��ü�� �����ؼ��� �ȵȴ�.
			//������ ��ü�� ���ؼ��� ���� �޼��带 ȣ���Ͽ� ���ϴ� ��ü�� �Ҵ��Ѵ�.
			if(!key.startsWith("jndi.")) {
				callSetter(objTable.get(key));
			}
		}
	}
	
	//�Ű������� �־��� ��ü�� ���� ���� �޼��带 ã�Ƽ� ȣ���ϴ� ���� �Ѵ�.
	private void callSetter(Object obj) throws Exception{
		Object dependency = null;
		for(Method m: obj.getClass().getMethods()) {
			if(m.getName().startsWith("set")) {
				
				//���� �޼��带 ã������ ���� �޼����� �Ű������� Ÿ���� ��ġ�ϴ� ��ü�� objTable���� ã�´�.
				dependency = findObjectByType(m.getParameterTypes()[0]);
				//���� ��ü�� ã������, ���� �޼��带 ȣ���Ѵ�.
				if(dependency != null) {
					m.invoke(obj, dependency);
				}
			}
		}
	}
	
	//�� �޼���� ���� �޼��带 ȣ���� �� �Ѱ��� ���� ��ü�� ã�� ���� �Ѵ�.
	private Object findObjectByType(Class<?> type) {
		for(Object obj : objTable.values()) {
			//���� ���� �޼����� �Ű����� Ÿ�԰� ��ġ�ϴ� ��ü�� ã�Ҵٸ� �� ��ü�� �ּҸ� �����Ѵ�.
			if(type.isInstance(obj)) {
				return obj;
			}
		}
		return null;
	}
}
