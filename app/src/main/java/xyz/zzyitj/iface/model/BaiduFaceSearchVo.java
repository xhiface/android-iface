package xyz.zzyitj.iface.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 16:57
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class BaiduFaceSearchVo {
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
     * 从指定的group中进行查找 用逗号分隔，上限10个
     */
    @SerializedName("group_id_list")
    private String groupIdList;
}
