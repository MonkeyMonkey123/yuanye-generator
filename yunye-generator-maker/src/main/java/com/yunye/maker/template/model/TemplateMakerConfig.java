package com.yunye.maker.template.model;

import com.yunye.maker.meta.Meta;
import lombok.Data;

@Data
public class TemplateMakerConfig {
    private long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();
    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
    private TemplateMakerOutConfig templateMakerOutConfig = new TemplateMakerOutConfig();
}
