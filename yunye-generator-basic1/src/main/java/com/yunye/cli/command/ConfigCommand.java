package com.yunye.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.yunye.model.MainTemplateConfig;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;

@Command(name = "config", description = "查看参数细信息", mixinStandardHelpOptions = true)

public class ConfigCommand implements Runnable {


    @Override
    public void run() {
        System.out.println("查看参数信息");
        //获取打印的属性信息的类
//        Class<MainTemplateConfig> mainTemplateConfigClass = MainTemplateConfig.class;
//        Field[] fields = mainTemplateConfigClass.getFields();
        Field[] fields = ReflectUtil.getFields(MainTemplateConfig.class);
        for (Field field : fields) {
            System.out.println("字段名称： " + field.getName());
            System.out.println("字段类型：" + field.getType());
            System.out.println("Modifiers:" + field.getModifiers());
            System.out.println("--");

        }

    }
}
