package com.poly.service;

import com.poly.entity.ImageProduct;
import com.poly.entity.ProductDetail;

import java.util.List;

public interface ImageProductService {

	List<ImageProduct> findAll();

	ImageProduct findById(Integer id);

	void update(List<ImageProduct> images,Integer productI);

	void  deleteById(int id);

	ImageProduct create(ImageProduct imageProduct);
	void saveAll(List<ImageProduct> images);

	void deleteByProductID(Integer productID);
}
