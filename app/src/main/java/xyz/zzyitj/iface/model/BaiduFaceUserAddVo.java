package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/11 17:13
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class BaiduFaceUserAddVo {
    /**
     * 图片信息(总数据大小应小于10M)，图片上传方式根据image_type来判断。
     * 注：组内每个uid下的人脸图片数目上限为20张
     */
    @SerializedName("image")
    private String image;

    /**
     * 图片类型
     * BASE64:图片的base64值，base64编码后的图片数据，编码后的图片大小不超过2M；
     * URL:图片的 URL地址( 可能由于网络等原因导致下载图片时间过长)；
     * FACE_TOKEN：人脸图片的唯一标识，调用人脸检测接口时，会为每个人脸图片赋予一个唯一的FACE_TOKEN，同一张图片多次检测得到的FACE_TOKEN是同一个。
     */
    @SerializedName("image_type")
    private String imageType;

    /**
     * 用户组id，标识一组用户（由数字、字母、下划线组成），长度限制48B。
     * 产品建议：根据您的业务需求，可以将需要注册的用户，按照业务划分，分配到不同的group下，
     * 例如按照会员手机尾号作为groupid，用于刷脸支付、会员计费消费等，
     * 这样可以尽可能控制每个group下的用户数与人脸数，提升检索的准确率
     */
    @SerializedName("group_id")
    private String groupId;

    /**
     * 用户id（由数字、字母、下划线组成），长度限制128B
     */
    @SerializedName("user_id")
    private String uid;

    /**
     * 用户资料，长度限制256B 默认空
     */
    @SerializedName("user_info")
    private String userInfo;
}
