package data;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import data.book.BookRef;
import data.book.BookRefDao;
import data.provider.Provider;
import data.provider.ProviderDao;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
@Database(entities = {Provider.class, BookRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context){
       if (instance == null) {
          instance = Room.databaseBuilder(context , AppDatabase.class, "appdb").build();
       }
       return instance;
    }
    public abstract ProviderDao providerDao();
    public abstract BookRefDao bookRefDao();
}
