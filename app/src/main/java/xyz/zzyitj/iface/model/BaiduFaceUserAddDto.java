package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 17:07
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class BaiduFaceUserAddDto {
    @SerializedName("face_token")
    private String faceToken;

    private Location location;

    @Getter
    @Setter
    @ToString
    static class Location {
        private Double left;
        private Double top;
        private Double width;
        private Double height;
        private Integer rotation;
    }
}
