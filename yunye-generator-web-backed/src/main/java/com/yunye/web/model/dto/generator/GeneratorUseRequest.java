package com.yunye.web.model.dto.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * 使用代码生成器请求
 *
 */
@Data
public class GeneratorUseRequest implements Serializable {

    /**
     * 生成器id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    private Map<String, Object> dataModel;

    private static final long serialVersionUID = 1L;
}