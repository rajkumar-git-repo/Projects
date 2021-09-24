package com.enterprise.path;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/enterprise")
public class EnterpriseApplication extends Application{

	 public Set<Class<?>> getClasses() {
	        return Collections.emptySet();
	    }
	 
}
