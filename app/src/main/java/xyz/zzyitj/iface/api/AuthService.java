package xyz.zzyitj.iface.api;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 15:27
 * @since 1.0
 */
public class AuthService {
    /**
     * 获取token
     *
     * @return token
     */
    public static Call getToken() {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST+ApiConst.AUTH_GET_TOKEN))
                .newBuilder();
        urlBuilder.addQueryParameter("grant_type",ApiConst.AUTH_GRAND_TYPE);
        urlBuilder.addQueryParameter("client_id",ApiConst.AUTH_API_KEY);
        urlBuilder.addQueryParameter("client_secret",ApiConst.AUTH_SECRET_KET);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .get();
        return OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build());
    }
}
