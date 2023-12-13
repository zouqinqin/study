package com.zqq.logRecord.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("test_zqq.user")
@Accessors(chain = true)
public class User implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name")
    private String name;
    @TableField(value = "pass_word")
    private String passWord;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;


}
