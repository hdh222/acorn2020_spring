package com.gura.spring05.shop.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.gura.spring05.shop.dao.OrderDao;
import com.gura.spring05.shop.dao.ShopDao;
import com.gura.spring05.shop.dto.OrderDto;
import com.gura.spring05.shop.dto.ShopDto;

@Service
public class ShopServiceImpl implements ShopService{
	
	@Autowired
	private ShopDao shopDao;
	@Autowired
	private OrderDao orderDao;

	@Override
	public void getList(ModelAndView mView) {
		List<ShopDto> list = shopDao.getList();
		mView.addObject("list", list);
	}
	@Transactional
	@Override
	public void buy(HttpServletRequest request, ModelAndView mView) {
		//구입자의 아이디
		String id = (String)request.getSession().getAttribute("id");
		//구입할 상품의 번호를 읽어온다.
		int num = Integer.parseInt(request.getParameter("num"));
		//상품의 가격을 얻어온다.
		int price = shopDao.getPrice(num);
		//상품의 가격만큼 계좌 잔액을 줄인다.
		
		ShopDto dto = new ShopDto();
		
		dto.setId(id);	//ooo의 계좌조회를 위한 id
		dto.setPrice(price);	//ooo의 계좌잔액을 줄이기위한 price
		
		shopDao.minusMoney(dto);
		//가격의 10%를 포인트로 적립한다.
		shopDao.plusPoint(dto);
		//재고의 개수를 1줄인다.
		shopDao.minusCount(num);
		//주문 테이블(배송) 에 정보를 추가한다.
		OrderDto dto2 = new OrderDto();
		dto2.setId(id);	//누가
		dto2.setCode(num);	//어떤 상품을
		dto2.setAddr("강남구 삼원빌딩 5층");	//어디로 배송할지
		orderDao.addOrder(dto2);
	}

}
