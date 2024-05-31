package com.poly.service.impl;

import com.poly.dao.ProductDetailDao;
import com.poly.dao.VoucherDao;
import com.poly.entity.ProductDetail;
import com.poly.entity.Voucher;
import com.poly.service.ProductDetailService;
import com.poly.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
	@Autowired
	ProductDetailDao dao;

	@Override
	public List<ProductDetail> findAll() {
		return dao.findAll();
	}

	@Override
	public ProductDetail findById(Integer id) {
		return dao.findById(id).get();
	}

	@Override
	public ProductDetail update(ProductDetail productDetail) {
		return dao.save(productDetail);
	}


	@Override
	public void deleteById(int id) {
		dao.deleteById(id);
	}

	@Override
	public ProductDetail create(ProductDetail productDetail) {
		return dao.save(productDetail);
	}

	@Override
	@Transactional
	@Modifying
	public void deleteByProductID(Integer productID) {
		dao.deleteByProductID(productID);
	}

	@Override
	public ProductDetail findByProductID(Integer productID) {
		return dao.findByProductID(productID);
	}



	@Override
	public ProductDetail findByProductId(Integer productId) {
		return dao.findByProductID(productId);
	}
}