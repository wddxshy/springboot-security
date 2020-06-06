package org.shy.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 17:26
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

//    @JsonIgnore  //禁止序列化
    private Integer aId;
    private String username;
    private String password;
    private List<Role> roles;
    private boolean checked;
    private String kaptcha;
//    {"username":"admin","password":"123456","checked":false,"kaptcha":"dsdsd"}
}
