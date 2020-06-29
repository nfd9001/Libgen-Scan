package data.provider;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.IllegalFormatException;


/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */

@Entity
public class Provider {
    @PrimaryKey
    @NonNull
    public String name;

    @NonNull
    public String format;

    public Provider(String name, String format){
        this.name = name;
        this.format = format;
    }

    //Make sure to validate Providers before use.
    public boolean validate(){
        try{
            String.format(format, "0262510871");
        }
        catch (IllegalFormatException e){
            return false;
        }
        return true;
    }
}
