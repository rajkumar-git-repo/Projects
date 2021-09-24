/**
 * 
 */
package com.enterprise.exception;


/**
 * {@link EnterpriseException} is user define exception
 * @author rajkumar
 *
 */
public class EnterpriseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EnterpriseException(String meassage){
		super(meassage);
	}
}
