package xyz.zzyitj.iface.api;

/**
 * xyz.zzyitj.iface.api
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 10:55
 * @since 1.0
 */
public class ApiConst {
    public static final String DEFAULT_ROLE_USER = "user";
    public static final String DEFAULT_ROLE_ADMIN = "sysadmin";
    private static final String IP = "139.224.119.162";
    private static final int PORT = 80;
    public static final String HOST = "http://" + ApiConst.IP + ":" + ApiConst.PORT;
    public static final String SHARED_PREFS_NAME = "api_shared_prefs";
    public static final String SHARED_PREFS_USER = "user";
    /**
     * auth
     */
    public static final String LOGIN = "/system/login";
    /**
     * user
     */
    public static final String REGISTER_USER = "/user/add-user";
    public static final String GET_USER_FROM_PHONE_NUMBER = "/user/get-one-user";
    /**
     * clock
     */
    public static final String ADD_ATTEND = "/attend/add-attend";
    public static final String USER_ATTEND_RECORD = "/attend/get-one-attend";
}
