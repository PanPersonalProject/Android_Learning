package pan.lib.baseandroidframework;
import pan.lib.baseandroidframework.Book;

interface IBookManager{
     List<Book> getBookList();
     void addBook(in Book book);
}