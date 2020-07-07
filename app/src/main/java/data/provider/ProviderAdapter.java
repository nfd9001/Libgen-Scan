package data.provider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nfd.libgenscan.R;
import data.AppDatabase;
import data.DataHelpers;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-24.
 */
public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.PAHolder> {
    List<Provider> l;
    int selectedPosition = 0;

    public ProviderAdapter(List<Provider> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public PAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View provider = inflater.inflate(R.layout.provider_view, parent, false);
        return new PAHolder(provider);
    }

    @Override
    public void onBindViewHolder(@NonNull final PAHolder holder, final int position) {
        holder.p = l.get(position);
        holder.t.setText(holder.p.name);
        if (holder.p.selected) {
            holder.t.setBackgroundResource(R.drawable.background_emphasized);
            selectedPosition = position;
        } else {
            holder.t.setBackgroundResource(R.drawable.background);
        }
        holder.t.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                return true; //true so the event doesn't fall through to onClick
            }
        });
        holder.t.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                final RecyclerView rv = view.findViewById(R.id.provider_recycler);
                if (holder.p.selected) {
                    return;
                }
                try {
                    FutureTask<Void> t = new FutureTask<>(
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    DataHelpers.setDefault(AppDatabase.getInstance(
                                            view.getContext().getApplicationContext()).providerDao(),
                                            holder.p);
                                    return null;
                                }
                            }
                    );
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    executor.execute(t);
                    t.get(1, TimeUnit.SECONDS);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    Log.e("LGS", "Failed to set default providers with a " + e.getClass().getCanonicalName());
                }
                notifyItemChanged(position);
                l.get(selectedPosition).selected = false;
                notifyItemChanged(selectedPosition);
                selectedPosition = position;

            }
        });

    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public static class PAHolder extends RecyclerView.ViewHolder {
        Provider p;
        TextView t;

        public PAHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.provider_name_textview);
        }
    }
}
