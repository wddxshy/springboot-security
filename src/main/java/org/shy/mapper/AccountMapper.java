package org.shy.mapper;

import org.apache.ibatis.annotations.Param;
import org.shy.beans.Account;

/**
 * @Author: WeiDongDong
 * @Date 2020/5/7 17:35
 * @Description
 */
public interface AccountMapper {

    Account queryAccountByUsername(@Param("username") String username);

}
