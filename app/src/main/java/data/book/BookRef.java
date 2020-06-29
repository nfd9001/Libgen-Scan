package data.book;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import me.dm7.barcodescanner.zbar.BarcodeFormat;

@Entity
/**
 * @author Alexander Ronsse-Tucherov
 * @version 2016-08-26.
 *          Wrapper to hold a reference to some book; provided for options for future expansion (e.g. UPC manipulation)
 */
public class BookRef {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public Boolean opened;

    public BookRef(String id, boolean opened) {
        this.id = id;
        this.opened = opened;
    }

    public String getId() {
        return id;
    }

    public boolean isOpened() {
        return opened;
    }

    /*
    public static void addToList(BookRef b) {
        bookList.add(b);
    }

    //TODO: add more libraries, possibly ways of handling other types of barcodes (search by UPC?)
    public void searchBook() {
        opened = true;
        String uri = "http://libgen.io/search.php?req=" + id +
                "&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        parent.fire(browserIntent);
    }
     */
}
