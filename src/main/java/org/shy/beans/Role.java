package org.shy.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 17:30
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    private Integer rId;
    private String rName;

    public Role(String rName) {
        this.rName = rName;
    }
}
