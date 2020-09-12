package xyz.zzyitj.iface.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.model.ApiFaceUserAddDto;
import xyz.zzyitj.iface.model.ApiResponseBody;

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
public class FaceService {
    public static void addUser(String accessToken,
                               ApiFaceUserAddDo userAddDo, ApiResponseCall<ApiResponseBody<ApiFaceUserAddDto>> responseCall) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST + ApiConst.FACE_ADD_USER))
                .newBuilder();
        urlBuilder.addQueryParameter("access_token", accessToken);
        String data = new Gson().toJson(userAddDo);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .post(RequestBody.create(MediaType.parse("application/json"), data));
        OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                responseCall.onError(call, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String b = Objects.requireNonNull(response.body()).string();
                Type type = new TypeToken<ApiResponseBody<ApiFaceUserAddDto>>() {
                }.getType();
                ApiResponseBody<ApiFaceUserAddDto> responseBody = new Gson().fromJson(b, type);
                responseCall.onSuccess(call, responseBody);
            }
        });
    }
}
