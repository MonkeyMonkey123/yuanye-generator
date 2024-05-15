package com.yunye.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunye.web.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author victor
 * @description 针对表【generator(代码生成器)】的数据库操作Mapper
 * @createDate 2024-04-13 17:49:46
 * @Entity com.yunye.web.model.entity.Generator
 */
public interface GeneratorMapper extends BaseMapper<Generator> {
    @Select("SELECT id, distPath FROM generator WHERE isDelete = 1")
    List<Generator> listDeletedGenerator();
}




