package xyz.zzyitj.iface.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 10:07
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ApiUserLoginVo {
    private String phoneNumber;
    private String password;
}
