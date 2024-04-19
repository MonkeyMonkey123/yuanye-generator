package com.yunye.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.yunye.maker.generator.file.FileGenerator;
import com.yunye.maker.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;
@Data
@Command(name = "generate", mixinStandardHelpOptions = true, description = "生成代码")

public class GeneratorCommand implements Callable<Integer> {
    @Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否循环", interactive = true, echo = true)
    private boolean loop;
    @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者", interactive = true, echo = true)
    private String author = "yunye";
    @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出文本", interactive = true, echo = true)
    private String outputText = "sum = ";

    @Override
    public Integer call() throws Exception {
        DataModel mainTemplateConfig = new DataModel();
        BeanUtil.copyProperties(this, mainTemplateConfig,true);
        System.out.println("配置信息" + mainTemplateConfig);
        FileGenerator.doGenerator(mainTemplateConfig);
        return 0;
    }
}
