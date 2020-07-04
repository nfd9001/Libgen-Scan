package data.book;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import data.provider.Provider;
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

    public long timestamp;

    public BookRef(String id, boolean opened) {
        this.id = id;
        this.opened = opened;
        Long ts = System.nanoTime(); //only useful for ordering the list, not for display
    }

    public void searchBook(Context context, Provider provider) {
        opened = true;
        String uri = String.format(provider.format, id);
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
    }
}
