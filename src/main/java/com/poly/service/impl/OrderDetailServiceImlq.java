package com.poly.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.poly.dao.*;
import com.poly.entity.ImageProduct;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.poly.entity.OrderDetail;
import com.poly.service.AccountService;
import com.poly.service.AuthorityService;
import com.poly.service.OrderDetailService;



@Service
public class OrderDetailServiceImlq implements OrderDetailService {
	@Autowired
	 OrderDetailDao dao;

	@Autowired
	private ImageProductDao imageProductDao;

	@Autowired
	private ProductDetailDao productDetailDao;

	@Override
	public List<OrderDetail> findByOrderID(Integer orderid) {
		List<OrderDetail> orderDetails = dao.findByOrderID(orderid).stream()
				.map(item -> {
					Product product = item.getProduct();
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					item.setProduct(product);
					return  item;
				})
				.collect(Collectors.toList());
		return orderDetails;

	}

	@Override
	public OrderDetail findOneByOrderDetailID(Integer orderDetailId) {
		return dao.findOneByOrderDetailId(orderDetailId);
	}

	@Override
	public void update(OrderDetail orderDetail) {
		dao.save(orderDetail);
	}

	@Override
	public void delete(Integer orderDetailId) {
		dao.deleteById(orderDetailId);
	}

	@Override
	public void add(OrderDetail orderDetail) {
		orderDetail.setOrderdetail_id(null);
		dao.save(orderDetail);
	}

	@Override
	public OrderDetail findOneByOrderIDAndProductID(Integer orderId, Integer productID) {
		return dao.findOneByOrderIdAndProductID(orderId, productID);
	}

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



}
