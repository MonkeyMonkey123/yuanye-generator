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
import java.io.IOException;

public abstract class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManger.getMetaObject();
        System.out.println(meta);
        //输出根路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        //复制原始文件
        String sourceCopyDestPath = copyResouce(meta, outputPath);
        //生成代码
        generatorCode(meta, outputPath);
        //构建jar包
        String jarPath = buildJar(outputPath, meta);

        //封装脚本
        String shellOutputPath = buildScript(outputPath, jarPath);
        //生成精简版的程序
        buildDest(outputPath, sourceCopyDestPath, shellOutputPath, jarPath);

    }

    private String buildScript(String outputPath, String jarPath) {
        String shellOutputPath = outputPath + File.separator + "generator";

        ScriptGenerator.doGenerator(shellOutputPath, jarPath);
        return shellOutputPath;
    }

    private void buildDest(String outputPath, String sourceCopyDestPath, String shellOutputPath, String jarPath) {
        String distOutputPath = outputPath + "-dist";


        //拷贝jar包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutionPath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutionPath, targetAbsolutePath, true);
        //拷贝脚本文件
        FileUtil.copy(shellOutputPath, distOutputPath, true);
        //拷贝原模版文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }

    private String buildJar(String outputPath,Meta meta) throws IOException, InterruptedException {
        JarGenerator.doGenerator(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    private void generatorCode(Meta meta, String outputPath) throws IOException, TemplateException {
        //读取resources目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();
        //java包基础路径
        String outputBashPackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBashPackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java/" + outputBasePackagePath;
        String inputFilePath;
        String outputFilePath;
        System.out.println(outputBaseJavaPackagePath);

        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //cli.command.ConfigCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //cli.command.GeneratorCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GeneratorCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GeneratorCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //cli.command.ListCommand.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //cli.CommandExecutor.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //Main.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //generator.DynamicGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //generator.MainGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //generator.StaticGenerator.java.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
        //pom.xml.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath +File.separator + "pom.xml";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);

        //README.md
        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerator(inputFilePath, outputFilePath, meta);
    }

    private String copyResouce(Meta meta, String outputPath) {
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }
}
