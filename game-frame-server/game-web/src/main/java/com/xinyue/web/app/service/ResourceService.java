package com.xinyue.web.app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xinyue.web.app.service.model.ResourceInfo;
import com.xinyue.web.app.service.model.ResponseResourceMessage;

@Service
public class ResourceService implements IResourceService {
	@Value("${unity.resource.url}")
	private String resourceUrl;

	@Override
	public ResponseResourceMessage uploadResource(ResourceInfo resourceInfo) {
		String fileName = resourceInfo.getResourceName();
		String filePath = this.getFilePath(resourceInfo.getResourceType(), fileName);
		File file = new File(filePath);

		ResponseResourceMessage message = new ResponseResourceMessage();
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(resourceInfo.getResourceBytes(), 0, resourceInfo.getResourceSize());
			out.close();
			message.setNewName(fileName);
			message.setOrgineName(fileName);
			message.setUrl(resourceUrl + "/unity_resource/" + resourceInfo.getResourceType() + "/" + fileName);
			return message;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getFilePath(String resourceType, String fileName) {
		String fileDir = "static/unity_resource/" + resourceType;
		File dir = new File(fileDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String filePath = fileDir + "/" + fileName;
		return filePath;
	}

}
