package es.udc.pojo.minibank.web.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class Layout {
	
	@Property
	@Parameter(required = true, defaultPrefix = "message")
	private String pageTitle;
	
}
