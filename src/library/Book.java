package library;

import java.util.Scanner;

import mgr.Manageable;
import mgr.UIdata;

public class Book implements Manageable, UIdata {
   public String isbn = null;
   public String title = null;
   public String author = null;
   String publisher = null;
   boolean bookLoan = false;
   int count = 0;
   int index = 0;

   public void read(Scanner scan) {
      isbn = scan.next();
      title = scan.nextLine();
      author = scan.next();
      publisher = scan.next();
      index = scan.nextInt();
      bookLoan = false;
   }

   public void print() {
      System.out.printf("[%s] %s ����: %s\n", isbn, title, author);
   }

   public boolean matches(String kwd) {
      if (isbn.contentEquals(kwd))
         return true;
      if (title.contains(kwd))
         return true;
      if (author.contains(kwd))
         return true;
      if (publisher.contains(kwd))
         return true;
      return false;
   }

   public Object[] getUiData() {
	      return new Object[] { title, author, publisher};
   }
   
   @Override
   public Object[] getUiDatas() {
      String bookLoanString;
      if (bookLoan)
         bookLoanString = "������";
      else
         bookLoanString = "��û��";
      return new Object[] { title, author, publisher, bookLoanString };
   }

   @Override
   public Object[] getUiTexts() {
	   String state;
	      if (bookLoan)
	         state = "����Ұ���";
	      else
	         state = "���Ⱑ��";
	      return new Object[] { title, author, publisher, state, false };
   }
   
   public boolean canLoan() {
	   return !bookLoan;
   }
}