package com.poly.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poly.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class Globallnterceptor implements HandlerInterceptor {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private TrademarkService trademarkService;

	@Autowired
	private SizeServices sizeServices;

	@Autowired
	private MaterialServices materialServices;

	@Autowired
	private ColorServices colorServices;
	// khai báo toàn chương trình hiển thị loại sản phẩm
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
						   ModelAndView modelAndView) throws Exception {
		request.setAttribute("cates", categoryService.findAll());
		request.setAttribute("trads", trademarkService.findAll());
		request.setAttribute("size", sizeServices.findAll());
		request.setAttribute("colors", colorServices.findAll());
		request.setAttribute("materials", materialServices.findAll());
	}


}