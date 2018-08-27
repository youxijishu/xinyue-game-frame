package com.xinyue.web.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xinyue.web.app.service.IResourceService;
import com.xinyue.web.app.service.model.ResourceInfo;
import com.xinyue.web.app.service.model.ResponseResourceMessage;

@RestController
@RequestMapping("resource")
public class ResourceController {
	@Autowired
	private IResourceService resourceService;

	@RequestMapping("upload")
	@ResponseBody
	public Object upload(@RequestBody ResourceInfo resourceInfo, HttpServletRequest request) {

		ResponseResourceMessage message = resourceService.uploadResource(resourceInfo);
		return message;
	}

}
