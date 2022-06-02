package library;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import mgr.Factory;
import mgr.Manageable;
import mgr.Manager;

public class Library {
	Scanner scan = new Scanner(System.in);
	boolean log = false;
	private static Library lib = null;

	private Library() {
	}

	public static Library getInstance() {
		if (lib == null)
			lib = new Library();
		return lib;
	}

	static Admin admin = new Admin();
	public static Manager userMgr = new Manager();
	public static Manager bookMgr = new Manager();
	public static Manager loanMgr = new Manager();
	public Basket basket;

	public User login_user = null;
	Admin login_admin = null;

	public void run() {
		readAll();
	}

	void readAll() {
		bookMgr.readAll("book.txt", new Factory() {
			public Manageable create() {
				return new Book();
			}
		});

		userMgr.readAll("user.txt", new Factory() {
			public Manageable create() {
				return new User();
			}
		});

		loanMgr.readAll("bookLoan.txt", new Factory() {
			public Manageable create() {
				return new BookLoan();
			}
		});
	}

	void printAll() {
		bookMgr.printAll("����");
		userMgr.printAll("�̿���");
	}

	Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (IOException e) {
			System.out.println("���� ���� ���� " + filename);
			System.exit(0);
		}
		return filein;
	}

	public static ArrayList<Book> sortList() {
		ArrayList<Book> sortlist = new ArrayList<>();
		Book tmp = null;
		for (Manageable book : Library.bookMgr.mList) {
			tmp = (Book) book;
			sortlist.add(tmp);
		}
		Collections.sort(sortlist, new Comparator<Book>() {
			public int compare(Book b1, Book b2) {
				Integer acnt = b1.count;
				Integer bcnt = b2.count;

				return bcnt.compareTo(acnt);
			}
		});
		return sortlist;
	}

	public boolean userLogin(String id, String pw) {
		for (Manageable user : userMgr.mList) {
			if (user.matches(id) && user.matches(pw)) {
				log = true;
				login_user = (User) user;
				basket = new Basket();
				login_user.basket = basket;
				return true;
			}
		}
		return false;
	}

	public void logout() {
		System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
		log = false;
		basket = null;
		login_user = null;
		login_admin = null;
	}

	// ���⼭���� ������ ���
	public void adminMenu() {
		while (true) {
			System.out.print("[������ �޴�]\n1.����� �� ���� ��� �˻� 2.��¥ �˻� 3.�ݳ� 4.���� ");
			int num = scan.nextInt();
			switch (num) {
			case 1:
				System.out.print("[����� �� ���� ��� �˻�]\n");
				loanMgr.search(scan);
				break;
			case 2:
				System.out.print("[��¥ �˻�]\n");
				loanMgr.search(scan);
				break;
			case 3:
				System.out.println("[�ݳ�]");
				bookReturn();
				break;
			case 4:
				return;
			}
		}
	}

	// �ݳ��ϸ� BookLoan ���������Ͽ� �ִ� reDate �ٲ� �� �ֵ��� �ؾ���.
	void bookReturn() {
		System.out.print("�ݳ��� å�� isbn: ");
		String kwd = scan.next();
		System.out.print("�ݳ��Ͻðڽ��ϱ�?(y or n): ");
		BookLoan tmp = null;
		if (scan.next().contentEquals("y"))
			for (Manageable l : loanMgr.mList)
				if (l.matches(kwd))
					tmp = (BookLoan) l;
		tmp.changeBookLoanState();
		tmp.returnWrite();
		System.out.println("�ݳ� �Ϸ�Ǿ����ϴ�.");
	}

	public boolean adminLogin(String id, String pw) {
		if (admin.matches(id) && admin.matches(pw))
			return true;
		return false;
	}

	public static void main(String[] args) {
		Library library = new Library();
		library.run();
	}
}