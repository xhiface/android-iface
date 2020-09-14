package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;

/**
 * xyz.zzyitj.iface.model
 * https://ai.baidu.com/ai-doc/FACE/7k37c1twu#%E8%BF%94%E5%9B%9E%E8%AF%B4%E6%98%8E
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:30
 * @since 1.0
 */
public class BaiduResponseBody<T> {

    @SerializedName("error_code")
    private Integer errorCode;

    @SerializedName("error_msg")
    private String errorMsg;

    @SerializedName("log_id")
    private Long logId;

    private Long timestamp;

    private Integer cached;

    private T result;

    @Override
    public String toString() {
        return "BaiduResponseBody{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", logId=" + logId +
                ", timestamp=" + timestamp +
                ", cached=" + cached +
                ", result=" + result +
                '}';
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCached() {
        return cached;
    }

    public void setCached(Integer cached) {
        this.cached = cached;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
