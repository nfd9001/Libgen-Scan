package data.provider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nfd.libgenscan.R;

import java.util.List;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-24.
 */
public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.PAHolder> {
    List<Provider> l;

    public ProviderAdapter(List<Provider> l) {
        this.l = l;
    }

    @NonNull
    @Override
    public PAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View provider = inflater.inflate(R.layout.provider_view, parent, false);
        return new PAHolder((provider));
    }

    @Override
    public void onBindViewHolder(@NonNull PAHolder holder, int position) {
        holder.p = l.get(position);
        holder.t.setText(holder.p.name);
        holder.t.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                return true; //true so the event doesn't fall through to onClick
            }
        });
        holder.t.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

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
            t = itemView.findViewById(R.id.isbn_textview);
        }
    }
}
