package com.yunye.web.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yunye.web.model.entity.Comments;
import com.yunye.web.model.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @TableName comments
 */
@Data
public class CommentsVO implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long user_id;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private Date created_at;

    /**
     *
     */
    private Long generator_id;

    private Long generatorOwnerId;

    private User users;

    private static final long serialVersionUID = 1L;
}