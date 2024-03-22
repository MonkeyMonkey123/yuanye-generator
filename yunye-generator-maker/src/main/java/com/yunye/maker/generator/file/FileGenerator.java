package com.yunye.maker.generator.file;

import com.yunye.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class FileGenerator {
    public static void doGenerator(Object model) throws TemplateException, IOException {

        String property = System.getProperty("user.dir");
        File parentFile = new File(property).getParentFile();
        String inputPath = new File(parentFile, "yunye-generator-demo-projects/acm-template").getAbsolutePath();
        System.out.println(inputPath);

        String outputPath = property;
        StaticFileGenerator.copyFilesByHutoll(inputPath, outputPath);
        String inputDynamicPath = property + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicPath = outputPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerator(inputDynamicPath, outputDynamicPath, model);

    }

    public static void main(String[] args) throws TemplateException, IOException {
        DataModel mainTemplateConfig = new DataModel();
        mainTemplateConfig.setOutputText("sum = ");

        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setAuthor("dog");
        doGenerator(mainTemplateConfig);

    }
}
