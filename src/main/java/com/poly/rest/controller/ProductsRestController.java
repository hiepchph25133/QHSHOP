package com.poly.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.entity.Product;
import com.poly.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductsRestController {
	@Autowired
	ProductService productService;

	@GetMapping("/list")
	public ResponseEntity<Page<Product>> getProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "18") int pageSize,
			@RequestParam(value = "keySearch", defaultValue = "") String keySearch
	) {
		Page<Product> products = productService.findAllProductsWithCondition(pageNumber, pageSize,keySearch);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping()
	public List<Product>  getAll() {
		return productService.findAll();
	}
	
	@GetMapping("{product_id}")
	public Product getOne(@PathVariable("product_id") Integer product_id) {
		return productService.findById(product_id);
	}
	
	@PostMapping
	public Product create(@RequestBody Product product) {
		return productService.create(product);
	}
	
	@PutMapping("{product_id}")
	public Product update(@PathVariable("product_id") 	Integer product_id
			,@RequestBody Product product) {
		return productService.update(product);
	}
	
	@DeleteMapping("{product_id}")
	public void delete(@PathVariable("product_id") Integer product_id) {
		 productService.delete(product_id);
	}
}
