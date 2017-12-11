package org.smap.service;

public enum ResponseType
{
	TextPlain("text/plain"),
	ApplicationJson("application/json"),
	ApplicationPdf("application/pdf");

	
	private String type;
	
	ResponseType(String type)
	{
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
