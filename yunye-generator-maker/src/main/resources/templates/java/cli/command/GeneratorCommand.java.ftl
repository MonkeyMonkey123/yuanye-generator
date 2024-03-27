package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
<#--生成选项-->
<#macro generatorOption indent modelInfo>
    ${indent}@Option(names = {<#if modelInfo.abbr??> "-${modelInfo.abbr}",</#if> "--${modelInfo.filedName}"}, arity = "0..1", <#if modelInfo.description??>description = "${modelInfo.description}",</#if> interactive = true, echo = true)
    ${indent}private ${modelInfo.type} ${modelInfo.filedName} <#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c} </#if>;
</#macro>
<#macro generateCommand indent modelInfo>
    ${indent}System.out.println("输入${modelInfo.groupName}配置信息");
    ${indent}CommandLine commandLine = new CommandLine(${modelInfo.type}Command.class);
    ${indent}commandLine.execute(${modelInfo.allArgsStr});
</#macro>
@Data
@Command(name = "generate", mixinStandardHelpOptions = true, description = "生成代码")

public class GeneratorCommand implements Callable<Integer> {

    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
            /**
            *${modelInfo.groupName}
            */
            static DataModel.${modelInfo.type} ${modelInfo.groupKey} = new DataModel.${modelInfo.type}();
            @Command(name = "${modelInfo.groupName}",description = "${modelInfo.description}",mixinStandardHelpOptions = true)
            @Data
            static class ${modelInfo.type}Command implements Runnable{
            <#list modelInfo.models as subModelInfo>
                <@generatorOption indent="            " modelInfo=subModelInfo />

            </#list>

            @Override
            public void run() {
            <#list modelInfo.models as subModelInfo>
             ${modelInfo.groupKey}.${subModelInfo.filedName} = ${subModelInfo.filedName};
            </#list>
            }
            }
            <#else>
                <@generatorOption indent="        " modelInfo=modelInfo />
        </#if>

    </#list>
<#--    生成调用方法-->
    @Override
    public Integer call() throws Exception {
    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
            <#if modelInfo.condition??>
                if(${modelInfo.condition}){
               <@generateCommand indent="                 " modelInfo=modelInfo />
                }
                <#else >
                    <@generateCommand indent="        " modelInfo=modelInfo />
            </#if>
        </#if>
    </#list>
    DataModel dataModel = new DataModel();
    BeanUtil.copyProperties(this, dataModel);
    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
            dataModel.${modelInfo.groupKey} = ${modelInfo.groupKey};
        </#if>
    </#list>
    System.out.println("配置信息" + dataModel);
    MainGenerator.doGenerator(dataModel);
    return 0;
    }
    }
