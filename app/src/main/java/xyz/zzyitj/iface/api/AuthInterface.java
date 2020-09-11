package xyz.zzyitj.iface.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.zzyitj.iface.model.ApiToken;

/**
 * @author intent
 * @version 1.0
 * @date 2020/2/4 8:03 下午
 * @email zzy.main@gmail.com
 */
public interface AuthInterface {
    @GET(value = ApiConst.AUTH_GET_TOKEN)
    Observable<ApiToken> getToken(@Query("grant_type") String grantType,
                                  @Query("client_id") String apiKey,
                                  @Query("client_secret") String secretKey);
}
