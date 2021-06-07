package com.company.generator.test;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestUtil {

    static String ftlPath = "generator\\src\\main\\java\\com\\company\\generator\\test\\";
    static String toPath = "generator\\src\\main\\java\\com\\company\\generator\\test\\";

    public static void main(String[] args) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setDirectoryForTemplateLoading(new File(ftlPath));// 读模板路径
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_30));
        Template temp = cfg.getTemplate("test.ftl");// 具体读哪个模板

        FileWriter fw = new FileWriter(toPath + "Test.java");// 生成的文件
        BufferedWriter bw = new BufferedWriter(fw);
        temp.process(null, bw);
        bw.flush();
        fw.close();
    }
}
