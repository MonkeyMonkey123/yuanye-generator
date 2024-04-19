package com.yunye.maker.main;

import freemarker.template.TemplateException;

import java.io.IOException;

public class ZipGenerator extends GenerateTemplate{


    @Override
    protected String buildDest(String outputPath, String sourceCopyDestPath, String shellOutputPath, String jarPath) {
        String distPath = super.buildDest(outputPath, sourceCopyDestPath, shellOutputPath, jarPath);
        return super.buildZip(distPath);
    }
}
