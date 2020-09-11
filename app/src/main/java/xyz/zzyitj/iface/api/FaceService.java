package xyz.zzyitj.iface.api;

import com.google.gson.Gson;
import okhttp3.*;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;

import java.util.Objects;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:28
 * @since 1.0
 */
public class FaceService {
    public static Call addUser(String accessToken,
                               ApiFaceUserAddDo userAddDo) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(ApiConst.HOST + ApiConst.FACE_ADD_USER))
                .newBuilder();
        urlBuilder.addQueryParameter("access_token", accessToken);
        String data = new Gson().toJson(userAddDo);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder
                .url(urlBuilder.build())
                .addHeader("Connection", "close")
                .post(RequestBody.create(MediaType.parse("application/json"), data));
        return OkHttpService.getOkHttpClientInstance().newCall(requestBuilder.build());
    }
}
