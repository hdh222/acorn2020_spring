package test.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MessengerAspect {
	
	//리턴 타입은 void 이고 send로 시작하는 모든메소드가 point cut이다.
	@Around("execution(void send*(..))")
	public void checkGreeting(ProceedingJoinPoint joinPoint) throws Throwable {
	//aop가 적용된 메소드 수행하기 이전에 해야할 작업
		
	Object[] args = joinPoint.getArgs();
	for(Object tmp : args) {
		//만약에 인자가 String type 이면
		if(tmp instanceof String) {
			String msg = (String)tmp;
			System.out.println("aspect에서 읽어낸 내용 : " + msg);
			if(msg.contains("바보")) {
				System.out.println("바보는 금지된 단어입니다.");
				return;
			}
		}
	}
	joinPoint.proceed(); // aop가 적용된 메소드 수행하기
	
	//aop가 적용된 메소드 후행된 이후에 해야할 작업
		
	}
	@Around("execution(String get*())")
	public Object checkReturn(ProceedingJoinPoint joinPoint) throws Throwable{
		//aop가 적용된 메소드가 리턴하는 데이터 얻어내기
		Object obj = joinPoint.proceed();
		//aop가 적용된 메소드가 리턴하는 데이터
		return "놀자놀자";
		
	}
}
//getArgs()로 전달된 인자의 목록을 동적으로 얻을 수 있다. 