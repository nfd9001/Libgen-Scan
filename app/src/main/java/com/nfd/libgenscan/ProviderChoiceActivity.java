package com.nfd.libgenscan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import data.AppDatabase;
import data.provider.Provider;
import data.provider.ProviderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle("Providers");

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
        rv.setAdapter(new ProviderAdapter(providers));
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.provider_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.provider_plus) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayoutCompat.VERTICAL);

            final EditText nameEditText = new EditText(this);
            nameEditText.setHint("Name");
            layout.addView(nameEditText, 0);

            final EditText formatEditText = new EditText(this);
            formatEditText.setHint("URI with \"%s\" in ISBN location");
            layout.addView(formatEditText, 1);

            builder.setView(layout);

            builder.setTitle("Add a new provider...");

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String name = nameEditText.getText().toString();
                    //boy howdy I hope I don't have someone file a bug report because Libgen adds
                    //a domain with emojis in the TLD or something
                    String format = formatEditText.getText().toString();

                    Provider p = new Provider(name, format);
                    if (p.name.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.name_required), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!p.validate()) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.bad_provider_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogInterface.dismiss();

                    addProvider(p);
                    refreshProviders();
                    try {
                        providers = refreshProviders().get(200, TimeUnit.MILLISECONDS);
                    } catch (ExecutionException | TimeoutException | InterruptedException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.providerfetchfail),
                                Toast.LENGTH_SHORT).show();
                        providers = new ArrayList<Provider>();
                        e.printStackTrace();
                    }
                    providers.add(p);
                    rv.getAdapter().notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), getString(R.string.mirroraddedsass),
                            Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
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

    FutureTask<Void> addProvider(final Provider p) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<Void> t = new FutureTask<>(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        AppDatabase
                                .getInstance(getApplicationContext())
                                .providerDao()
                                .insertAll(p);
                        return null;
                    }
                });
        executor.execute(t);
        return t;
    }
}
