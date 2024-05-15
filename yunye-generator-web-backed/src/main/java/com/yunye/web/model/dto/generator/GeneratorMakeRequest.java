package com.yunye.web.model.dto.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yunye.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 制作代码生成器请求
 *
 */
@Data
public class GeneratorMakeRequest implements Serializable {

    /**
     * 元信息
     */
    private Meta meta;
    /**
     * 压缩包路径
     */
    private String zipFilePath;
    private Map<String, Object> dataModel;

    private static final long serialVersionUID = 1L;
}