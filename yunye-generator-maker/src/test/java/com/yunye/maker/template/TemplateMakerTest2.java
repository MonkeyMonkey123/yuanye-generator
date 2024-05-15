package com.yunye.maker.template;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.yunye.maker.meta.Meta;
import com.yunye.maker.template.enums.FileFilterRangeEnum;
import com.yunye.maker.template.enums.FileFilterRuleEnum;
import com.yunye.maker.template.model.FileFilterConfig;
import com.yunye.maker.template.model.TemplateMakerConfig;
import com.yunye.maker.template.model.TemplateMakerFileConfig;
import com.yunye.maker.template.model.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateMakerTest2 {

    @Test
    public void makeTemplate() {

    }

    @Test
    public void makeLoopTemplate() {
        String rootPath;
        String configStr;
        TemplateMakerConfig templateMakerConfig;
        long id = 0;
        rootPath = "loop/";
        configStr = ResourceUtil.readUtf8Str(rootPath + "loop.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }
}