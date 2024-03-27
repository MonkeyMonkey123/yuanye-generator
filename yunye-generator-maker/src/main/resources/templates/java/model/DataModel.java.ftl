package ${basePackage}.model;
import lombok.Data;
/**
*数据模型
*/
<#macro generatorModel indent modelInfo>
    <#if modelInfo.description??>
        ${indent}/**
        ${indent}*${modelInfo.description}
        ${indent}*/
    </#if>
    ${indent}public ${modelInfo.type} ${modelInfo.filedName} <#if modelInfo.defaultValue??>= ${modelInfo.defaultValue?c}</#if>;
</#macro>
@Data
public class DataModel{
<#list modelConfig.models as modelInfo>
    <#if modelInfo.groupKey??>
        /**
        * <#if modelInfo.description??>${modelInfo.description}</#if>
        */
        public ${modelInfo.type} ${modelInfo.groupKey} = new ${modelInfo.type}() ;
        @Data
        public static class ${modelInfo.type} {
        <#list modelInfo.models as modelInfo>
            <@generatorModel indent="           " modelInfo=modelInfo/>
        </#list>
        }
    <#else>
        <@generatorModel indent="       " modelInfo=modelInfo/>
    </#if>
</#list>
}