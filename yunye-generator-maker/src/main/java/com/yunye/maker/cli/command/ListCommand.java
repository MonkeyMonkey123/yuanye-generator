package com.yunye.maker.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

@Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {


    @Override
    public void run() {
        String property = System.getProperty("user.dir");
        File parentFile = new File(property).getParentFile();
        String inputPath = new File(parentFile, "yunye-generator-demo-projects/acm-template").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);

        }


    }
}
