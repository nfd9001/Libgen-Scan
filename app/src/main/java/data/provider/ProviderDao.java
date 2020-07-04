package data.provider;
import androidx.room.*;
import java.util.List;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
@Dao
public interface ProviderDao {
    @Query("select * from provider")
    List<Provider> getAll();

    @Query("select * from provider where name like :name")
    Provider findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Provider... providers);

    @Delete
    void deleteAll(Provider... providers);

    @Update
    void updateProviders(Provider... providers);

    @Update
    void updateProviders(List<Provider> providers);


    @Query("select * from provider where selected")
    List<Provider> findSelected();


}
