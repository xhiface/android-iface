package xyz.zzyitj.iface.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author intent
 * @version 1.0
 * @date 2020/2/4 6:46 下午
 * @email zzy.main@gmail.com
 */
public class RetrofitHttpService {
    private static final long TIMEOUT = 10;

    private static RetrofitHttpService retrofitHttpService;

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = null;

    public static RetrofitHttpService instance(String baseUrl) {
        if (retrofitHttpService == null) {
            synchronized (RetrofitHttpService.class) {
                if (retrofitHttpService == null) {
                    retrofitHttpService = new RetrofitHttpService();
                    retrofit = createRetrofit(baseUrl);
                }
            }
        }
        return retrofitHttpService;
    }

    public static Retrofit createRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(client)
                .build();
    }

    public static Retrofit getRetrofit(String baseUrl) {
        if (retrofit == null) {
            RetrofitHttpService.instance(baseUrl);
        }
        return retrofit;
    }
}
