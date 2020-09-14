package xyz.zzyitj.iface.api;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 10:55
 * @since 1.0
 */
public class ApiConst {
    private static final String IP = "139.224.119.162";
    private static final int PORT = 8080;
    public static final String BASE_URL = ApiConst.IP + ":" + ApiConst.PORT;

    /**
     * auth
     */
    public static final String LOGIN = "/system/login";
    public static final String REGISTER_USER = "/user/insertUser";
}
