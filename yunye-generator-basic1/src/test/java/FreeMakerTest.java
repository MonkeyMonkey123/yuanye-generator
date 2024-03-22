import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FreeMakerTest {
    @Test
   public void test() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("myweb.html.ftl");
        Map<String, Object> dataModel = new HashMap<String, Object>();
        dataModel.put("currentYear", 2023);
        List<Map<String,Object>> menuitems  = new ArrayList<Map<String,Object>>();
        Map<String,Object> menitem1 = new HashMap<String,Object>();
        Map<String, Object> menitem2 = new HashMap<>();
        menitem1.put("url", "http://codefather.cn");
        menitem1.put("label", "编程导航");
        menitem2.put("url", "http://laoyunjianli.com");
        menitem2.put("label", "简历模板");
        menuitems.add(menitem1);
        menuitems.add(menitem2);
        dataModel.put("menuItems", menuitems);
        FileWriter fileWriter = new FileWriter("myweb.html");
        template.process(dataModel, fileWriter);
        fileWriter.close();


    }
}
