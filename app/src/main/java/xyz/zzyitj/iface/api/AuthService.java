package xyz.zzyitj.iface.api;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.model.ApiTokenDto;

import java.io.IOException;
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
    public static void getToken(ApiResponseCall<ApiTokenDto> responseCall) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST + ApiConst.AUTH_GET_TOKEN))
                .newBuilder();
        urlBuilder.addQueryParameter("grant_type", ApiConst.AUTH_GRAND_TYPE);
        urlBuilder.addQueryParameter("client_id", ApiConst.AUTH_API_KEY);
        urlBuilder.addQueryParameter("client_secret", ApiConst.AUTH_SECRET_KET);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .get();
        OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                responseCall.onError(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = Objects.requireNonNull(response.body()).string();
                ApiTokenDto apiTokenDto = new Gson().fromJson(body, ApiTokenDto.class);
                responseCall.onSuccess(call, apiTokenDto);
            }
        });
    }
}
