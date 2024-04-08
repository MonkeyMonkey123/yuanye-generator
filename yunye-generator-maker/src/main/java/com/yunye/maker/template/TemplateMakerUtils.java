package com.yunye.maker.template;

import cn.hutool.core.util.StrUtil;
import com.yunye.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模板制作工具类
 */
public class TemplateMakerUtils {
    /**
     * 从未分组的文件中移除同名文件
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfigDTO.FilesDTO> removeGroupFilesFromRoot(List<Meta.FileConfigDTO.FilesDTO> fileInfoList) {
        //先获取到所有的分组
        List<Meta.FileConfigDTO.FilesDTO> groupFileInfoList = fileInfoList.stream()
                .filter(filesDTO -> StrUtil.isNotBlank(filesDTO.getGroupKey()))
                .collect(Collectors.toList());

        //获取所有的分组的文件列表
        List<Meta.FileConfigDTO.FilesDTO> groupInnerInfoList = groupFileInfoList.stream()
                .flatMap(filesDTO -> filesDTO.getFiles().stream())
                .collect(Collectors.toList());


        //获取所有分组内文件的输入路径集合
        Set<String> fileInputPathSet = groupInnerInfoList.stream()
                .map(Meta.FileConfigDTO.FilesDTO::getInputPath)
                .collect(Collectors.toSet());
        //移除所有在集合类的外层文件
        return fileInfoList.stream()
                .filter(
                        //不在集合类保留
                        filesDTO -> !fileInputPathSet.contains(filesDTO.getInputPath())
                )
                .collect(Collectors.toList());

    }
}
