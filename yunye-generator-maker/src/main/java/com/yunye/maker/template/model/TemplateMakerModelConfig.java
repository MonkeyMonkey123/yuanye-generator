package com.yunye.maker.template.model;

import com.yunye.maker.meta.Meta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TemplateMakerModelConfig {
    private List<ModelInfoConfig> models;
    private ModelGroupConfig modelGroupConfig;
    @NoArgsConstructor
    @Data
    public static class ModelInfoConfig {
        private String filedName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;
        private String replaceText;
    }
    @NoArgsConstructor
    @Data
    public static class ModelGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;
        private String type;
        private String description;
    }
}
