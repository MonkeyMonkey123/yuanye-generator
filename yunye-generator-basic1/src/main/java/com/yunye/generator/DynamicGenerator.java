package com.yunye.generator;

import com.yunye.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {

        String property = System.getProperty("user.dir");
        String inputPath = property + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = property + File.separator + "MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setAuthor("yunye2");
        mainTemplateConfig.setOutputText("求和结果：");
        doGenerator(inputPath, outputPath, mainTemplateConfig);

    }
    public static void doGenerator(String inputPath, String outPath,Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File(inputPath).getParentFile());
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate(new File(inputPath).getName());
        FileWriter out = new FileWriter(outPath);
        template.process(model, out);
        out.close();

    }
}
