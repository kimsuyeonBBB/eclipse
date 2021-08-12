package spms.bind;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Set;

import javax.servlet.ServletRequest;

//Ŭ���̾�Ʈ�� ���� �Ű����� ���� �ڹ� ��ü�� ��� �ִ� ������ �����Ѵ�.
public class ServletRequestDataBinder {
	
	//Ư�� �ν��Ͻ��� ���� �ٷ��� �ʴ´ٸ� static���� �����Ͽ� 'Ŭ���� �޼���'�� ����� ���� ����.
	//bind() �޼���� ����Ʈ ��Ʈ�ѷ����� ȣ���ϴ� �޼����̴�.
	//��û �Ű������� ���� ������ �̸�, ������ Ÿ���� �޾Ƽ� ������ ��ü�� ����� ���� �Ѵ�.
	public static Object bind(ServletRequest request, Class<?> dataType, String dataName) throws Exception{
		if(isPrimitiveType(dataType)) {
			return createValueObject(dataType, request.getParameter(dataName));
		}
		//�Ű������� �̸� ����� �ҷ��´�.
		Set<String> paramNames = request.getParameterMap().keySet();
		//���� ������ ��ü�� �����Ѵ�. new �����ڸ� �̿����� �ʰ� �̷������� ��ü�� ������ �� �ִ�.
		Object dataObject = dataType.newInstance();
		Method m = null;
		
		//��û �Ű������� �̸� ����� �غ�Ǿ����� for �ݺ����� �����Ѵ�.
		for(String paramName: paramNames) {
			//������ Ÿ�� Ŭ�������� �Ű����� �̸��� ��ġ�ϴ� ������Ƽ(���� �޼���)�� ã�´�.
			//findSetter()�� ���ο� ����� �޼����̴�.������ Ÿ�԰� �Ű����� �̸��� �ָ� ���� �޼��带 ã�Ƽ� ��ȯ�Ѵ�.
			m = findSetter(dataType, paramName);
			//���� �޼��带 ã������ ������ ������ dataObject�� ���� ȣ���Ѵ�.
			if( m != null) {
				m.invoke(dataObject, createValueObject(m.getParameterTypes()[0], request.getParameter(paramName)));
			}
		}
		return dataObject;
	}
	
	//�Ű������� �־��� Ÿ���� �⺻ Ÿ������ �˻��ϴ� �޼���
	private static boolean isPrimitiveType(Class<?> type) {
		if(type.getName().equals("int") || type == Integer.class ||
				type.getName().equals("long") || type == Long.class ||
				type.getName().equals("float") || type == Float.class ||
				type.getName().equals("double") || type == Double.class ||
				type.getName().equals("boolean") || type == Boolean.class ||
				type == Date.class || type == String.class) {
			return true;
		}
		return false;
	}
	
	//createValueObject()�� ���ͷ� ���� �Ҵ��� �� ���� �⺻ Ÿ�Կ� ���� ��ü�� �����ϴ� �޼����̴�.
	private static Object createValueObject(Class<?> type, String value) {
		if(type.getName().equals("int") || type == Integer.class) {
			return new Integer(value);
		} else if(type.getName().equals("float") || type == Float.class) {
			return new Float(value);
		} else if(type.getName().equals("double") || type == Double.class) {
			return new Double(value);
		} else if(type.getName().equals("long") || type == Long.class) {
			return new Long(value);
		} else if(type.getName().equals("boolean") || type == Boolean.class) {
			return new Boolean(value);
		} else if(type == Date.class) {
			return java.sql.Date.valueOf(value);
		} else {
			return value;
		}
	}
	
	//Ŭ����(type)�� �����Ͽ� �־��� �̸�(name)�� ��ġ�ϴ� ���� �޼��带 ã�´�.
	private static Method findSetter(Class<?> type, String name) {
		//���� ������ Ÿ�Կ��� �޼��� ����� ��´�.
		Method[] methods = type.getMethods();
		
		String propName = null;
		//�޼��� ����� �ݺ��Ͽ� ���� �޼��忡 ���ؼ��� �۾��� �����Ѵ�.
		//���� �޼��� �̸��� set���� �������� �ʴ´ٸ� �����Ѵ�.
		for(Method m : methods) {
			if(!m.getName().startsWith("set")) continue;
			//���� �޼����� ��� ��û �Ű������� �̸��� ��ġ�ϴ��� �˻��Ѵ�.
			propName = m.getName().substring(3);
			//��ҹ��� �������� �ʱ� ���� ��� �ҹ��ڷ� �ٲ� ���� ���Ѵ�.
			if(propName.toLowerCase().equals(name.toLowerCase())) {
				return m;
			}
		}
		return null;
	}

}
