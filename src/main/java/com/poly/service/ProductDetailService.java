package com.poly.service;

import com.poly.entity.Category;
import com.poly.entity.ProductDetail;
import com.poly.entity.Voucher;

import java.util.List;

public interface ProductDetailService {

	List<ProductDetail> findAll();

	ProductDetail findById(Integer id);

	ProductDetail update(ProductDetail productDetail);

	void  deleteById(int id);

	ProductDetail create(ProductDetail productDetail);

	void deleteByProductID(Integer productID);

	ProductDetail findByProductID(Integer productID);

	public ProductDetail findByProductId(Integer productId);
}