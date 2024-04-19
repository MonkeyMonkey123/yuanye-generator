package com.yunye.maker.main;

import freemarker.template.TemplateException;

import java.io.IOException;

public class MainGenerator extends GenerateTemplate{
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();

    }

    @Override
    protected String buildDest(String outputPath, String sourceCopyDestPath, String shellOutputPath, String jarPath) {
        System.out.println("不要输出到dist");
        return "";
    }
}
