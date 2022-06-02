package library;

import java.util.ArrayList;
import java.util.List;

import mgr.DataEngineInterface;
import mgr.Manageable;

public class BestMgr implements DataEngineInterface {

   private static BestMgr mgr = null;

   private BestMgr() {
   }

   public static BestMgr getInstace() {
      if (mgr == null) {
         mgr = new BestMgr();
      }
      return mgr;
   }

   List<Book> bestList;
   private String[] headers = { "제목", "저자", "출판사", "대출가능여부" };

   @Override
   public int getColumnCount() {
      // TODO Auto-generated method stub
      return 4;
   }

   @Override
   public String[] getColumnNames() {
      // TODO Auto-generated method stub
      return headers;
   }

   public List<Manageable> search(String kwd) {
      List<Manageable> result = new ArrayList();
      List<Manageable> tmplist1 = new ArrayList<>();
      ArrayList<Book> tmplist2 = new ArrayList<>();
      tmplist2 = Library.sortList();
      tmplist1 = (List) tmplist2;
      Book tmp;
      int num = 0;

      if (("").contentEquals(kwd)) {
         for (int i = 0; num < 10; i++) {
            Manageable book = tmplist2.get(i);
            result.add(book);
            num++;
         }
      }

      else {
         for (int i = 0; num < 10; i++) {
            Manageable book = tmplist1.get(i);
            tmp = (Book) book;
            int index = ((tmp.index) / 100);

            if ((index + "").contentEquals(kwd)) {
               result.add(tmp);
               num++;
            }
         }
      }

      return result;
   }

   @Override
   public String getType() {
      return "BestBookMgr";
   }

}