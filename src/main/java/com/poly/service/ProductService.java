package com.poly.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.poly.dtos.ProductEnoughQuantityDTO;
import com.poly.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.Product;

public interface ProductService {

	List<Product> findByCategoryId(Integer integer);
	List<Product> findByLaptop();
	List<Product> findAll();

	List<Product> finbyIdOrName( String keywords);

	List<Product> findByAllKeyWordAdmin(Integer unit_price, Integer unit_price1   ,
										String Category_id ,String Trademark_id ,
										String Status );

	Product findById(Integer id);


	void saveAll(List<Product> products);
	
	Product create(Product product);

	Product update(Product product);

	void delete(Integer id);

	List<Product> findByNameContaining(String name);

	List<Product> findByKeywords(String keywords);

	List<Product> findByTrademarkId(Integer integer);
	File save(MultipartFile file, String path) throws IOException;

	Page<Product> findAllProductsWithCondition(int pageNumber, int pageSize, String keySearch);

	ProductEnoughQuantityDTO checkQuantityEnough(List<OrderDetail> orderDetails) throws Throwable;
	

}
