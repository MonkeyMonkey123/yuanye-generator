package com.yunye.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yunye.maker.meta.Meta;
import com.yunye.maker.meta.enums.FileGenerateTypeEnum;
import com.yunye.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 */
public class TemplateMaker {
    /**
     * 制作模板
     * @param newMeta
     * @param originProjectPath
     * @param inputFilePathLList
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return
     */
    private static long makeTemplate(Meta newMeta, String originProjectPath, List<String> inputFilePathLList, Meta.ModelConfigDTO.ModelsDTO modelInfo, String searchStr, Long id) {
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
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();


        //2、输入文件信息
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        //要挖坑文件，相对路径
        List<Meta.FileConfigDTO.FilesDTO> newFiles = new ArrayList<>();
        //支持遍历多个文件
        for (String inputFilePath : inputFilePathLList) {
            String inputFileAbsolution = sourceRootPath + File.separator + inputFilePath;
            if (FileUtil.isDirectory(inputFileAbsolution)) {
                List<File> fileList = FileUtil.loopFiles(inputFileAbsolution);
                for (File file : fileList) {
                    Meta.FileConfigDTO.FilesDTO filesDTO = makeFileTemplate(file, modelInfo, searchStr, sourceRootPath);
                    newFiles.add(filesDTO);
                }
            } else {
                Meta.FileConfigDTO.FilesDTO filesDTO = makeFileTemplate(new File(inputFileAbsolution), modelInfo, searchStr, sourceRootPath);
                newFiles.add(filesDTO);
            }
        }


        //三，生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        //已有meat文件，源文件修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            //1、追加配置参数
            List<Meta.FileConfigDTO.FilesDTO> filesDTOList = oldMeta.getFileConfig().getFiles();
            filesDTOList.addAll(newFiles);
            List<Meta.ModelConfigDTO.ModelsDTO> modelsDTOList = oldMeta.getModelConfig().getModels();
            modelsDTOList.add(modelInfo);
            //2、输出辕信息文件
            //配置去重
            oldMeta.getFileConfig().setFiles(distinctFiles(filesDTOList));
            oldMeta.getModelConfig().setModels(distinctModels(modelsDTOList));
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta), metaOutputPath);


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
            modelsDTOSList.add(modelInfo);
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        }

        return id;
    }

    /**
     * 制作模板文件
     * @param fileInput 传入文件绝对路径
     * @param modelInfo
     * @param searchStr
     * @param sourceRootPath
     * @return
     */
    private static Meta.FileConfigDTO.FilesDTO makeFileTemplate(File fileInput, Meta.ModelConfigDTO.ModelsDTO modelInfo, String searchStr, String sourceRootPath) {
        String fileInputAbsolutePath = fileInput.getAbsolutePath();
        fileInputAbsolutePath = fileInputAbsolutePath.replaceAll("\\\\", "/");
        String inputFilePath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = inputFilePath + ".ftl";

        //二：使用字符串替换，生成模板文件

        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        //如果已有模板文件表示不是第一次制作，在原有文件挖坑
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        String replacement = String.format("${%s}", modelInfo.getFiledName());
        String newFileContent = StrUtil.replace(fileContent, searchStr, replacement);
        //输出模板文件
        //文件配置信息

        Meta.FileConfigDTO.FilesDTO filesDTO = new Meta.FileConfigDTO.FilesDTO();
        filesDTO.setInputPath(inputFilePath);
        filesDTO.setOutputPath(fileOutputPath);
        filesDTO.setType(FileTypeEnum.FILE.getValue());

        if (newFileContent.equals(fileContent)) {
            //输出路径等于输出路径
            filesDTO.setOutputPath(inputFilePath);
            filesDTO.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        }
        else {
            //输出模板文件
            filesDTO.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
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
        String fileInputPath2 = "src/com/yupi/temp";
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
        TemplateMaker.makeTemplate(meta, originProjectPath, fileInputPath, modelInfo, str, 1773909827003686912L);
    }

    /**
     * 文件去重
     *
     * @param filesDTOList
     * @return
     */
    private static List<Meta.FileConfigDTO.FilesDTO> distinctFiles(List<Meta.FileConfigDTO.FilesDTO> filesDTOList) {
        ArrayList<Meta.FileConfigDTO.FilesDTO> newFilesDTOArrayList = new ArrayList<>(filesDTOList.stream().collect(
                Collectors.toMap(Meta.FileConfigDTO.FilesDTO::getInputPath, o -> o, (e, r) -> r)
        ).values());
        return newFilesDTOArrayList;
    }

    /**
     * 模型去重
     *
     * @param modelsDTOList
     * @return
     */
    private static List<Meta.ModelConfigDTO.ModelsDTO> distinctModels(List<Meta.ModelConfigDTO.ModelsDTO> modelsDTOList) {
        ArrayList<Meta.ModelConfigDTO.ModelsDTO> newModelsDTOArrayList = new ArrayList<>(modelsDTOList.stream().collect(
                Collectors.toMap(Meta.ModelConfigDTO.ModelsDTO::getFiledName, o -> o, (e, r) -> r)
        ).values());
        return newModelsDTOArrayList;
    }

}
