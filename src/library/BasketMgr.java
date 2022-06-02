package library;

import java.util.ArrayList;
import java.util.List;

import mgr.DataEngineInterface;
import mgr.Manageable;

public class BasketMgr implements DataEngineInterface {
	private static BasketMgr mgr = null;
	private BasketMgr() {}
	public static BasketMgr getInstance() {
		if(mgr == null)
			mgr = new BasketMgr();
		return mgr;
	}
	List<Book> basketList;
	private String[] headers = { "제목", "저자", "출판사", "대출가능여부", "선택" };
	public void setBasket(User user) {
		basketList = user.basket.basketedBookList;
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return headers;
	}

	@Override
	public List<Manageable> search(String kwd) {
		List<Manageable> result = new ArrayList<Manageable>();
		for(Book book: basketList)
			result.add(book);
		return result;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "BasketMgr";
	}

}
