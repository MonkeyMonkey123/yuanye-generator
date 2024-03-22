# ${name}
> ${description}
> 
> 作者：${author}
>   
>   可以通过输入行交互式输入方式动态的生成代码
## 使用说明
执行项目根目录的下的脚本文件

 ```
generator <命令> <选项参数>
 ```
示例命令：
```
generator generate<#list modelConfig.models ad modelInfo>-${modelInfo.abbr}</#list>

```
##参数说明
<#list modelConfig.models as modelInfo>
${modelInfo?index+1}）${modelInfo.filedName}
类型：${modelInfo.type}
描述：${modelInfo.description}
默认值：${modelInfo.defaultValue?c}
缩写： -${modelInfo.abbr}

</#list>



