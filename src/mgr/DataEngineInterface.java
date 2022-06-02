package mgr;

import java.util.List;

public interface DataEngineInterface {
	// 이 매니저가 관리하는 데이터를 테이블에 보여주기 위해 
	// 열제목의 개수와 배열을 반환. 필요한 열의 개수만큼 배열이 반환됨
	int getColumnCount();
	String[] getColumnNames();
	// 키워드에 매치되는 것을 모두 찾아 리스트로 반환
	List<Manageable> search(String kwd);
	// UI 테이블의 행에 있는 데이터를 스트링 배열로 받아와서 새로운 객체 추가
	String getType();
}
