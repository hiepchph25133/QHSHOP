package com.poly.controller.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.poly.dao.ImageProductDao;
import com.poly.dao.ProductDetailDao;
import com.poly.entity.ImageProduct;
import com.poly.entity.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.dao.AccountDao;
import com.poly.dao.ProductDao;
import com.poly.entity.Account;
import com.poly.entity.Product;
import com.poly.service.AccountService;

@Controller
public class HomeController {
	@Autowired
	ProductDao pdao;
	@Autowired
	AccountService accservice;

	@Autowired
	private ImageProductDao imageProductDao;

	@Autowired
	private ProductDetailDao productDetailDao;

	public void updateInformationProduct(Product product, ProductDetail detail , List<ImageProduct> images){
		if(detail!=null){
			product.setDetail(detail.getDetail());
			product.setDescription(detail.getDescription());

		}
		for(int i = 0 ; i <images.size();i++){
			if(i==0) product.setImage1(images.get(0).getPath());
			if(i==1) product.setImage2(images.get(1).getPath());
			if(i==2) product.setImage3(images.get(2).getPath());
			if(i==3) product.setImage4(images.get(3).getPath());
			if(i==4) product.setImage5(images.get(4).getPath());
			if(i==5) product.setImage6(images.get(5).getPath());
			if(i==6) product.setImage7(images.get(6).getPath());
		}

	}

	@Autowired
	AccountDao dao;
	@RequestMapping("/home/index")
	public String home(Model model) {
		List<Product> list = pdao.findByAllDis().stream()
				.filter(product -> product.getQuantity() > 0)
				.collect(Collectors.toList()).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("item1", list);
		List<Product> list1 = pdao.findByAllSpe().stream()
				.filter(product -> product.getQuantity() > 0)
				.collect(Collectors.toList()).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("item2", list1);
		List<Product> list2 = pdao.getTop10().stream()
				.filter(product -> product.getQuantity() > 0)
				.collect(Collectors.toList()).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("item3", list2);
	
		return "user/home/index";
	}

}
