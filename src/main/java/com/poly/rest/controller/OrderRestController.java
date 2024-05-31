package com.poly.rest.controller;

import java.util.List;

import com.poly.dtos.BillDTO;
import com.poly.dtos.FileDTO;
import com.poly.entity.*;
import com.poly.exceptions.MoneyNotEnoughException;
import com.poly.mappers.JsonMapper;
import com.poly.service.*;
import com.poly.service.impl.ProductServiceImlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.poly.dao.OrderDao;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@CrossOrigin("*")
@RestController
public class OrderRestController {
	@Autowired
	OrderDao odao;
	@Autowired
	OrderService orderService;
	@Autowired
	ReportService reportService;
	@Autowired
	VoucherService voucherService;

	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	ProductServiceImlq productService;

	@GetMapping("/rest/orders")
	public List<Order> getAll(){
		return orderService.findByAllDesc();
	}
	@GetMapping("/rest/ordersstatus")
	public List<Order> getStatus(){
		return odao.getStatus();
	}

	@GetMapping("/rest/orders/pending")
	public List<Order> getAllOrderPending(){
		return orderService.findByStatus(5);
	}

	@GetMapping("/rest/orders/pending/{id}")
	public Order getAllOrderPendingById(@PathVariable("id") int idOrder){
		return orderService.findById(idOrder);
	}


	@PostMapping("/rest/orders")
	public Order create(@RequestBody JsonNode orderData,@RequestParam("code") String code) throws Throwable {
		return orderService.create(orderData,code);
	}

	@PostMapping("/rest/orders/sell")
	public FileDTO createOrderOfSell(@RequestBody JsonNode orderData,@RequestParam("code") String code) throws Throwable {
		var billDTO = JsonMapper.convertToBillDTO(orderData);
		if(billDTO.getOrder().getMoney_give()<(billDTO.getOrder().getPrice()-billDTO.getOrder().getVoucher_price()))
			throw  new MoneyNotEnoughException("Money not enough");
		String pdfPath= reportService.createPdf(billDTO);
		orderService.createBillSell(billDTO,code);
		FileDTO fileDTO = new FileDTO(pdfPath);
		return fileDTO;
	}

	@PutMapping("/rest/orders/pending/update")
	public FileDTO updateOrderPendding(@RequestBody Order order) throws Throwable {
		order.setPrice(order.getPrice()+order.getVoucher_price());
		var billDTO = BillDTO.builder().order(order).build();
		List<OrderDetail> orderDetails = orderDetailService.findByOrderID(order.getOrder_id());
		billDTO.setOrderDetails(orderDetails);
		if(billDTO.getOrder().getMoney_give()<(billDTO.getOrder().getPrice()-billDTO.getOrder().getVoucher_price()))
			throw  new MoneyNotEnoughException("Money not enough");
		String pdfPath= reportService.createPdf(billDTO);
		orderService.update(order);
		FileDTO fileDTO = new FileDTO(pdfPath);
		return fileDTO;
	}

	@PostMapping("/rest/orders/sell/status")
	public boolean createOrderOfSellStatusPending(@RequestBody JsonNode orderData,@RequestParam("code") String code) throws Throwable {
		var billDTO = JsonMapper.convertToBillDTO(orderData);
		orderService.createBillSellPending(billDTO,code);
		return true;
	}

	@PutMapping("/rest/orders/{id}")
	public Order put(@PathVariable("id")Integer id,@RequestBody Order order) {
		return  orderService.update(order);
	}

	@DeleteMapping("/rest/orders/{id}")
	public Order delete(@PathVariable("id")Integer id) {
		orderService.deleteById(id);
		return new Order();
	}

	@PostMapping("/rest/orderpending/edit/{order_id}")
	public FileDTO updateOrderPendding(Model model, @PathVariable("order_id") Integer order_id, @RequestBody Order ord) throws Throwable {
		Order old = orderService.findById(order_id);
		Account account = old.getAccount();
		account.setFullname(ord.getAccount().getFullname());
		old.setPhone(ord.getPhone());
		old.setMoney_give(ord.getMoney_give());
		old.setDescription(ord.getDescription());
		old.setAddress(ord.getAddress());
		old.setStatus(3);
		orderService.update(old);
		var billDTO = BillDTO.builder().order(old).build();
		List<OrderDetail> orderDetails = orderDetailService.findByOrderID(old.getOrder_id());
		billDTO.setOrderDetails(orderDetails);
		if(billDTO.getOrder().getMoney_give()<(billDTO.getOrder().getPrice()-billDTO.getOrder().getVoucher_price()))
			throw new MoneyNotEnoughException("Money not enough");
		String pdfPath= reportService.createPdf(billDTO);
		FileDTO fileDTO = new FileDTO(pdfPath);
		return fileDTO;
	}







//	@PostMapping("/rest/order/add")
//	public String addOrder(){
//
//	}
}