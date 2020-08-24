package com.gura.spring05.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gura.spring05.cafe.dao.CafeDao;
import com.gura.spring05.cafe.dto.CafeDto;
import com.gura.spring05.exception.NotDeleteException;
import com.gura.spring05.file.dao.FileDao;
import com.gura.spring05.file.dto.FileDto;

@Aspect
@Component
public class DeleteAspect {
	
	@Autowired
	private FileDao fileDao;

	@Autowired
	private CafeDao cafeDao;
	
	@Around("execution(void com.gura.spring05.file.service.*.delete*(..))")	//test.file.service 에 있는 모든 클래스 안에 delete로 시작하는 모든 메소드
	public void checkDelete(ProceedingJoinPoint joinPoint) throws Throwable{
		int num = 0;
		HttpServletRequest request = null;
		Object[] args = joinPoint.getArgs();
		for(Object tmp : args) {
			if(tmp instanceof Integer) {	//전달된 인자중에서 정수(int)찾기
				num = (int)tmp;
			}
			if(tmp instanceof HttpServletRequest) {		//HttpServletRequest 찾기
				request = (HttpServletRequest)tmp;
			}
		}
		//삭제할 파일의 정보를 얻어온다.
		FileDto fileDto = fileDao.getData(num);
		//세션에 저장된 아이디를 읽어온다(로그인된 아이디)
		String id = (String)request.getSession().getAttribute("id");
		//만일 로그인된 아이디와 글 작성자가 다르면
		if(!id.equals(fileDto.getWriter())) {
			throw new NotDeleteException("남의 파일 지우기 없기!");
		}
		
		joinPoint.proceed();
		
	}
	@Around("execution(void com.gura.spring05.cafe.service.*.delete*(*, *))")
	public void checkContentDelete(ProceedingJoinPoint joinPoint) throws Throwable {
	
		int num = 0;
		HttpServletRequest request = null;
		Object[] args = joinPoint.getArgs();
		
		for(Object tmp : args) {
			if(tmp instanceof Integer) {
				num = (int)tmp;
			}
			if(tmp instanceof HttpServletRequest) {
				request = (HttpServletRequest)tmp;
			}
		}
		//1. 삭제할 글의 정보를 읽어온다.
		CafeDto dto=cafeDao.getData(num);
		//2. 본인이 작성한 글이 아닌경우 에러 처리를한다 (예외를 발생시킨다)
		String id=(String)request.getSession().getAttribute("id");
		//만일 로그인된 아이디와 글 작성자가 다르면
		if(!id.equals(dto.getWriter())) {
			throw new NotDeleteException("남의 글 지우기 없기!");
		}		// TODO Auto-generated method stub
		
		joinPoint.proceed();
		
	}
}
