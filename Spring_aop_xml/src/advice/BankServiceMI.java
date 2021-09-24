package advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class BankServiceMI implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		System.out.println("MI BEFORE");
		Object obj = mi.proceed();
		System.out.println("MI AFTER");
		return obj;
	}

}
