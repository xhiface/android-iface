package xyz.zzyitj.iface.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * xyz.zzyitj.iface.model
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 18:36
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ApiClockDto {
    private Integer id;
    private String username;
    private String phoneNumber;
    private String checkTime;
    private Integer type;
    private Integer status;
}
