//package com.yunye.maker.template;
//
//import cn.hutool.core.io.resource.ResourceUtil;
//import cn.hutool.json.JSONUtil;
//import com.yunye.maker.meta.Meta;
//import com.yunye.maker.template.enums.FileFilterRangeEnum;
//import com.yunye.maker.template.enums.FileFilterRuleEnum;
//import com.yunye.maker.template.model.FileFilterConfig;
//import com.yunye.maker.template.model.TemplateMakerConfig;
//import com.yunye.maker.template.model.TemplateMakerFileConfig;
//import com.yunye.maker.template.model.TemplateMakerModelConfig;
//import org.junit.Test;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class TemplateMakerTest {
//
//    @Test
//    public void makeTemplate() {
//
//    }
//
//    public static void testMakeTemplateBug1(String[] args) {
//        //1、项目基本信息
//        String name = "acm-template-generator";
//        String description = "ACM 示例模板生成器";
//        Meta meta = new Meta();
//        meta.setName(name);
//        meta.setDescription(description);
//        //指定原始项目路径
//        String projectPath = System.getProperty("user.dir");
//        //要挖坑项目目录
//        String originProjectPath = new File(projectPath).getParent() + File.separator + "yunye-generator-demo-projects/acm-template";
//        String fileInputPath1 = "src/com/yupi/acm";
//        String fileInputPath2 = "src/com/yupi/control";
//        List<String> fileInputPath = new ArrayList<String>();
//        fileInputPath.add(fileInputPath1);
//        fileInputPath.add(fileInputPath2);
//        //3、输入模型参数信息
////        Meta.ModelConfigDTO.ModelsDTO modelInfo = new Meta.ModelConfigDTO.ModelsDTO();
////        modelInfo.setFiledName("outputText");
////        modelInfo.setType("String");
////        modelInfo.setDescription("输出信息");
////        modelInfo.setDefaultValue("sum=");
//        //输入模型参数信息第二次
//        Meta.ModelConfigDTO.ModelsDTO modelInfo = new Meta.ModelConfigDTO.ModelsDTO();
//        modelInfo.setFiledName("className");
//        modelInfo.setType("String");
//        //替换变量首次
////        String str = "Sum";
//        //替换变量第二次
//        String str = "MainTemplate";
//
//        //文件过滤配置
//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig1.setPath(fileInputPath1);
//        List<FileFilterConfig> fileFilterConfigList = new ArrayList<FileFilterConfig>();
//        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
//                .range(FileFilterRangeEnum.FILE_NAME.getValue())
//                .rule(FileFilterRuleEnum.CONTAINS.getValue())
//                .value("late")
//                .build();
//        fileFilterConfigList.add(fileFilterConfig);
//        fileInfoConfig1.setFileFilterConfigList(fileFilterConfigList);
//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig2.setPath(fileInputPath2);
//        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1, fileInfoConfig2);
//        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
//        templateMakerFileConfig.setFiles(fileInfoConfigList);
//        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
//        fileGroupConfig.setCondition("outputText2");
//        fileGroupConfig.setGroupKey("test1");
//        fileGroupConfig.setGroupName("测试分组2");
//        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);
//        //模型参数配置
//        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
//        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
//        modelGroupConfig.setGroupKey("mainTemplate");
//        modelGroupConfig.setGroupName("ACM模板");
//        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig1.setFiledName("className");
//        modelInfoConfig1.setType("String");
//        modelInfoConfig1.setDefaultValue("MainTemplate");
//        modelInfoConfig1.setReplaceText("MainTemplate");
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig2.setFiledName("outputText");
//        modelInfoConfig2.setType("String");
//        modelInfoConfig2.setDefaultValue("Sum");
//        modelInfoConfig2.setReplaceText("Sum");
//        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigs = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
//        templateMakerModelConfig.setModels(modelInfoConfigs);
//
//        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1773909827003686912L, null);
//    }
//
//    @Test
//    public void testMakeTemplateWithJSON() {
//        String configStr = ResourceUtil.readUtf8Str("templateMaker1.json");
//        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        long id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//
//    }
//
//    @Test
//    public void makeSpringBootTemplate() {
//        String rootPath;
//        String configStr;
//        TemplateMakerConfig templateMakerConfig;
//        long id = 0;
//        rootPath = "examples/springboot-init/";
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker1.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker2.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker3.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker4.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker5.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker6.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker7.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker8.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        id = TemplateMaker.makeTemplate(templateMakerConfig);
//        System.out.println(id);
//    }
//}