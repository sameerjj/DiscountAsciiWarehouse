package zerhal.discountasciiwarehouse;

/**
 * Created by sameer on 16-07-23.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ApiManager.initializeInstance(this);
    }
}
