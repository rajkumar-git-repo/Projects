package service;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class BankService {
	
	@Pointcut("execution(* BankBusiness.*(..))")
    public void pname() {};
    
    @AfterThrowing(pointcut = "execution(* Employee.*(..))", throwing = "error")
    public void beforAdvice(JoinPoint jp) {
    	System.out.println("Method Before Advice");
    }
}
