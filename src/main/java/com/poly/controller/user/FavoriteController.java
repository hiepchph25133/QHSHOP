package com.poly.controller.user;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.poly.dao.ImageProductDao;
import com.poly.dao.ProductDetailDao;
import com.poly.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.dao.FavoriteDao;
import com.poly.service.FavoriteService;

@Controller
public class FavoriteController {
	@Autowired
	FavoriteService fadao;
	
	@Autowired
	FavoriteDao dao;

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

	@RequestMapping("/favolist/user")
	public String favolist(Model model , HttpServletRequest request) {
		String username = request.getRemoteUser();
		List<Favorite> list = dao.getListUserFavorite(username).stream()
				.map(item -> {
					Product product = item.getProduct();
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					item.setProduct(product);
					return  item;
				})
				.collect(Collectors.toList());
		model.addAttribute("listfavo", list);
		 return "user/favo/list";
	}
	
	@RequestMapping("/favolist/remove/{product_id}")
	public String removefavo(Model model,@PathVariable("product_id") Integer product_id ,HttpServletRequest request ){
		String username = request.getRemoteUser();
		dao.deleteFavaritesAdmin(product_id, username);
		 return "redirect:/favolist/user";
	}
	
	@RequestMapping("/add/favo/{product_id}")
	public String addFavo(Model model ,@PathVariable("product_id") Integer product_id ,HttpServletRequest request ) {
		String username = request.getRemoteUser();
		Favorite addlist = new Favorite();
		Account acc = new Account();
		acc.setUsername(username);
		Product pro = new Product();
		pro.setProduct_id(product_id);
	
			addlist.setFavorite_date(new Date());
			addlist.setAccount(acc);;
			addlist.setProduct(pro);
			dao.save(addlist);
			model.addAttribute("statusfavo", "Đã thích");
			return "redirect:/product/detail/"+product_id;
		
	}
	
	@RequestMapping("/delete/favo/{product_id}")
	public String deletefavo(Model model,@PathVariable("product_id") Integer product_id ,HttpServletRequest request ){
		String username = request.getRemoteUser();
		dao.deleteFavaritesAdmin(product_id, username);
		model.addAttribute("statusfavo", "Chưa thích");
		return "redirect:/product/detail/"+product_id;
	}
}
