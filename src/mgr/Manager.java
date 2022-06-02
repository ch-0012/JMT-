package mgr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import library.Book;

public class Manager {
	public ArrayList<Manageable> mList = new ArrayList<>();

	public void readAll(String filename, Factory fac) {
		Scanner filein = openFile(filename);
		Manageable m = null;
		while (filein.hasNext()) {
			m = fac.create();
			m.read(filein);
			mList.add(m);
		}
		filein.close();
	}

	public void printAll(String txt) {
		System.out.printf("[%s]\n", txt);
		for (Manageable m : mList)
			m.print();
	}

	public Manageable find(String kwd) {
		for (Manageable m : mList) {
			if (m.matches(kwd))
				return m;
		}
		return null;
	}
	
	public List<Manageable> findAll(String kwd) {
		List<Manageable> results = new ArrayList<>();
		for (Manageable m: mList)
			if (m.matches(kwd))
				results.add(m);
		return results;
	}

	public Scanner openFile(String filename) {
		Scanner filein = null;
		try {
			filein = new Scanner(new File(filename));
		} catch (IOException e) {
			System.out.println("파일 오픈 실패 " + filename);
			System.exit(0);
		}
		return filein;
	}

	public ArrayList<Manageable> list() {
		ArrayList<Manageable> myList = mList;
		return myList;
	}

	public void search(Scanner scan) {
		String kwd;
		while (true) {
			System.out.print("검색키워드: ");
			kwd = scan.next();
			if (kwd.equals("end"))
				break;
			for (Manageable m : mList) {
				if (m.matches(kwd))
					m.print();
			}
		}
	}
	
	public void delete(Scanner scan) {
		while (true) {
			System.out.print("삭제할 키워드: ");
			String kwd = scan.next();
			if (kwd.contentEquals("end"))
				break;
			Manageable m = find(kwd);
			mList.remove(m);
		}
	}
	
	public void add(Manageable m) {
		mList.add(m);
	}
}
