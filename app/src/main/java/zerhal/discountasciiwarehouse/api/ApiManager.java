package zerhal.discountasciiwarehouse.api;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    public static synchronized ApiManager getInstance(){
        return sInstance;
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
            @SuppressLint("DefaultLocale")
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .header("Cache-Control", String.format( "max-age=%d, only-if-cached", CACHE_TIME_LIMIT_SECONDS))
                        .build();

            }
        };
    }

    public void getProductsRequest(int limit, int skip, String search, boolean onlyInStock, Callback callback){
        HttpUrl url = new HttpUrl.Builder()
                .host("http://74.50.59.155:5000/api/search")
                .addQueryParameter("limit", Integer.toString(limit))
                .addQueryParameter("skip", Integer.toString(skip))
                .addQueryParameter("q", search)
                .addQueryParameter("onlyInStock", Boolean.toString(onlyInStock))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        mOkHttpClient.newCall(request).enqueue(callback);
    }


}
