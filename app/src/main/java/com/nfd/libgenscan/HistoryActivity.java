package com.nfd.libgenscan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import data.AppDatabase;
import data.book.BookRef;
import data.book.BookRefAdapter;
import data.provider.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class HistoryActivity extends AppCompatActivity implements BookRefAdapter.BookRefAdapterCallback {
    Provider p;
    List<BookRef> bookRefs;
    private RecyclerView rv;
    private RecyclerView.LayoutManager lm;

    @Override
    public void handleBookRef(BookRef b) {
        if (p == null) {
            //toast
            Toast.makeText(getApplicationContext(),
                    getString(R.string.providerfetchfail), Toast.LENGTH_SHORT).show();
            return;
        }
        b.searchBook(this, p);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = getIntent().getParcelableExtra("currentProvider");
        if (p == null) {
            Log.d("LGS", "HistoryActivity got no Provider");
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle("History");

        setContentView(R.layout.activity_history);
        rv = findViewById(R.id.bookref_recycler);
        rv.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        try {
            bookRefs = refreshBookRefs().get(200, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.providerfetchfail),
                    Toast.LENGTH_SHORT).show();
            bookRefs = new ArrayList<>();
            e.printStackTrace();
        }
        rv.setAdapter(new BookRefAdapter(this, bookRefs));
        rv.getAdapter().notifyDataSetChanged();
    }

    FutureTask<List<BookRef>> refreshBookRefs() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<List<BookRef>> t = new FutureTask<>(
                new Callable<List<BookRef>>() {
                    @Override
                    public List<BookRef> call() throws Exception {
                        return AppDatabase
                                .getInstance(getApplicationContext())
                                .bookRefDao()
                                .getAll();
                    }
                });
        executor.execute(t);
        return t;
    }

    FutureTask<Void> dropBookRefs() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        FutureTask<Void> t = new FutureTask<>(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        AppDatabase
                                .getInstance(getApplicationContext())
                                .bookRefDao()
                                .dropBookRefs();
                        return null;
                    }
                });
        executor.execute(t);
        return t;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.history_clear) {
            new AlertDialog.Builder(this)
                    .setTitle("Clear History?")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dropBookRefs();
                            bookRefs.clear();
                            rv.getAdapter().notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
