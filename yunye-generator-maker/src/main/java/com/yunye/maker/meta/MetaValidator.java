package com.yunye.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.yunye.maker.meta.enums.FileGenerateTypeEnum;
import com.yunye.maker.meta.enums.FileTypeEnum;
import com.yunye.maker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        vlaidAndFillMetaRoot(meta);
        validAndFillFileConfig(meta);
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfigDTO modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfigDTO.ModelsDTO> modelInfoList = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(modelInfoList)) {
            return;
        }
        for (Meta.ModelConfigDTO.ModelsDTO modelsDTO : modelInfoList) {
            String groupKey = modelsDTO.getGroupKey();
            if(StrUtil.isNotEmpty(groupKey)) {
                List<Meta.ModelConfigDTO.ModelsDTO> sunModelInfoList = modelsDTO.getModels();
                String allArgsStr = sunModelInfoList.stream().map(sunModelInfo -> {
                    return String.format("\"--%s\"", sunModelInfo.getFiledName());
                }).collect(Collectors.joining(","));
                modelsDTO.setAllArgsStr(allArgsStr);
                continue;
            }
            String filedName = modelsDTO.getFiledName();
            if (StrUtil.isBlank(filedName)) {
                throw new MetaException("未填写 filedName");

            }
            String modelInfoType = modelsDTO.getType();
            if (StrUtil.isBlank(modelInfoType)) {
                modelsDTO.setType(ModelTypeEnum.STRING.getValue());
            }

        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        Meta.FileConfigDTO fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isEmpty(sourceRootPath)) {
            throw new MetaException(("未填写 sourceRootPath"));
        }
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath = ".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();

        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);

        }
        //outputRootPath： 默认为当前路径下的generated
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputPath = "generated";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputPath);

        }
        String fileConfigType = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getValue();
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfig.setType(defaultType);

        }
        List<Meta.FileConfigDTO.FilesDTO> filesInfoList = fileConfig.getFiles();
        if (!CollectionUtil.isNotEmpty(filesInfoList)) {
            return;
        }
        for (Meta.FileConfigDTO.FilesDTO filesDTO : filesInfoList) {
            String type1 = filesDTO.getType();
            if (FileTypeEnum.GROUP.getValue().equals(type1)) {
                continue;
            }
            String inputPath = filesDTO.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            String type = filesDTO.getType();
            if (StrUtil.isEmpty(type)) {
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    filesDTO.setType(FileTypeEnum.DIR.getValue());

                } else {
                    filesDTO.setType(FileTypeEnum.FILE.getValue());

                }
            }
            String generateType = filesDTO.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                if (inputPath.endsWith(".ftl")) {
                    filesDTO.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

                } else {
                    filesDTO.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());

                }

            }
        }

    }

    private static void vlaidAndFillMetaRoot(Meta meta) {
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String author = StrUtil.blankToDefault(meta.getAuthor(), "yunye");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.yunye");
        String version = StrUtil.blankToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.blankToDefault(meta.getCreateTime(), DateUtil.now());
        String description = StrUtil.blankToDefault(meta.getDescription(), "我的模板代码生成器");
        meta.setName(name);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createTime);
        meta.setDescription(description);


    }
}
