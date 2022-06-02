package library;

import java.util.Scanner;

public class Admin {
	private String name = "강하늘";
	private String adminId = "201012345";
	private String adminPw = "12345";
	Scanner scan = new Scanner(System.in);

	public void read(Scanner scan) {
		name = scan.next();
		adminId = scan.next();
		adminPw = scan.next();
	}

	public void print() {
		System.out.printf("%s %s \n\t아이디: %s\n\t비밀번호: %s\n", name, adminId, adminPw);
	}

	void modify(Scanner scan) {
		scan.nextLine();
		System.out.println("[관리자] 수정할 내용을 입력하세요");
		System.out.printf("아이디 : %s", adminId);
		String tmp = scan.nextLine();
		if (tmp.length() > 0)
			adminId = tmp;
		System.out.printf("비밀번호 : %s", adminPw);
		tmp = scan.nextLine();
		if (tmp.length() > 0)
			adminPw = tmp;
	}

	public boolean matches(String kwd) {
		if (adminId.contentEquals(kwd))
			return true;
		if (adminPw.contentEquals(kwd))
			return true;
		return false;
	}
}
