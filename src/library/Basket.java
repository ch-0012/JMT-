package library;

import java.util.ArrayList;
import java.util.Scanner;

import mgr.Manageable;

//Basket 클래스를 User의 내부 클래스로 사용하는 방안도 생각해보면 좋을 것 같다.
public class Basket implements Manageable {
	public ArrayList<Book> basketedBookList = new ArrayList<>();

	public void print() {
		System.out.println("----장바구니----");
		for (Book b : basketedBookList) {
			b.print();
		}
	}
	
	public void add(String kwd) {
		Book tmp = (Book)Library.bookMgr.find(kwd);
		if(basketedBookList.contains(tmp))
			return;
		basketedBookList.add(tmp);
	}

	public void sub(ArrayList<Book> checked) {
		basketedBookList.removeAll(checked);
	}
	
	Book getBook() {
		if(basketedBookList.isEmpty())
			return null;
		Book tmp = basketedBookList.get(0);
		basketedBookList.remove(0);
		return tmp;
	}

	@Override
	public void read(Scanner scan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean matches(String kwd) {
		// TODO Auto-generated method stub
		return false;
	}
}
