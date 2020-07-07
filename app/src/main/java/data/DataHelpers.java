package data;

import android.content.Context;
import android.util.Log;
import data.provider.Provider;
import data.provider.ProviderDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DataHelpers {
    //the elegant way to do this would be to add a default method to these DAOs, which is unfortunately unsupported
    //in old versions of Android
    //this should be used async, like anything that touches the DAOs, and this should be the only way we change the default
    public static void setDefault(ProviderDao d, Provider p) {
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

    //hardcoding this for the time being, but it might not be the worst idea to add some sort of fetchable manifest here
    public static void addDefaultProviders(final ProviderDao d) {
        final List<Provider> ps = new ArrayList<>();
        ps.add(new Provider("gen.lib.rus.ec",
                "http://gen.lib.rus.ec/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("LibGen Iceland",
                "http://libgen.is/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("Libgen Seychelles Direct",
                "http://93.174.95.27/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("LibGen ST",
                "http://libgen.st/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("LibGen LC",
                "https://libgen.lc/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("LibGen LI",
                "https://libgen.li/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("LibGen Switzerland Direct",
                "http://185.39.10.101/search.php?req=%s&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier"));
        ps.add(new Provider("Z-Library", "https://b-ok.cc/s/%s"));

        try {
            FutureTask<Void> t = new FutureTask<>(
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Log.i("LGS", "Got DAO.");
                            d.insertAll(ps);
                            Log.i("LGS", "Added default providers");
                            setDefault(d, ps.get(0));
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
    }
}
