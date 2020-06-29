package data.provider;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-24.
 */
public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.PAHolder> {
    @NonNull
    @Override
    public PAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PAHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class PAHolder extends RecyclerView.ViewHolder{
        public Provider provider;
        public PAHolder(Provider )

    }
}
