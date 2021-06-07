package com.company.generator.server;

import com.company.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;

import java.io.IOException;

public class ServerGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String toPath = "generator\\src\\main\\java\\com\\company\\generator\\test\\";
        FreemarkerUtil.initConfig("test.ftl");
        FreemarkerUtil.generator(toPath + "Test.java");
    }
}
