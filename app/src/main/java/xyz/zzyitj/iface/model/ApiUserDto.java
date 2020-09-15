package xyz.zzyitj.iface.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 11:26
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ApiUserDto {
    private String groupId;
    private String email;
    private String phoneNumber;
    private String username;
    private String password;
    private int age;
    private boolean gender;
    private String role;
    private String createTime;
    private String updateTime;
}
