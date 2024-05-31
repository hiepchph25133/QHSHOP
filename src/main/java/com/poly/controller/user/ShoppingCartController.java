package com.poly.controller.user;

import com.poly.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShoppingCartController {
	@Autowired
	VoucherService voucherService;
	
	@RequestMapping("/cart/view")
	public String cart(Model model) {
		model.addAttribute("vouchers", voucherService.findAll());
		return "user/cart/view";
	}
	
}
