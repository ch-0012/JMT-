package library;

import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

import mgr.Manageable;
import mgr.UIdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookLoan implements Manageable, UIdata {
	Scanner scan = new Scanner(System.in);
	Calendar cal = Calendar.getInstance();

	User user;
	Book book;
	Date date;
	Date reDate;
	Date time;
	public boolean isReturn = true; // 반납됨: true 반납안됨: false
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public BookLoan() {

	}

	public BookLoan(Book book, User user, String date, String time) {
		this.user = user;
		this.book = book;
		try {
			this.date = dateFormat.parse(date);
			this.time = timeFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(this.date);
		cal.add(Calendar.DATE, user.bookLoanDate[user.position]);
		reDate = new Date(cal.getTimeInMillis());
		book.bookLoan = true; // 책상태를 빌린것으로 수정
		book.count++;
		user.interested[book.index / 100]++;
		isReturn = false;

		fileWrite();
	}

	void returnWrite() {
		String text = book.isbn + " " + user.userId + " " + dateFormat.format(date) + " " + timeFormat.format(time);
		Date todate = new Date();
		String today = dateFormat.format(todate);
		String memo = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("bookLoan.txt")));
			String line;
			line = br.readLine();
			while(line != null) {
	            if(line.contains(text))
	               memo += (text +  " " + today + "\r\n");
	            else
	               memo += (line + "\r\n");
	            line = br.readLine();
	         }
			FileWriter fw = new FileWriter("bookLoan.txt", false);
			fw.write(memo);
			fw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void fileWrite() {
		FileWriter fout = null;
		String line = book.isbn + " " + user.userId + " " + dateFormat.format(date) + " " + timeFormat.format(time)
				+ " " + "0" + "\n";
		try {
			fout = new FileWriter("bookLoan.txt", true);
			fout.write(line);
			fout.close();
		} catch (IOException e) {
			System.out.println("입출력 오류");
		}
	}

	void modify(Book b) {
		Boolean tmp = false;
		b.bookLoan = tmp;
	}

	public void print() {
		Date today = new Date();
		if (reDate.before(today)) // 반납 완료: 오늘을 기준으로 reDate가 과거인 경우
			System.out.printf("\t[%s] %s \n\t\t날짜: %s ~ %s [반납 완료]\n", book.isbn, book.title, dateFormat.format(date),
					dateFormat.format(reDate));
		else if (date.before(today) && reDate.after(today)) // 반납 예정: 오늘을 기준으로 date가 과거이고 reDate가 미래인 경우
			System.out.printf("\t[%s] %s \n\t\t날짜: %s ~ %s\n", book.isbn, book.title, dateFormat.format(date),
					dateFormat.format(reDate));
		else if (date.after(today)) // 대출 예정: 오늘을 기준으로 date가 미래인 경우
			System.out.printf("\t[%s] %s \n\t\t날짜: %s ~ %s 대출 시간: %s\n", book.isbn, book.title, dateFormat.format(date),
					dateFormat.format(reDate), timeFormat.format(time));
	}

	@Override
	public void read(Scanner scan) {
		String tmp = scan.next();
		book = (Book) Library.bookMgr.find(tmp);
		tmp = scan.next();
		user = (User) Library.userMgr.find(tmp);
		String strDate = scan.next();
		String strTime = scan.next();
		String strreDate = scan.next();

		try {
			date = dateFormat.parse(strDate);
			time = timeFormat.parse(strTime);
			if (strreDate.contentEquals("0")) {
				isReturn = false;
				book.bookLoan = true;
				cal.setTime(date);
				cal.add(Calendar.DATE, user.bookLoanDate[user.position]);
				reDate = new Date(cal.getTimeInMillis());
			} else {
				reDate = dateFormat.parse(strreDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		book.count++;
		user.interested[book.index / 100]++;
	}

	@Override
	public boolean matches(String kwd) {
		if (book.isbn.contentEquals(kwd))
			return true;
		if (user.userId.contentEquals(kwd))
			return true;
		try {
			Date tmp = dateFormat.parse(kwd);
			cal.setTime(tmp);
			cal.add(Calendar.DATE, 7);
			Date periodDate = new Date(cal.getTimeInMillis());
			if (tmp.compareTo(date) <= 0 && date.compareTo(periodDate) <= 0)
				return true;
		} catch (ParseException e) {
		}
		return false;
	}

	public void changeBookLoanState() {
		book.bookLoan = false;
		reDate = new Date();
		isReturn = true;
	}

	public boolean overLoan() {
		Date today = new Date();
		if (reDate.before(today) && !isReturn)
			return true;
		return false;
	}

	@Override
	public Object[] getUiTexts() {
		String state;
		Date today = new Date();
		if (date.before(today) && reDate.after(today))
			state = "대출 중";
		else if (overLoan())
			state = "연체";
		else if (reDate.before(today))
			state = "반납 완료";
		else
			state = "대출 신청";

		String[] texts = new String[5];
		texts[0] = book.title;
		texts[1] = book.author;
		texts[2] = dateFormat.format(date);
		texts[3] = dateFormat.format(reDate);
		texts[4] = state;
		return texts;
	}

	@Override
	public Object[] getUiDatas() {
		// TODO Auto-generated method stub
		return null;
	}
}
