package com.yunye.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import com.yunye.maker.model.DataModel;
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
    public static void doGenerator(String inputPath, String outPath,Object model) throws IOException, TemplateException {
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
}
