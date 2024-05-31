package com.poly.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.poly.dao.*;
import com.poly.dtos.BillDTO;
import com.poly.entity.*;
import com.poly.exceptions.QuantityNotEnoughException;
import com.poly.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.poly.service.OrderService;

import javax.transaction.Transactional;

@Service
public class OrderServiceImlq implements OrderService {
	@Autowired
	OrderDao dao;
	@Autowired
	OrderDetailDao ddao;
	@Autowired
	AccountDao accountDao;
	@Autowired
	VoucherDao voucherDao;
	@Autowired
	VoucherDetailDao voucherDetailDao;
	@Autowired
	ProductService productService;

	@Autowired
	ProductDao productDao;

	@Override
	@Transactional
	public Order create(JsonNode orderData,String code) throws Throwable {
		VoucherDetail voucherDetail = new VoucherDetail();
		ObjectMapper mapper= new ObjectMapper();
		Order order = mapper.convertValue(orderData, Order.class);
		TypeReference<List<OrderDetail>> type =new TypeReference<List<OrderDetail>>() {};
		List<OrderDetail> details=mapper.convertValue(orderData.get("orderDetails"),type )
				.stream().peek(d ->d.setOrder(order)).collect(Collectors.toList());
		Voucher voucher = null;
		if(!code.equals("")){
			voucher = voucherDao.findByVoucherName(code);
			if(Objects.isNull(voucher)) throw  new Exception("Voucher not found!");
			else {
				order.setPrice(order.getPrice()-voucher.getVoucher_price());
				voucherDetail.setVoucher_id(voucher.getVoucher_id());
			}
		}
		var errors = productService.checkQuantityEnough(details);
		if(errors.getErrors().size()>0){
			ObjectMapper objectMapper = new ObjectMapper();
			String error = objectMapper.writeValueAsString(errors.getErrors());
			throw  new QuantityNotEnoughException(error);
		}
		productService.saveAll(errors.getProducts());

		Order orderNew = dao.save(order);

		if(Objects.nonNull(voucher)) voucherDetail.setOrder_id(orderNew.getOrder_id());
		voucherDetailDao.save(voucherDetail);
		ddao.saveAll(details);
		return order;

	}

	@Override
	@Transactional
	public Order createBillSellPending(BillDTO billDTO,String code) throws Throwable {
		Account account = new Account();
		VoucherDetail voucherDetail = new VoucherDetail();
		var order = billDTO.getOrder();
		UUID randomUUID = UUID.randomUUID();
		var fullname = order.getAccount().getUsername()!=null
				?order.getAccount().getUsername()+"_"+randomUUID:"Anonymous"+"_"+randomUUID;
		account.setUsername("Anonymous");
		account.setFullname(fullname);
		account.setActive(false);
		Account newAccount = accountDao.save(account);
		order.setAccount(newAccount);
		order.setStatus(5);
		Voucher voucher = null;
		if(!code.equals("")){
			voucher = voucherDao.findByVoucherName(code);
			if(Objects.isNull(voucher)) throw  new Exception("Voucher not found!");
			else {
				order.setPrice(order.getPrice()-voucher.getVoucher_price());
				voucherDetail.setVoucher_id(voucher.getVoucher_id());
			}
		}
		var errors = productService.checkQuantityEnough(billDTO.getOrderDetails());
		if(errors.getErrors().size()>0){
			ObjectMapper objectMapper = new ObjectMapper();
			String error = objectMapper.writeValueAsString(errors.getErrors());
			throw  new QuantityNotEnoughException(error);
		}
		productService.saveAll(errors.getProducts());
		var newOrder = dao.save(order);
		if(Objects.nonNull(voucher)) voucherDetail.setOrder_id(newOrder.getOrder_id());
		List<OrderDetail> details = billDTO.getOrderDetails();
		ddao.saveAll(details);
		return newOrder;
	}

	@Override
	@Transactional
	public Order createBillSell(BillDTO billDTO,String code) throws Throwable {
		VoucherDetail voucherDetail = new VoucherDetail();
		var order = billDTO.getOrder();
		order.setStatus(3);
		Voucher voucher = null;
		if(!code.equals("")){
			voucher = voucherDao.findByVoucherName(code);
			if(Objects.isNull(voucher)) throw  new Exception("Voucher not found!");
			else {
				order.setPrice(order.getPrice()-voucher.getVoucher_price());
				voucherDetail.setVoucher_id(voucher.getVoucher_id());
			}
		}
		var errors = productService.checkQuantityEnough(billDTO.getOrderDetails());
		if(errors.getErrors().size()>0){
			ObjectMapper objectMapper = new ObjectMapper();
			String error = objectMapper.writeValueAsString(errors.getErrors());
			throw  new QuantityNotEnoughException(error);
		}
		productService.saveAll(errors.getProducts());
		var newOrder = dao.save(order);
		if(Objects.nonNull(voucher)) voucherDetail.setOrder_id(newOrder.getOrder_id());
		List<OrderDetail> details = billDTO.getOrderDetails();
		ddao.saveAll(details);
		return newOrder;
	}

	@Override
	public Order findById(Integer id) {

		return dao.findById(id).get();
	}

	@Override
	public List<Order> findByUsername(String username) {

		return dao.findByUsername(username);
	}

	@Override
	public List<Order> findByAllDesc() {
		return dao.findByAllDesc();
	}
	@Override
	public void deleteById(Integer id) {
		ddao.deleteOrderId(id);
		dao.deleteById(id);
	}
	@Override
	public Order update(Order order) {
		return dao.save(order);
	}

	@Override
	public List<Order> findByStatus(int status) {
		return dao.findByStatusPending(status);
	}

	@Override
	public BillDTO getBillDetail(int idOrder) {
		Order order = dao.findById(idOrder).get();
		List<OrderDetail> orderDetails = ddao.findByOrder(idOrder);
		BillDTO billDTO = BillDTO.builder().orderDetails(orderDetails).order(order).build();
		return billDTO;
	}


	@Override
	@Transactional
	public void addProductToOrder(Order order, Product product, int quantity) {
		// Kiểm tra xem sản phẩm đã có trong OrderDetail chưa
		OrderDetail existingOrderDetail = findOrderDetailByOrderAndProduct(order, product);

		if (existingOrderDetail == null) {
			// Tạo một bản ghi mới cho sản phẩm trong OrderDetail
			OrderDetail newOrderDetail = new OrderDetail();
			newOrderDetail.setOrder(order);
			newOrderDetail.setProduct(product);
			newOrderDetail.setQuantity(quantity);
			newOrderDetail.setPrice(product.getUnit_price() * quantity);
			ddao.save(newOrderDetail);
		} else {
			// Sản phẩm đã có trong OrderDetail, cập nhật số lượng
			int newQuantity = existingOrderDetail.getQuantity() + quantity;
			existingOrderDetail.setQuantity(newQuantity);
			existingOrderDetail.setPrice(product.getUnit_price() * newQuantity);
			ddao.save(existingOrderDetail);
		}

		// Giảm số lượng của sản phẩm trong bảng Product
		int remainingQuantity = product.getQuantity() - quantity;
		if (remainingQuantity < 0) {
			throw new RuntimeException("Không đủ hàng trong kho");
		}
		product.setQuantity(remainingQuantity);
		productDao.save(product);
	}

	// Tìm kiếm OrderDetail theo Order và Product
	private OrderDetail findOrderDetailByOrderAndProduct(Order order, Product product) {
		List<OrderDetail> orderDetails = order.getOrderDetails();
		for (OrderDetail orderDetail : orderDetails) {
			if (orderDetail.getProduct().equals(product)) {
				return orderDetail;
			}
		}
		return null;
	}

	@Override
	public Order findOrderById(Integer orderId) {
		return dao.findById(orderId).orElse(null);
	}
	@Override
	public void updateOrder(Order order) {
		dao.save(order);
	}
}