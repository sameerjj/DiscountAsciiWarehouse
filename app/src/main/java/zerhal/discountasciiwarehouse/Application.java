package zerhal.discountasciiwarehouse;

import zerhal.discountasciiwarehouse.api.ApiManager;

/**
 * Created by sameer on 16-07-23.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //init singleton
        ApiManager.initializeInstance(this);
    }
}
