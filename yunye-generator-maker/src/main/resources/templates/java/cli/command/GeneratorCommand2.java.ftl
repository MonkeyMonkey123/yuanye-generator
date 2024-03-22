package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
@Data
@Command(name = "generate", mixinStandardHelpOptions = true, description = "生成代码")

public class GeneratorCommand implements Callable<Integer> {

    <#list modelConfig.models as modelInfo>
        @Option(names = {<#if modelInfo.abbr??> "-${modelInfo.abbr}",</#if> "--${modelInfo.filedName}"}, arity = "0..1", <#if modelInfo.description??>description = "${modelInfo.description}",</#if> interactive = true, echo = true)
        private ${modelInfo.type} ${modelInfo.filedName} <#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c} </#if>;
    </#list>
    @Override
    public Integer call() throws Exception {
    DataModel dataModel = new DataModel();
    BeanUtil.copyProperties(this, dataModel);
    System.out.println("配置信息" + dataModel);
    MainGenerator.doGenerator(dataModel);
    return 0;
    }
    }
