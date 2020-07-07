package data.provider;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.IllegalFormatException;


/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */

@Entity
public class Provider implements Parcelable {
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    public String format;

    public boolean selected;

    @Ignore
    public static final Parcelable.Creator<Provider> CREATOR = new Parcelable.Creator<Provider>() {

        @Override
        public Provider createFromParcel(Parcel parcel) {
            String name = parcel.readString();
            String format = parcel.readString();
            Provider p = new Provider(name, format);
            p.selected = (parcel.readInt() == 1);
            return p;
        }

        @Override
        public Provider[] newArray(int i) {
            return new Provider[0];
        }
    };


    public Provider(String name, String format) {
        this.name = name;
        this.format = format;
        selected = false;
    }

    //Make sure to validate Providers before use.
    public boolean validate() {
        try {
            String s = String.format(format, "978-3-16-148410-0");
            if (!s.contains("978-3-16-148410-0")) {
                return false;
            }
        } catch (IllegalFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(format);
        //readBoolean requires Q+ for some bizarre reason
        parcel.writeInt((selected) ? 1 : 0);
    }
}
