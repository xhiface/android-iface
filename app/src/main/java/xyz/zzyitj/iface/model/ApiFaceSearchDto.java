package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 17:04
 * @since 1.0
 */
public class ApiFaceSearchDto {
    @SerializedName("face_token")
    private String faceToken;

    @SerializedName("user_list")
    private List<User> userList;

    @Override
    public String toString() {
        return "ApiFaceSearchDto{" +
                "faceToken='" + faceToken + '\'' +
                ", userList=" + userList +
                '}';
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public static class User {
        @SerializedName("group_id")
        private String groupId;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_info")
        private String userInfo;

        private float score;

        @Override
        public String toString() {
            return "User{" +
                    "groupId='" + groupId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userInfo='" + userInfo + '\'' +
                    ", score=" + score +
                    '}';
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(String userInfo) {
            this.userInfo = userInfo;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }
    }
}
