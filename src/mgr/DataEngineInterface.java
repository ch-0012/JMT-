package mgr;

import java.util.List;

public interface DataEngineInterface {
	// �� �Ŵ����� �����ϴ� �����͸� ���̺� �����ֱ� ���� 
	// �������� ������ �迭�� ��ȯ. �ʿ��� ���� ������ŭ �迭�� ��ȯ��
	int getColumnCount();
	String[] getColumnNames();
	// Ű���忡 ��ġ�Ǵ� ���� ��� ã�� ����Ʈ�� ��ȯ
	List<Manageable> search(String kwd);
	// UI ���̺��� �࿡ �ִ� �����͸� ��Ʈ�� �迭�� �޾ƿͼ� ���ο� ��ü �߰�
	String getType();
}
