package com.nfd.libgenscan;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import data.AppDatabase;
import data.provider.Provider;
import data.provider.ProviderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
public class ProviderChoiceActivity extends AppCompatActivity {
    private RecyclerView rv;
    private RecyclerView.LayoutManager lm;
    List<Provider> providers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        rv = findViewById(R.id.provider_recycler);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        try {
            providers = refreshProviders().get(200, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.providerfetchfail),
                    Toast.LENGTH_SHORT).show();
            providers = new ArrayList<Provider>();
            e.printStackTrace();
        }
        rv.setAdapter(new ProviderAdapter(null)); //null placeholder
        rv.getAdapter().notifyDataSetChanged();


    }


    FutureTask<List<Provider>> refreshProviders() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<List<Provider>> t = new FutureTask<>(
                new Callable<List<Provider>>() {
                    @Override
                    public List<Provider> call() throws Exception {
                        return AppDatabase
                                .getInstance(getApplicationContext())
                                .providerDao()
                                .getAll();
                    }
                });
        executor.execute(t);
        return t;
    }
}
