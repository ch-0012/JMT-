package library;

import java.util.List;

import mgr.DataEngineInterface;
import mgr.Manageable;

public class BookMgr implements DataEngineInterface {
	private static BookMgr mgr = null;
	private BookMgr() {}
	public static BookMgr getInstance() {
		if(mgr == null)
			mgr = new BookMgr();
		return mgr;
	}
	public Book getBook(int index) {
		return (Book)Library.bookMgr.mList.get(index);
	}
	private String[] headers = { "제목", "저자", "출판사", "대출가능여부" };
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String[] getColumnNames() {
		return headers;
	}

	@Override
	public List<Manageable> search(String kwd) {
		// TODO Auto-generated method stub
		return Library.bookMgr.findAll(kwd);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "BookMgr";
	}

}
