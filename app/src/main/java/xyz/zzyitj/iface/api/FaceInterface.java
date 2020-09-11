package xyz.zzyitj.iface.api;

import io.reactivex.Observable;
import retrofit2.http.*;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.model.ApiFaceUserAddDto;
import xyz.zzyitj.iface.model.ApiResponseBody;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:18
 * @since 1.0
 */
public interface FaceInterface {
    /**
     * 在人脸库添加用户
     *
     * @param accessToken token
     * @return state
     */
    @POST(value = ApiConst.FACE_ADD_USER)
    Observable<ApiResponseBody<ApiFaceUserAddDto>> addUser(
            @Query("access_token") String accessToken,
            @Body ApiFaceUserAddDo userAddDo);
}
