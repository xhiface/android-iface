package xyz.zzyitj.iface;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import xyz.zzyitj.iface.api.BaiduApiConst;
import xyz.zzyitj.iface.model.BaiduTokenDto;

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

    /**
     * access_token的有效期为30天，切记需要每30天进行定期更换，或者每次请求都拉取新token
     */
    private String apiToken;

    public IFaceApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    /**
     * 设置API本地存储，SharePreface方式
     *
     * @param key   key
     * @param value value
     * @param <T>   类型
     */
    public <T> void putApiLocalStorage(String key, T value) {
        Log.d(TAG, "put: " + key + ", value: " + value);
        SharedPreferences sp = this.getSharedPreferences(BaiduApiConst.SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

    /**
     * 获取本地存储的数据
     *
     * @param key    key
     * @param tClass 类型
     * @param <T>    类型
     * @return 数据
     */
    public <T> Object getApiLocalStorage(String key, Class<T> tClass) {
        SharedPreferences sp = this.getSharedPreferences(BaiduApiConst.SHARED_PREFS_NAME, MODE_PRIVATE);
        if (tClass == Long.class) {
            return sp.getLong(key, -1);
        }
        return sp.getString(key, null);
    }

    /**
     * 根据key移除本地数据
     *
     * @param key key
     */
    public void removeApiLocalStorage(String key) {
        SharedPreferences sp = this.getSharedPreferences(BaiduApiConst.SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
    }

    /**
     * 检查本地是否存储了token
     * 如果有token则检查token过期时间
     * 如果没有token则返回null
     *
     * @return apiToken
     */
    public String getApiToken() {
        if (apiToken == null) {
            String tempToken = (String) getApiLocalStorage(BaiduApiConst.SHARED_PREFS_TOKEN, String.class);
            if (tempToken == null) {
                return null;
            } else {
                long nowTime = System.currentTimeMillis();
                long expiresTime = (long) getApiLocalStorage(BaiduApiConst.SHARED_PREFS_TOKEN_EXPIRES, Long.class);
                // 检查当前时间是否超过token过期时间
                if (nowTime > expiresTime) {
                    return null;
                }
                apiToken = tempToken;
                return apiToken;
            }
        }
        return apiToken;
    }

    /**
     * 设置token和token过期时间
     *
     * @param apiToken apiToken
     */
    public void setApiToken(BaiduTokenDto apiToken) {
        this.apiToken = apiToken.getAccessToken();
        putApiLocalStorage(BaiduApiConst.SHARED_PREFS_TOKEN, apiToken.getAccessToken());
        // 构造过期时间
        long expiresTime = System.currentTimeMillis() + (apiToken.getExpiresIn() * 1000);
        putApiLocalStorage(BaiduApiConst.SHARED_PREFS_TOKEN_EXPIRES, expiresTime);
    }
}
