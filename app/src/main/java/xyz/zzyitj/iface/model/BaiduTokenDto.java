package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 16:14
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class BaiduTokenDto {
    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private Long expiresIn;

    @SerializedName("session_key")
    private String sessionKey;

    @SerializedName("access_token")
    private String accessToken;

    private String scope;

    @SerializedName("session_secret")
    private String sessionSecret;
}
