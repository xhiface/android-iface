package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 * https://ai.baidu.com/ai-doc/FACE/7k37c1twu#%E8%BF%94%E5%9B%9E%E8%AF%B4%E6%98%8E
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 15:30
 * @since 1.0
 */
@Getter
@Setter
@ToString
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
}
