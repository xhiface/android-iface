package xyz.zzyitj.iface.api;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.model.ApiFaceUserAddDto;
import xyz.zzyitj.iface.model.ApiResponseBody;
import xyz.zzyitj.iface.model.Server;
import xyz.zzyitj.iface.util.ApiServerUtils;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:28
 * @since 1.0
 */
public class FaceService {
    public static Observable<ApiResponseBody<ApiFaceUserAddDto>> addUser(Server server,
                                                                         String accessToken,
                                                                         ApiFaceUserAddDo userAddDo) {
        FaceInterface request = ApiServerUtils.getRetrofit(server).create(FaceInterface.class);
        return request.addUser(accessToken, userAddDo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
