package pan.lib.baseandroidframework;
import pan.lib.baseandroidframework.Book;
import pan.lib.baseandroidframework.IOnNewBookArrivedListener;


interface IBookManager{
     List<Book> getBookList();
     void addBook(in Book book);
     void registerListener(IOnNewBookArrivedListener listener);
     void unregisterListener(IOnNewBookArrivedListener listener);
}