package com.yunye.maker.meta;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Data
public class Meta {

    private String name;
    private String description;
    private String basePackage;
    private String version;
    private String author;
    private String createTime;
    private FileConfigDTO fileConfig;
    private ModelConfigDTO modelConfig;

    @NoArgsConstructor
    @Data
    public static class FileConfigDTO implements Serializable {
        private String inputRootPath;
        private String outputRootPath;
        private String sourceRootPath;
        private String type;
        private List<FilesDTO> files;

        @NoArgsConstructor
        @Data
        public static class FilesDTO  implements Serializable{
            private String inputPath;
            private String outputPath;
            private String type;
            private String generateType;
            private String condition;
            private String groupKey;
            private String groupName;
            private List<FilesDTO> files;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ModelConfigDTO  implements Serializable{
        private List<ModelsDTO> models;

        @NoArgsConstructor
        @Data
        public static class ModelsDTO  implements Serializable{
            private String filedName;
            private String type;
            private String description;
            private Object defaultValue;
            private String abbr;
            private String groupKey;
            private String groupName;
            private String condition;
            private List<ModelsDTO> models;
            //中间参数
            private String allArgsStr;
        }
    }
}
