package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import mgr.*;

public class User implements Manageable {
	public int position;
	// 0: 재학생(10, 15), 1: 대학원생(20, 30), 2: 졸업생(5, 10), 3: 교수(20, 30), 4: 직원(10, 30)
	String userId = null;
	String userPw = null;
	public String name = null;
	String phone = null;
	int[] bookNum = { 10, 20, 5, 20, 10 };
	public int[] bookLoanDate = { 15, 30, 10, 30, 30 }; // 대출 기한
	boolean bookLoanAvailability = true;
	public Basket basket;
	public ArrayList<Book> recommendBooklist = new ArrayList<>();
	int[] interested = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	@Override
	public void read(Scanner scan) {
		position = scan.nextInt();
		userId = scan.next();
		userPw = scan.next();
		name = scan.next();
		phone = scan.next();
	}

	@Override
	public void print() {
		System.out.printf("[%s] %s %s\n", userId, name, phone);
	}

	public boolean matches(String kwd) {
		if (userId.contentEquals(kwd))
			return true;
		if (userPw.contentEquals(kwd))
			return true;

		if (name.contains(kwd))
			return true;
		if (phone.contains(kwd))
			return true;
		return false;
	}

	public boolean canLoan() { // 도서대출 가능 여부
		if (loanNum() >= bookNum[position]) {
			bookLoanAvailability = false;
			return false;
		} else if(overLoanNum() > 0) {
			return false;
		}
		else {
			bookLoanAvailability = true;
			return true;
		}
	}

	public List<BookLoan> getMyList() {
		ArrayList<BookLoan> myList = new ArrayList<>();
		for (Manageable m : Library.loanMgr.mList)
			if (m.matches(userId))
				myList.add((BookLoan) m);
		return myList;
	}

	public int findMaxIndex() {
		int indexLength = 10;
		int maxIndex = 0;
		for (int i = 0; i < indexLength; i++) {
			if (interested[i] > interested[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	@SuppressWarnings("null")
	public Object[] getUiDatas() {
		// TODO Auto-generated method stub
		Object[] ob = null;
		int i = 0;
		for (Book b : recommendBooklist) {
			b.getUiData();
			ob[i] = b;
			i++;
		}
		return ob;
	}

	public int loanNum() {
		int num = 0;
		BookLoan tmp;
		for (Manageable m : Library.loanMgr.mList) {
			tmp = (BookLoan) m;
			if (tmp.matches(userId) && !tmp.isReturn)
				num++;
		}
		return num;
	}

	public int canLoanNum() {
		if(overLoanNum()>0)
			return 0;
		else
			return bookNum[position] - loanNum();
	}

	public int overLoanNum() {
		int num = 0;
		BookLoan tmp;
		for (Manageable m : Library.loanMgr.mList) {
			tmp = (BookLoan) m;
			if (tmp.matches(userId) && tmp.overLoan())
				num++;
		}
		return num;
	}

	public ArrayList<Book> recommend() {
		ArrayList<Book> recommendBooklist = new ArrayList<>();
		int index = findMaxIndex();

		Book tmp = null;
		for (Manageable book : Library.bookMgr.mList) {
			tmp = (Book) book;
			if ((tmp.index / 100) == index) {
				recommendBooklist.add(tmp);
			}
		}
		Collections.sort(recommendBooklist, new Comparator<Book>() {
			public int compare(Book b1, Book b2) {
				Integer acnt = b1.count;
				Integer bcnt = b2.count;

				return bcnt.compareTo(acnt);
			}
		});
		return recommendBooklist;
	}
	
}
