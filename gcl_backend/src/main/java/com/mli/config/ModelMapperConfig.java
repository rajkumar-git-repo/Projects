package com.mli.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Devendra.Kumar
 * @version 1.0
 * 
 *          Spring Bean configuration class for ModelMapper
 *
 */
@Component
public class ModelMapperConfig {

	/**
	 * Create a bean of Model Mapper and register into Spring Context
	 */
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
