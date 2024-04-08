package com.yunye.maker.template;

import cn.hutool.core.bean.copier.CopyOptions;
import com.yunye.maker.template.model.*;
import com.yunye.maker.template.model.TemplateMakerModelConfig.ModelGroupConfig;

import java.util.List;

import com.yunye.maker.template.model.TemplateMakerModelConfig.ModelInfoConfig;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yunye.maker.meta.Meta;
import com.yunye.maker.meta.enums.FileGenerateTypeEnum;
import com.yunye.maker.meta.enums.FileTypeEnum;
import com.yunye.maker.template.enums.FileFilterRangeEnum;
import com.yunye.maker.template.enums.FileFilterRuleEnum;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 */
public class TemplateMaker {

    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        long id = templateMakerConfig.getId();
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig templateMakerFileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig templateMakerModelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutConfig templateMakerOutConfig = templateMakerConfig.getTemplateMakerOutConfig();
        return makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, id, templateMakerOutConfig);

    }

    /**
     * 制作模板
     *
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @param templateMakerOutConfig
     * @return
     */
    static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id, TemplateMakerOutConfig templateMakerOutConfig) {
        //没有id不生成
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();

        }
        //业务逻辑

        //复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(tempDirPath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }


        //一、输入信息
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath), 1, null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath();
        //处理模型信息
        ArrayList<Meta.ModelConfigDTO.ModelsDTO> newModelInfoList = getModelsDTOSList(templateMakerModelConfig);
        //2、输入文件信息
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();

        //要挖坑文件，相对路径
        List<Meta.FileConfigDTO.FilesDTO> newFiles = makeFileTemplats(templateMakerFileConfig, templateMakerModelConfig, sourceRootPath, fileInfoConfigList);

        //三，生成配置文件
        String metaOutputPath = templatePath + File.separator + "meta.json";
        //已有meat文件，源文件修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //1、追加配置参数
            List<Meta.FileConfigDTO.FilesDTO> filesDTOList = newMeta.getFileConfig().getFiles();
            filesDTOList.addAll(newFiles);
            List<Meta.ModelConfigDTO.ModelsDTO> modelsDTOList = newMeta.getModelConfig().getModels();
            modelsDTOList.addAll(newModelInfoList);
            //2、输出辕信息文件
            //配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(filesDTOList));
            newMeta.getModelConfig().setModels(distinctModels(modelsDTOList));


        } else {
            //1.构造配置参数对象

            Meta.FileConfigDTO fileConfigDTO = new Meta.FileConfigDTO();
            newMeta.setFileConfig(fileConfigDTO);
            fileConfigDTO.setSourceRootPath(sourceRootPath);
            ArrayList<Meta.FileConfigDTO.FilesDTO> filesDTOSList = new ArrayList<>();
            fileConfigDTO.setFiles(filesDTOSList);
            filesDTOSList.addAll(newFiles);
            Meta.ModelConfigDTO modelConfigDTO = new Meta.ModelConfigDTO();
            newMeta.setModelConfig(modelConfigDTO);
            ArrayList<Meta.ModelConfigDTO.ModelsDTO> modelsDTOSList = new ArrayList<>();
            modelConfigDTO.setModels(modelsDTOSList);
            modelsDTOSList.addAll(newModelInfoList);

        }
        if (templateMakerOutConfig != null) {
            if (templateMakerOutConfig.isRemoveGroupFilesFromRoot()) {
                List<Meta.FileConfigDTO.FilesDTO> fileInfoList = newMeta.getFileConfig().getFiles();
                newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(fileInfoList));
            }
        }
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    /**
     * 获取模型校验
     *
     * @param templateMakerModelConfig
     * @return
     */
    private static ArrayList<Meta.ModelConfigDTO.ModelsDTO> getModelsDTOSList(TemplateMakerModelConfig templateMakerModelConfig) {

        //本次新增的模型列表
        ArrayList<Meta.ModelConfigDTO.ModelsDTO> newModelInfoList = new ArrayList<>();
        if (templateMakerModelConfig == null) {
            return newModelInfoList;
        }
        //转化为配置文件接受的 modelsDTO 对象
        List<ModelInfoConfig> models = templateMakerModelConfig.getModels();
        if (CollUtil.isEmpty(models)) {
            return newModelInfoList;
        }
        List<Meta.ModelConfigDTO.ModelsDTO> inputModelInfoList = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfigDTO.ModelsDTO modelsDTO = new Meta.ModelConfigDTO.ModelsDTO();
            BeanUtil.copyProperties(modelInfoConfig, modelsDTO);
            return modelsDTO;
        }).collect(Collectors.toList());

        //如果是模型组
        ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();


        if (modelGroupConfig != null) {
            Meta.ModelConfigDTO.ModelsDTO modelsDTO = new Meta.ModelConfigDTO.ModelsDTO();
          BeanUtil.copyProperties(modelGroupConfig,modelsDTO);
            //文件全放在一个分组内
            modelsDTO.setModels(inputModelInfoList);
            newModelInfoList = new ArrayList<>();
            newModelInfoList.add(modelsDTO);
        } else {
            //不分组增加所有的模型信息
            newModelInfoList.addAll(inputModelInfoList);
        }
        return newModelInfoList;
    }

    /**
     * 生成多个文件
     *
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param fileInfoConfigList
     * @return
     */
    private static List<Meta.FileConfigDTO.FilesDTO> makeFileTemplats(TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList) {
        List<Meta.FileConfigDTO.FilesDTO> newFiles = new ArrayList<>();
        if (templateMakerFileConfig == null) {
            return newFiles;
        }
        if (fileInfoConfigList == null) {
            return newFiles;
        }
        //支持遍历多个文件
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();
            String inputFileAbsolution = sourceRootPath + File.separator + inputFilePath;
            //传入绝对路径
            //得到过滤后的文件列表
            List<File> fileList = FileFilter.doFilter(inputFileAbsolution, fileInfoConfig.getFileFilterConfigList());
            //不处理已经生成的ftl模板文件
            fileList = fileList.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfigDTO.FilesDTO filesDTO = makeFileTemplate(file, templateMakerModelConfig, sourceRootPath, fileInfoConfig);
                newFiles.add(filesDTO);
            }

        }
        //如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();


        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            Meta.FileConfigDTO.FilesDTO filesDTO = new Meta.FileConfigDTO.FilesDTO();
            filesDTO.setType(FileTypeEnum.GROUP.getValue());
            filesDTO.setCondition(condition);
            filesDTO.setGroupKey(groupKey);
            filesDTO.setGroupName(groupName);
            //文件全放在一个分组内
            filesDTO.setFiles(newFiles);
            newFiles = new ArrayList<>();
            newFiles.add(filesDTO);
        }
        return newFiles;
    }

    /**
     * 制作模板文件
     *
     * @param fileInput                传入文件绝对路径
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @param fileInfoConfig
     * @return
     */
    private static Meta.FileConfigDTO.FilesDTO makeFileTemplate(File fileInput, TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, TemplateMakerFileConfig.FileInfoConfig fileInfoConfig) {
        String fileInputAbsolutePath = fileInput.getAbsolutePath();
        fileInputAbsolutePath = fileInputAbsolutePath.replaceAll("\\\\", "/");
        String inputFilePath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = inputFilePath + ".ftl";

        //二：使用字符串替换，生成模板文件

        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";
        String replacement;
        String fileContent;
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String newFileContent = fileContent;
        //支持多个模型对同一个文件进行多轮替换
        ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();

        for (ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            String filedName = modelInfoConfig.getFiledName();
            //模型配置
            //不是分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", filedName);
            } else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, filedName);
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }


        //如果已有模板文件表示不是第一次制作，在原有文件挖坑


        //输出模板文件
        //文件配置信息

        Meta.FileConfigDTO.FilesDTO filesDTO = new Meta.FileConfigDTO.FilesDTO();
        filesDTO.setInputPath(fileOutputPath);
        filesDTO.setOutputPath(inputFilePath);
        filesDTO.setType(FileTypeEnum.FILE.getValue());
        filesDTO.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        filesDTO.setCondition(fileInfoConfig.getCondition());
        boolean contentEquals = newFileContent.equals(fileContent);
        //之前不存在模板文件并且这次替换没有修改文件的内容，才是静态生成
        if (!hasTemplateFile) {
            if (contentEquals) {
                //输出路径等于输出路径
                filesDTO.setInputPath(inputFilePath);
                filesDTO.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                //输出模板文件
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

            }
        } else if (!contentEquals) {
            //有模板文件并且增加了新坑，生成新的模板文件
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }

        return filesDTO;

    }

    public static void main(String[] args) {
        //1、项目基本信息
        String name = "acm-template-generator";
        String description = "ACM 示例模板生成器";
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);
        //指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        //要挖坑项目目录
        String originProjectPath = new File(projectPath).getParent() + File.separator + "yunye-generator-demo-projects/acm-template";
        String fileInputPath1 = "src/com/yupi/acm";
        String fileInputPath2 = "src/com/yupi/control";
        List<String> fileInputPath = new ArrayList<String>();
        fileInputPath.add(fileInputPath1);
        fileInputPath.add(fileInputPath2);
        //3、输入模型参数信息
//        Meta.ModelConfigDTO.ModelsDTO modelInfo = new Meta.ModelConfigDTO.ModelsDTO();
//        modelInfo.setFiledName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDescription("输出信息");
//        modelInfo.setDefaultValue("sum=");
        //输入模型参数信息第二次
        Meta.ModelConfigDTO.ModelsDTO modelInfo = new Meta.ModelConfigDTO.ModelsDTO();
        modelInfo.setFiledName("className");
        modelInfo.setType("String");
        //替换变量首次
//        String str = "Sum";
        //替换变量第二次
        String str = "MainTemplate";

        //文件过滤配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<FileFilterConfig>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("late")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFileFilterConfigList(fileFilterConfigList);
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1, fileInfoConfig2);
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(fileInfoConfigList);
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText2");
        fileGroupConfig.setGroupKey("test1");
        fileGroupConfig.setGroupName("测试分组2");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);
        //模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        ModelGroupConfig modelGroupConfig = new ModelGroupConfig();
        modelGroupConfig.setGroupKey("mainTemplate");
        modelGroupConfig.setGroupName("ACM模板");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
        ModelInfoConfig modelInfoConfig1 = new ModelInfoConfig();
        modelInfoConfig1.setFiledName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("MainTemplate");
        modelInfoConfig1.setReplaceText("MainTemplate");
        ModelInfoConfig modelInfoConfig2 = new ModelInfoConfig();
        modelInfoConfig2.setFiledName("outputText");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("Sum");
        modelInfoConfig2.setReplaceText("Sum");
        List<ModelInfoConfig> modelInfoConfigs = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigs);

        TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1773909827003686912L, null);
    }

    /**
     * 文件去重
     *
     * @param filesDTOList
     * @return
     */
    private static List<Meta.FileConfigDTO.FilesDTO> distinctFiles(List<Meta.FileConfigDTO.FilesDTO> filesDTOList) {

        //1.将所有文件配置（fileInfo）分为有分组的和无分组的
        //先处理有分组的
        //以组为单位划分
        Map<String, List<Meta.FileConfigDTO.FilesDTO>> groupKeyFileInfoListMap = filesDTOList.stream()
                .filter(fileInfo -> StrUtil.isNotEmpty(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfigDTO.FilesDTO::getGroupKey));


        //同组内配置合并
        //{groupKey:files:[[1,2],[2,3]]}
        //{groupKey:files:[[1,2，2,3]]}
        //{groupKey:files:[[1,2,3]]}
        //合并后对象Map
        HashMap<String, Meta.FileConfigDTO.FilesDTO> groupKeyMergedInfoMap = new HashMap<>();

        for (Map.Entry<String, List<Meta.FileConfigDTO.FilesDTO>> stringListEntry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfigDTO.FilesDTO> tempFileInfoList = stringListEntry.getValue();
            List<Meta.FileConfigDTO.FilesDTO> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(Collectors.toMap(Meta.FileConfigDTO.FilesDTO::getOutputPath, o -> o, (e, r) -> r))
                    .values());
            //使用新的group配置

            Meta.FileConfigDTO.FilesDTO newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = stringListEntry.getKey();

            groupKeyMergedInfoMap.put(groupKey, newFileInfo);
        }

        //2、对于有分组的文件配置，如果有相同的分组，同分组的文件merge，不同分组可同时保留

        //3.创建新的文件配置列表，将要合并后的分组添加到结果列表
        ArrayList<Meta.FileConfigDTO.FilesDTO> resultList = new ArrayList<>(groupKeyMergedInfoMap.values());

        //4.再将无分组的文件配置列表添加的结果列表
        resultList.addAll(new ArrayList<>(filesDTOList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupName()))
                .collect(
                        Collectors.toMap(Meta.FileConfigDTO.FilesDTO::getOutputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

    /**
     * 模型去重
     *
     * @param modelsDTOList
     * @return
     */
    private static List<Meta.ModelConfigDTO.ModelsDTO> distinctModels(List<Meta.ModelConfigDTO.ModelsDTO> modelsDTOList) {
        //1.将所有模型配置（modelInfo）分为有分组的和无分组的
        //先处理有分组的
        //以组为单位划分
        Map<String, List<Meta.ModelConfigDTO.ModelsDTO>> groupKeyModelInfoListMap = modelsDTOList.stream()
                .filter(modelInfo -> StrUtil.isNotEmpty(modelInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.ModelConfigDTO.ModelsDTO::getGroupKey));


        //同组内配置合并
        //{groupKey:models:[[1,2],[2,3]]}
        //{groupKey:models:[[1,2，2,3]]}
        //{groupKey:models:[[1,2,3]]}
        //合并后对象Map
        HashMap<String, Meta.ModelConfigDTO.ModelsDTO> groupKeyMergedInfoMap = new HashMap<>();

        for (Map.Entry<String, List<Meta.ModelConfigDTO.ModelsDTO>> stringListEntry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfigDTO.ModelsDTO> tempModelInfoList = stringListEntry.getValue();
            List<Meta.ModelConfigDTO.ModelsDTO> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(Collectors.toMap(Meta.ModelConfigDTO.ModelsDTO::getFiledName, o -> o, (e, r) -> r))
                    .values());
            //使用新的group配置

            Meta.ModelConfigDTO.ModelsDTO newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = stringListEntry.getKey();

            groupKeyMergedInfoMap.put(groupKey, newModelInfo);
        }

        //2、对于有分组的模型配置，如果有相同的分组，同分组的模型merge，不同分组可同时保留

        //3.创建新的模型配置列表，将要合并后的分组添加到结果列表
        ArrayList<Meta.ModelConfigDTO.ModelsDTO> resultList = new ArrayList<>(groupKeyMergedInfoMap.values());

        //4.再将无分组的模型配置列表添加的结果列表
        resultList.addAll(new ArrayList<>(modelsDTOList.stream()
                .filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupName()))
                .collect(
                        Collectors.toMap(Meta.ModelConfigDTO.ModelsDTO::getFiledName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

}
