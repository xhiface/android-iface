package xyz.zzyitj.iface.api;

import okhttp3.Call;

import java.io.IOException;

/**
 * xyz.zzyitj.iface.api
 * 对OkHttp返回进行封装
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 08:05
 * @since 1.0
 */
public interface ApiResponseCall<T> {
    /**
     * 调用百度API返回成功
     *
     * @param call call
     * @param t    返回数据
     */
    void onSuccess(Call call, T t);

    /**
     * 调用百度API返回错误
     *
     * @param call call
     * @param e    exception
     */
    void onError(Call call, IOException e);
}
