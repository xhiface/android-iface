package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 17:07
 * @since 1.0
 */
public class BaiduFaceUserAddDto {
    @SerializedName("face_token")
    private String faceToken;

    private Location location;

    @Override
    public String toString() {
        return "BaiduFaceUserAddDto{" +
                "faceToken='" + faceToken + '\'' +
                ", location=" + location +
                '}';
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    static class Location {
        private Double left;
        private Double top;
        private Double width;
        private Double height;
        private Integer rotation;

        @Override
        public String toString() {
            return "Location{" +
                    "left=" + left +
                    ", top=" + top +
                    ", width=" + width +
                    ", height=" + height +
                    ", rotation=" + rotation +
                    '}';
        }

        public Double getLeft() {
            return left;
        }

        public void setLeft(Double left) {
            this.left = left;
        }

        public Double getTop() {
            return top;
        }

        public void setTop(Double top) {
            this.top = top;
        }

        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public Integer getRotation() {
            return rotation;
        }

        public void setRotation(Integer rotation) {
            this.rotation = rotation;
        }
    }
}
