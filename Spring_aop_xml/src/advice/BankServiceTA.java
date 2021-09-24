package advice;

import java.lang.reflect.Method;
import org.springframework.aop.ThrowsAdvice;

public class BankServiceTA implements ThrowsAdvice{

	public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
		System.out.println("Thrwos Advice");
	}

	
}
