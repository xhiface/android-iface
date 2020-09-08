package xyz.zzyitj.iface;

import android.app.Application;
import android.util.Log;
import xyz.zzyitj.iface.model.Server;

/**
 * xyz.zzyitj.iface
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 14:51
 * @since 1.0
 */
public class IFaceApplication extends Application {
    private static final String TAG = IFaceApplication.class.getSimpleName();
    public static IFaceApplication instance;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token
     */
    private String token;
    private Server server;

    public IFaceApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }
}
