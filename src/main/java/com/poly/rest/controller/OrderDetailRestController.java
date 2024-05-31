package com.poly.rest.controller;

import java.util.List;

import com.poly.entity.Order;
import com.poly.entity.Product;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poly.entity.OrderDetail;
import com.poly.service.OrderDetailService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orderdetails")
public class OrderDetailRestController {
	@Autowired
	OrderDetailService orderDetailService;
	@Autowired
	ProductService productService;
	@Autowired
	OrderService orderService;
	
	@GetMapping("{id}")
	public List<OrderDetail> getAll(@PathVariable("id") Integer id){
		return orderDetailService.findByOrderID(id);
	}

	@PutMapping("{id}")
	public ResponseEntity<OrderDetail> update(@PathVariable("id") Integer id, @RequestBody OrderDetail orderDetail){
		orderDetail.setOrderdetail_id(id);
		OrderDetail old = orderDetailService.findOneByOrderDetailID(id);
		Product product = productService.findById(old.getProduct().getProduct_id());
		if(product.getQuantity() + old.getQuantity() < orderDetail.getQuantity()){
			return ResponseEntity.badRequest().build();
		}
		product.setQuantity(product.getQuantity() + old.getQuantity() - orderDetail.getQuantity());
		productService.update(product);
		Order order = orderService.findById(old.getOrder().getOrder_id());
		order.setPrice(old.getOrder().getPrice() - (old.getQuantity() * old.getProduct().getUnit_price()) + (orderDetail.getQuantity() * old.getProduct().getUnit_price()));
		orderService.update(order);
		old.setQuantity(orderDetail.getQuantity());
		old.setPrice(orderDetail.getQuantity() * old.getProduct().getUnit_price());
		orderDetailService.update(old);
		return ResponseEntity.ok(orderDetail);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id){
		OrderDetail old = orderDetailService.findOneByOrderDetailID(id);
		Product product = productService.findById(old.getProduct().getProduct_id());
		product.setQuantity(product.getQuantity() + old.getQuantity());
		productService.update(product);
		Order order = orderService.findById(old.getOrder().getOrder_id());
		order.setPrice(old.getOrder().getPrice() - (old.getQuantity() * old.getProduct().getUnit_price()));
		orderService.update(order);
		orderDetailService.delete(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("deleteAll/{id}")
	public ResponseEntity<?> cancel(@PathVariable("id") Integer id){
		List<OrderDetail> orderDetails = orderDetailService.findByOrderID(id);
		for(var item : orderDetails){
			Product product = productService.findById(item.getProduct().getProduct_id());
			product.setQuantity(product.getQuantity() + item.getQuantity());
			productService.update(product);
			orderDetailService.delete(item.getOrderdetail_id());
		}
		orderService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("")
	public ResponseEntity<OrderDetail> add(@RequestBody OrderDetail orderDetail){
		Product product = productService.findById(orderDetail.getProduct().getProduct_id());
		if(product.getQuantity() < orderDetail.getQuantity()){
			return ResponseEntity.badRequest().build();
		}
		product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
		productService.update(product);
		Order order = orderService.findById(orderDetail.getOrder().getOrder_id());
		order.setPrice(order.getPrice() + (orderDetail.getQuantity() * product.getUnit_price()));
		orderService.update(order);
		OrderDetail old = orderDetailService.findOneByOrderIDAndProductID(orderDetail.getOrder().getOrder_id(), orderDetail.getProduct().getProduct_id());
		if(old == null){
			orderDetail.setOrder(order);
			orderDetail.setProduct(product);
			orderDetail.setPrice(orderDetail.getQuantity() * product.getUnit_price());
			orderDetailService.add(orderDetail);
		}else{
			old.setOrder(order);
			old.setProduct(product);
			old.setPrice(old.getPrice() + (orderDetail.getQuantity() * product.getUnit_price()));
			old.setQuantity(old.getQuantity() + orderDetail.getQuantity());
			orderDetailService.update(old);
		}
		return ResponseEntity.ok(orderDetail);
	}
}