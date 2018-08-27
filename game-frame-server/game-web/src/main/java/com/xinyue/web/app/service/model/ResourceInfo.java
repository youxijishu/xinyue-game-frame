package com.xinyue.web.app.service.model;

public class ResourceInfo {

	private String resourceName;
	private int resourceSize;
	private String resourceType;
	private byte[] resourceBytes;

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getResourceSize() {
		return resourceSize;
	}

	public void setResourceSize(int resourceSize) {
		this.resourceSize = resourceSize;
	}

	public byte[] getResourceBytes() {
		return resourceBytes;
	}

	public void setResourceBytes(byte[] resourceBytes) {
		this.resourceBytes = resourceBytes;
	}

}
