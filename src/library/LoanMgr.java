package library;

import java.util.ArrayList;
import java.util.List;

import mgr.DataEngineInterface;
import mgr.Manageable;

//대출 할 것 table
public class LoanMgr implements DataEngineInterface{
	private static LoanMgr mgr = null;
	private LoanMgr() {}
	public static LoanMgr getInstance() {
		if(mgr == null)
			mgr = new LoanMgr();
		return mgr;
	}
	List<Book> loanList;
	private String[] headers = { "제목", "저자", "출판사" };
	public void setLoan(ArrayList<Book> check) {
		loanList = check;
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return headers;
	}

	@Override
	public List<Manageable> search(String kwd) {
		List<Manageable> result = new ArrayList<Manageable>();
		for(Book book: loanList)
			result.add(book);
		return result;
	}

	@Override
	public String getType() {
		return "LoanMgr";
	}

}
