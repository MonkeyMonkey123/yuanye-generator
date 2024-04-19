package com.yunye.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicFileGenerator {
    /**
     * 生成文件
     * @param inputPath 模板文件输入路径
     * @param outPath 输出路径
     * @param model 数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGeneratorByPath(String inputPath, String outPath,Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File(inputPath).getParentFile());
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate(new File(inputPath).getName());
        if (!FileUtil.exist(outPath)) {
            FileUtil.touch(outPath);
        }

        FileWriter out = new FileWriter(outPath);

        template.process(model, out);
        out.close();

    }
    /**
     * 使用相对路径生成文件
     * @param relativeInputPath 模板文件相对输入路径
     * @param outPath 输出路径
     * @param model 数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerator(String relativeInputPath, String outPath,Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        int lastSplitIndex = relativeInputPath.lastIndexOf("/");
//        获取模板文件所属包的模板名称
        String basePackagePath = relativeInputPath.substring(0,lastSplitIndex);
        String templateName = relativeInputPath.substring(lastSplitIndex + 1);
        //通过类加载器读取模板
        ClassTemplateLoader templateLoader = new ClassTemplateLoader(DynamicFileGenerator.class, basePackagePath);
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate(templateName);
        if (!FileUtil.exist(outPath)) {
            FileUtil.touch(outPath);
        }

        FileWriter out = new FileWriter(outPath);

        template.process(model, out);
        out.close();

    }
}
