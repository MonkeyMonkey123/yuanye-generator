package com.yunye.maker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataModel {
    private boolean loop;

    private String author = "yunye";


    private String outputText = "sum = ";

    public DataModel() {

    }
}
