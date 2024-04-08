package com.yunye.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

import java.util.ResourceBundle;

public class MetaManger {
    private static volatile Meta meta;

    private MetaManger() {

    }

    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (MetaManger.class) {
                if(meta==null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta() {
//        String meatJson = ResourceUtil.readUtf8Str("meta.json");
        String meatJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");

        Meta newMeta = JSONUtil.toBean(meatJson, Meta.class);
        Meta.FileConfigDTO fileConfig = newMeta.getFileConfig();
        //校验处理默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;

    }

}
