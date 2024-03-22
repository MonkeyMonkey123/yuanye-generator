package com.yunye.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;

public class StaticGenerator {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        String absolutePath = parentFile.getAbsolutePath();
        System.out.println(absolutePath);
        String inputPath = parentFile + File.separator + "yunye-generator-demo-projects" + File.separator + "acm-template";
        System.out.println(inputPath);
        String outputPath = absolutePath;
        copyFilesByHutoll(inputPath, outputPath);
    }

    /**
     * 拷贝文件，Hutool实现，输入路径赋值到输出路径
     * @param inputPath
     * @param outputPath
     */
    static void copyFilesByHutoll(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
