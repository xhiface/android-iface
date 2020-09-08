package xyz.zzyitj.iface.api;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xyz.zzyitj.iface.model.Server;
import xyz.zzyitj.iface.model.Token;
import xyz.zzyitj.iface.util.ApiServerUtils;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 15:27
 * @since 1.0
 */
public class AuthService {
    private static final String TAG = AuthService.class.getSimpleName();

    public static Observable<Token> getToken(Server server) {
        AuthInterface request = ApiServerUtils.getRetrofit(server).create(AuthInterface.class);
        return request.getToken(ApiConst.GRAND_TYPE, ApiConst.API_KEY, ApiConst.SECRET_KET)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
