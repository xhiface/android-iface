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
import xyz.zzyitj.iface.model.ApiClockDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 18:35
 * @since 1.0
 */
public class ApiClockService {
    private static final String TAG = ApiClockService.class.getSimpleName();

    public static Observable<ApiClockDto> addClock(String phoneNumber) {
        return Observable.create((ObservableEmitter<ApiClockDto> emitter) -> {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST + ApiConst.ADD_ATTEND))
                    .newBuilder();
            Request.Builder requestBuilder = new Request.Builder();
            Map<String, String> map = new HashMap<>();
            map.put("phoneNumber", phoneNumber);
            String data = new Gson().toJson(map);
            requestBuilder
                    .url(urlBuilder.build())
                    .addHeader("Connection", "close")
                    .post(RequestBody.create(MediaType.parse("application/json"), data));
            OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = Objects.requireNonNull(response.body()).string();
                    Log.d(TAG, "onResponse: " + body);
                    emitter.onNext(new Gson().fromJson(body, ApiClockDto.class));
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<ApiClockDto>> getAttendRecordList(String phoneNumber) {
        return Observable.create((ObservableEmitter<List<ApiClockDto>> emitter) -> {
            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST + ApiConst.USER_ATTEND_RECORD))
                    .newBuilder();
            Request.Builder requestBuilder = new Request.Builder();
            Map<String, String> map = new HashMap<>();
            map.put("phoneNumber", phoneNumber);
            String data = new Gson().toJson(map);
            requestBuilder
                    .url(urlBuilder.build())
                    .addHeader("Connection", "close")
                    .post(RequestBody.create(MediaType.parse("application/json"), data));
            OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    emitter.onError(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = Objects.requireNonNull(response.body()).string();
                    Log.d(TAG, "onResponse: " + body);
                    Type type = new TypeToken<List<ApiClockDto>>() {
                    }.getType();
                    emitter.onNext(new Gson().fromJson(body, type));
                    emitter.onComplete();
                }
            });
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
