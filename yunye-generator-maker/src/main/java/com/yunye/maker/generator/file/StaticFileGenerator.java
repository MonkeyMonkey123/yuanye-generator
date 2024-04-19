package com.yunye.maker.generator.file;

import cn.hutool.core.io.FileUtil;

public class StaticFileGenerator {

    /**
     * 拷贝文件，Hutool实现，输入路径赋值到输出路径
     *
     * @param inputPath
     * @param outputPath
     */
    static void copyFilesByHutoll(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
