package com.xinyue.utils;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

/**
 * 描述:
 * 
 * @author wang guang shuai 2016年12月8日 上午10:47:24
 */
public class XmlUtil {

	public static <T> T xmlToBean(String path, Class<T> t) {
		return xmlToBean(new File(path), t);
	}

	public static <T> T xmlToBean(File file, Class<T> t) {
		XStream stream = new XStream();
		stream.processAnnotations(t);
		@SuppressWarnings("unchecked")
		T result = (T) stream.fromXML(file);
		return result;
	}

	/**
	 * 
	 * 此方法是根据dom4j获取xml的document类
	 *
	 * @param file
	 * @return
	 * @throws DocumentException
	 * @author Terry
	 * @time 2016年5月11日
	 */
	public static Document getDocument(String path) throws DocumentException {
		File file = new File(path);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		return doc;
	}

	public static Document getDocument(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		return doc;
	}

}
