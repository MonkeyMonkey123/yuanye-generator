package com.yunye.generator;

import com.yunye.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void doGenerator(Object model) throws TemplateException, IOException {
        String inputRootPath = "";
        String outputRootPath = "";
        String inputPath;
        String outputPath;
        inputPath = new File(inputRootPath, "").getAbsolutePath();
        outputPath = new File(outputRootPath, "").getAbsolutePath();
        DynamicGenerator.doGenerator(inputPath, outputPath, model);
        inputPath = new File(inputPath, "").getAbsolutePath();
        outputPath = new File(outputPath, "").getAbsolutePath();
        StaticGenerator.copyFilesByHutoll(inputPath, outputPath);
        inputPath = new File(inputPath, "").getAbsolutePath();
        outputPath = new File(outputPath, "").getAbsolutePath();
        StaticGenerator.copyFilesByHutoll(inputPath, outputPath);
//        String property = System.getProperty("user.dir");
//        File parentFile = new File(property).getParentFile();
//        String inputPath = new File(parentFile, "yunye-generator-demo-projects/acm-template").getAbsolutePath();
//        System.out.println(inputPath);
//
//        String outputPath = property;
//        StaticGenerator.copyFilesByHutoll(inputPath, outputPath);
//        String inputDynamicPath = property + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
//        String outputDynamicPath = outputPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
//        DynamicGenerator.doGenerator(inputDynamicPath, outputDynamicPath, model);

    }

    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setOutputText("sum = ");

        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setAuthor("dog");
        doGenerator(mainTemplateConfig);

    }
}
