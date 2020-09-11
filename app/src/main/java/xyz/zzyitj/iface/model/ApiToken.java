package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/8 16:14
 * @since 1.0
 */
public class ApiToken {
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

    @Override
    public String toString() {
        return "ApiToken{" +
                "refreshToken='" + refreshToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", sessionKey='" + sessionKey + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", scope='" + scope + '\'' +
                ", sessionSecret='" + sessionSecret + '\'' +
                '}';
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSessionSecret() {
        return sessionSecret;
    }

    public void setSessionSecret(String sessionSecret) {
        this.sessionSecret = sessionSecret;
    }
}
