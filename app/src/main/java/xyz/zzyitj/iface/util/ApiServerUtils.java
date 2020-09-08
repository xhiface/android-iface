package xyz.zzyitj.iface.util;

import retrofit2.Retrofit;
import xyz.zzyitj.iface.api.RetrofitHttpService;
import xyz.zzyitj.iface.model.Server;

/**
 * @author intent
 * @version 1.0
 * @date 2020/2/10 7:45 下午
 * @email zzy.main@gmail.com
 */
public class ApiServerUtils {

    public static Retrofit getRetrofit(Server server) {
        if (server.isHttps()) {
            return RetrofitHttpService.getRetrofit(
                    "https://" + server.getHost() + ":" + server.getPort());
        }
        return RetrofitHttpService.getRetrofit(
                "http://" + server.getHost() + ":" + server.getPort());
    }
}
