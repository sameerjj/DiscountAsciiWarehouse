package zerhal.discountasciiwarehouse;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by sameer on 16-07-23.
 */
public class ApiManager {
    public static long CACHE_SIZE = 5 * 1024 * 1024; //5mb
    public static int CACHE_TIME_LIMIT_SECONDS = 60 * 60; //1 hour
    static final String CACHE_NAME = "daw-cache";

    private static ApiManager sInstance;

    private Context mContext;
    private OkHttpClient mOkHttpClient;

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ApiManager();
            sInstance.createOkHttpClient();
        }
    }

    private void createOkHttpClient(){
        sInstance.mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(cacheControlInterceptor())
                .cache(createCache())
                .build();
    }

    private Cache createCache() {
        File cache = new File(mContext.getApplicationContext().getCacheDir(), CACHE_NAME);
        if (!cache.exists()) {
            //noinspection ResultOfMethodCallIgnored
            cache.mkdirs();
        }
        return new Cache(cache, CACHE_SIZE);
    }

    private Interceptor cacheControlInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .header("Cache-Control", String.format("max-age=%d, only-if-cached", CACHE_TIME_LIMIT_SECONDS))
                        .build();

            }
        };
    }


}
