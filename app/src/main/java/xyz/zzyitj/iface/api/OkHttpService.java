package xyz.zzyitj.iface.api;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author intent
 * @version 1.0
 * @date 2020/2/4 6:46 下午
 * @email zzy.main@gmail.com
 */
public class OkHttpService {
    private static volatile OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            //连接超时
                            .connectTimeout(BaiduApiConst.DEFAULT_HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            //读取超时
                            .readTimeout(BaiduApiConst.DEFAULT_HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                            //写超时
                            .writeTimeout(BaiduApiConst.DEFAULT_HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                    okHttpClient.dispatcher().setMaxRequests(128);
                    okHttpClient.dispatcher().setMaxRequestsPerHost(12);
                }
            }
        }
        return okHttpClient;
    }

}
