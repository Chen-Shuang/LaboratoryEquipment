package word;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class PrintWord {

	/**
	 * 1、模板路径
	 * 2、存放路径以及名称
	 * 3、map集合
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public static void taskBook(String templetName, String outputFileName, Map<String, Object> dataMap, HttpServletResponse resp) throws TemplateException, IOException {

		Configuration configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
		
		configuration.setClassForTemplateLoading(PrintWord.class, "/word/");
		Template t=null;
		try {
			//test.ftl为要装载的模板
			t = configuration.getTemplate(templetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		resp.setHeader("Content-disposition",
                "attachment; filename=\""
                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");
		Writer out = null;
		out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(),"UTF-8"));
		//这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
		
		t.process(dataMap, out);
		out.flush();
		out.close();
	}
}
