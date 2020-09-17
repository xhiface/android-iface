package xyz.zzyitj.iface.api;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:28
 * @since 1.0
 */
public class BaiduFaceService {
    private static final String TAG = BaiduFaceService.class.getSimpleName();
    public static Observable<BaiduResponseBody<BaiduFaceUserAddDto>> addUser(String accessToken,
                                                                             BaiduFaceUserAddVo userAddDo) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BaiduApiConst.HOST + BaiduApiConst.FACE_ADD_USER))
                .newBuilder();
        urlBuilder.addQueryParameter("access_token", accessToken);
        String data = new Gson().toJson(userAddDo);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .post(RequestBody.create(MediaType.parse("application/json"), data));
        return Observable.create((ObservableEmitter<BaiduResponseBody<BaiduFaceUserAddDto>> emitter) -> {
            OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String b = Objects.requireNonNull(response.body()).string();
                    Log.d(TAG, "onResponse: " + b);
                    Type type = new TypeToken<BaiduResponseBody<BaiduFaceUserAddDto>>() {
                    }.getType();
                    BaiduResponseBody<BaiduFaceUserAddDto> responseBody = new Gson().fromJson(b, type);
                    emitter.onNext(responseBody);
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BaiduResponseBody<BaiduFaceSearchDto>> searchUser(String accessToken, BaiduFaceSearchVo userSearchDo) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BaiduApiConst.HOST + BaiduApiConst.FACE_SEARCH_USER))
                .newBuilder();
        urlBuilder.addQueryParameter("access_token", accessToken);
        String data = new Gson().toJson(userSearchDo);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .post(RequestBody.create(MediaType.parse("application/json"), data));
        return Observable.create((ObservableEmitter<BaiduResponseBody<BaiduFaceSearchDto>> emitter) -> {
            OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String b = Objects.requireNonNull(response.body()).string();
                    Log.d(TAG, "onResponse: " + b);
                    Type type = new TypeToken<BaiduResponseBody<BaiduFaceSearchDto>>() {
                    }.getType();
                    BaiduResponseBody<BaiduFaceSearchDto> responseBody = new Gson().fromJson(b, type);
                    emitter.onNext(responseBody);
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
