package data.book;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import data.provider.Provider;

@Entity
/**
 * @author Alexander Ronsse-Tucherov
 * @version 2016-08-26.
 *          Wrapper to hold a reference to some book; provided for options for future expansion (e.g. UPC manipulation)
 */
public class BookRef implements Parcelable {
    @PrimaryKey
    @NonNull
    public String id;

    @NonNull
    public Boolean opened;

    public long timestamp;

    @Ignore
    public static final Parcelable.Creator<BookRef> CREATOR = new Parcelable.Creator<BookRef>() {

        @Override
        public BookRef createFromParcel(Parcel parcel) {
            String s = parcel.readString();
            int btmp = parcel.readInt();
            long l = parcel.readLong();
            boolean b = btmp == 1;
            BookRef br = new BookRef(s, b);
            br.timestamp = l;
            return br;
        }

        @Override
        public BookRef[] newArray(int i) {
            return new BookRef[0];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        //readBoolean requires Q+ for some bizarre reason
        parcel.writeInt((opened) ? 1 : 0);
        parcel.writeLong(timestamp);
    }

}
