package com.yunye.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.yunye.maker.template.enums.FileFilterRangeEnum;
import com.yunye.maker.template.enums.FileFilterRuleEnum;
import com.yunye.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FileFilter {
    /**
     * 单个文件过滤
     * @param fileFilterConfigList
     * @param file
     * @return
     */
    public static boolean doSingleFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);
        //所以过滤器校验结束后的结果
        boolean result = true;
        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }
        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }
            //要过滤的的原内容
            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:

            }
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }
            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
            //有一个不满足就返回
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对某个文件或目录进行过滤
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {

        //根据路径获取所有文件
        List<File> filesList = FileUtil.loopFiles(filePath);
        return filesList.stream()
                .filter(file -> doSingleFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());

    }
}

