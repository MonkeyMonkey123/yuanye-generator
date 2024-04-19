package  ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
* 读取json文件生成代码
*/
@Data
@Command(name = "json-generate", mixinStandardHelpOptions = true, description = "读取 json 文件生成代码")

public class JSONGeneratorCommand implements Callable<Integer> {

    @Option(names = {"-f", "--file"}, arity = "0..1", description = "json文件路径", interactive = true, echo = true)
    private String filePath;

    @Override
    public Integer call() throws Exception {
    //读取json文件转化为数据模型
    String jsonStr = FileUtil.readUtf8String(filePath);
    DataModel dataModel = JSONUtil.toBean(jsonStr, DataModel.class);
    System.out.println("配置信息" + dataModel);
    MainGenerator.doGenerator(dataModel);
    return 0;
    }
    }
