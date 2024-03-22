package com.yunye.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MainTemplateConfig {
    private boolean loop;

    private String author = "yunye";


    private String outputText = "sum = ";

    public MainTemplateConfig() {

    }
}
