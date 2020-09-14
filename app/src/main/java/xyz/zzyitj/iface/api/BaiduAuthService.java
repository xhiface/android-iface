package xyz.zzyitj.iface.api;

import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.model.BaiduTokenDto;

import java.io.IOException;
import java.util.Objects;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 15:27
 * @since 1.0
 */
public class BaiduAuthService {
    /**
     * 获取token
     *
     * @return token
     */
    public static Observable<BaiduTokenDto> getToken() {
        return Observable.create((ObservableEmitter<BaiduTokenDto> emitter) -> {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BaiduApiConst.HOST + BaiduApiConst.AUTH_GET_TOKEN))
                    .newBuilder();
            urlBuilder.addQueryParameter("grant_type", BaiduApiConst.AUTH_GRAND_TYPE);
            urlBuilder.addQueryParameter("client_id", BaiduApiConst.AUTH_API_KEY);
            urlBuilder.addQueryParameter("client_secret", BaiduApiConst.AUTH_SECRET_KET);
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder
                    .url(urlBuilder.build())
                    .addHeader("Connection", "close")
                    .get();
            OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = Objects.requireNonNull(response.body()).string();
                    BaiduTokenDto apiTokenDto = new Gson().fromJson(body, BaiduTokenDto.class);
                    emitter.onNext(apiTokenDto);
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
