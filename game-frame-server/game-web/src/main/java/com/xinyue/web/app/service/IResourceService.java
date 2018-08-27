package com.xinyue.web.app.service;

import com.xinyue.web.app.service.model.ResourceInfo;
import com.xinyue.web.app.service.model.ResponseResourceMessage;

public interface IResourceService {

	ResponseResourceMessage uploadResource(ResourceInfo resourceInfo);
}
