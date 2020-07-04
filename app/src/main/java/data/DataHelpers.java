package data;

import android.util.Log;
import data.provider.Provider;
import data.provider.ProviderDao;

import java.util.List;

public class DataHelpers {
    //the elegant way to do this would be to add a default method to these DAOs, which is unfortunately unsupported
    //in old versions of Android

    //these should be used async, like anything that touches the DAOs
    static void setDefault(ProviderDao d, Provider p) {
        List<Provider> ppl = d.findSelected();
        if ((ppl.size() >= 2)) {
            Log.w("LGS", "Multiple providers selected??");
        }
        for (Provider pp : ppl) {
            pp.selected = false;
        }
        d.updateProviders(ppl);
        p.selected = true;
        d.updateProviders(p);
    }
}
