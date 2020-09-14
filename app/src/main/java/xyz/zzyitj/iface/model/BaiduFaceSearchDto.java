package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 17:04
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class BaiduFaceSearchDto {
    @SerializedName("face_token")
    private String faceToken;

    @SerializedName("user_list")
    private List<User> userList;

    @Getter
    @Setter
    @ToString
    public static class User {
        @SerializedName("group_id")
        private String groupId;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_info")
        private String userInfo;

        private float score;
    }
}
