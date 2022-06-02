package library;

import java.util.ArrayList;
import java.util.List;

import mgr.DataEngineInterface;
import mgr.Manageable;

//대출된 것 현황 table
public class LoanedMgr implements DataEngineInterface {
	private static LoanedMgr mgr = null;

	private LoanedMgr() {
	}

	public static LoanedMgr getInstance() {
		if (mgr == null)
			mgr = new LoanedMgr();
		return mgr;
	}

	List<BookLoan> myList = new ArrayList<>();

	public void setLoan(User user) {
		myList = user.getMyList();
	}

	public BookLoan getLoan(int index) {
		return (BookLoan) Library.loanMgr.mList.get(index);
	}

	private String[] headers = { "제목", "저자", "대출일", "반납일", "상태" };

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String[] getColumnNames() {
		return headers;
	}

	@Override
	public List<Manageable> search(String kwd) {
		List<Manageable> result = new ArrayList<Manageable>();
		for(BookLoan l: myList)
			result.add(l);
		return result;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "LoanedMgr";
	}

}
