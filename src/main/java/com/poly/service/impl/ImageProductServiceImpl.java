package com.poly.service.impl;

import com.poly.dao.ImageProductDao;
import com.poly.dao.ProductDetailDao;
import com.poly.entity.ImageProduct;
import com.poly.entity.ProductDetail;
import com.poly.service.ImageProductService;
import com.poly.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ImageProductServiceImpl implements ImageProductService {
	@Autowired
	ImageProductDao  dao;

	@Override
	public List<ImageProduct> findAll() {
		return dao.findAll();
	}

	@Override
	public ImageProduct findById(Integer id) {
		return dao.findById(id).get();
	}


	@Override
	@Transactional
	@Modifying
	public void update(List<ImageProduct> images,Integer productID) {
	  if(!images.isEmpty()){
		  dao.deleteByProductID(productID);
		  dao.saveAll(images);
	  }
	}


	@Override
	public void deleteById(int id) {
		dao.deleteById(id);
	}


	@Override
	public ImageProduct create(ImageProduct imageProduct) {
		return dao.save(imageProduct);
	}

	@Override
	public void saveAll(List<ImageProduct> images) {
		dao.saveAll(images);
	}

	@Override
	@Transactional
	@Modifying
	public void deleteByProductID(Integer productID) {
		dao.deleteByProductID(productID);
	}

}
