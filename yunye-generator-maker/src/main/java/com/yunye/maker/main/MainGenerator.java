package com.yunye.maker.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.yunye.maker.generator.JarGenerator;
import com.yunye.maker.generator.ScriptGenerator;
import com.yunye.maker.generator.file.DynamicFileGenerator;
import com.yunye.maker.meta.Meta;
import com.yunye.maker.meta.MetaManger;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainGenerator extends GenerateTemplate{
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();

    }
}
