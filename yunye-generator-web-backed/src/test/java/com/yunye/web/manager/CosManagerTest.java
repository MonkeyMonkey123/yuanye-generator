package com.yunye.web.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Arrays;

@SpringBootTest
class CosManagerTest {

    @Resource
    private CosManager cosManager;
    @Test
    void deleteObject() {
        cosManager.deleteObject("/test/1.png");

    }

    @Test
    void deleteObjects() {
        cosManager.deleteObjects(Arrays.asList("test/20150504044555325.jpg", "test/49f8564668b4dee.jpeg"));

    }

    @Test
    void deleteDir() {
        cosManager.deleteDir("/test/");

    }
}