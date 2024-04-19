package com.yunye.maker;

import com.yunye.maker.cli.CommandExecutor;
import com.yunye.maker.main.GenerateTemplate;
import com.yunye.maker.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        args = new String[]{"generate", "-l", "-a", "-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
//        CommandExecutor commandExecutor = new CommandExecutor();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();

    }
}
