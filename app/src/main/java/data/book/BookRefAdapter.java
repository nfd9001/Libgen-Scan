package data.book;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nfd.libgenscan.R;
import data.AppDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-24.
 */
public class BookRefAdapter extends RecyclerView.Adapter<BookRefAdapter.BRHolder> {
    List<BookRef> l;
    int selectedPosition = 0;
    private BookRefAdapterCallback c;

    public BookRefAdapter(Context context, List<BookRef> l) {
        try {
            c = ((BookRefAdapterCallback) context);
            this.l = l;
        } catch (ClassCastException e) {
            Log.i("LGS", "No callback registered in adapter.");
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public BRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View bookref = inflater.inflate(R.layout.bookref_view, parent, false);
        return new BRHolder(bookref);
    }

    @Override
    public void onBindViewHolder(@NonNull final BRHolder holder, final int position) {
        final BookRef b = l.get(position);
        holder.t.setText(b.id);
        if (b.opened) {
            holder.t.setBackgroundResource(R.drawable.background);
        } else {
            holder.t.setBackgroundResource(R.drawable.background_emphasized);
        }
        holder.t.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                if (!b.opened) {
                    b.opened = true;
                    holder.t.setBackgroundResource(R.drawable.background);
                    notifyItemChanged(position);
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    executor.execute(new FutureTask<Void>(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            AppDatabase.getInstance(view.getContext().getApplicationContext())
                                    .bookRefDao()
                                    .updateAll(b);
                            return null;
                        }
                    }));
                }
                c.handleBookRef(b);
            }
        });

    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public interface BookRefAdapterCallback {
        void handleBookRef(BookRef b);
    }

    public static class BRHolder extends RecyclerView.ViewHolder {
        TextView t;

        public BRHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.isbn_textview);
        }
    }
}

