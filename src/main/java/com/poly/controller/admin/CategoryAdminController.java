package com.poly.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.dao.AccountDao;
import com.poly.dao.CategoryDao;
import com.poly.dao.ProductDao;
import com.poly.entity.Account;
import com.poly.entity.Category;
import com.poly.entity.Product;
import com.poly.entity.Trademark;
import com.poly.service.AccountService;
import com.poly.service.CategoryService;
import com.poly.service.ProductService;
@Controller
public class CategoryAdminController {
	@Autowired
	CategoryService categoryService;
	@Autowired
	CategoryDao cdao;

	@RequestMapping("/admin/category/list")
	public String home(Model model) {
		
		return "admin/category/index";
	}
	
}
