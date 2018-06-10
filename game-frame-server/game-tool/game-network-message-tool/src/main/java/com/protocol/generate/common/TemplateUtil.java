package com.protocol.generate.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateUtil {

	public static void build(Object map, String templateName, String outPath, String fileName) {
		File outDir = new File(outPath);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		String dirPath = "config/templates";
		File file = new File(dirPath);
		try {
			cfg.setDirectoryForTemplateLoading(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		try {
			Template template = cfg.getTemplate(templateName);
			outPath += "/" + fileName;
			File outFile = new File(outPath);
			OutputStream fos = new FileOutputStream(outFile);
			Writer out = new OutputStreamWriter(fos);
			template.process(map, out);
			fos.flush();
			fos.close();
			System.out.println("create file success -> " + outPath);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}

}
