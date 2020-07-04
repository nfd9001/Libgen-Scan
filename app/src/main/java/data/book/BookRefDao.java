package data.book;
import androidx.room.*;
import java.util.List;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
@Dao
public interface BookRefDao {
    @Query("select * from bookref order by timestamp desc")
    List<BookRef> getAll();

    @Query("select * from bookref where opened")
    List<BookRef> getOpened();

    @Query("select * from bookref where not opened")
    List<BookRef> getUnopened();

    @Query("select * from bookref where id like :id")
    BookRef findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(BookRef... bookRefs);

    @Delete
    void deleteBookRefs(BookRef... bookRefs);

    @Delete
    void deleteBookRefs(List<BookRef> bookRefs);

}
