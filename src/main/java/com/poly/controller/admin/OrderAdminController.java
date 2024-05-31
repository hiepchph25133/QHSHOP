package com.poly.controller.admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.poly.controller.user.OrderController;
import com.poly.dtos.BillDTO;
import com.poly.entity.Account;
import com.poly.service.OrderDetailService;
import com.poly.service.OrderService;
import com.poly.service.ProductService;
import com.poly.service.impl.ProductServiceImlq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.dao.OrderDao;
import com.poly.dao.OrderDetailDao;
import com.poly.dao.ProductDao;
import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import com.poly.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class OrderAdminController {
	@Autowired
	OrderDao odao;

	@Autowired
	OrderDetailDao otddao;
	@Autowired
	OrderDetailService service;



	@Autowired
	ProductDao prodao;
	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;


	@Autowired
	ProductServiceImlq productService1;

	private final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@GetMapping("/admin/order/list")
	public String index(Model model, HttpServletRequest request, RedirectAttributes redirect) {
		request.getSession().setAttribute("orderlist", null);
		if (model.asMap().get("success") != null)
			redirect.addFlashAttribute("success", model.asMap().get("success").toString());
		return "redirect:/admin/order/list/page/1";
	}

	@GetMapping("/admin/order/list/page/{pageNumber}")
	public String showProductPage(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orderlist");
		int pagesize = 9;
		List<Order> list = (List<Order>) odao.findByAllDesc();
		model.addAttribute("sizepro", list.size());
		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("orderlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/order/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "/admin/order/list";
	}

	//	@RequestMapping("/order/list")
//	public String index(Model model){
//		List<Order> list = odao.findByAllDesc();
//		model.addAttribute("orders", list);
//		return "admin/order/list";
//	}
	@GetMapping("/admin/order/findbyOrderId")
	public String search(@RequestParam("order_id") String order_id, Model model) {
		if (order_id.equals("")) {
			return "redirect:/admin/order/list";
		}
		model.addAttribute("items", odao.findByOrder_Id(order_id));
		return "list";
	}

	@RequestMapping("/admin/order/findbyOrderId/{pageNumber}")
	public String findIdorName(Model model, @RequestParam("order_id") String order_id, HttpServletRequest request,
							   @PathVariable int pageNumber) {
		if (order_id.equals("")) {
			return "redirect:/admin/order/list";
		}
		List<Order> list = odao.findByOrder_Id(order_id);
		if (list == null) {
			return "redirect:/admin/order/list";
		}
		model.addAttribute("sizepro", list.size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orderlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("orderlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/order/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "/admin/order/list";
	}


	@RequestMapping("/admin/order/findallkeyword")
	public String findallkeyword(Model model) {
		List<Order> list = odao.findAll();
		model.addAttribute("items", list);
		return "list";
	}

	@SuppressWarnings("deprecation")
	@RequestMapping("/admin/order/findByAllkeyword/{pageNumber}")
	public String findByAllkeyword(Model model, @RequestParam("Username") String username,
								   @RequestParam(value = "Minday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date minday,
								   @RequestParam(value = "Maxday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxday,
								   @RequestParam("Status") String Status, @RequestParam("Phone") String Phone,
								   @RequestParam("MinPrice") Integer unit_price, @RequestParam("MaxPrice") Integer unit_price1,
								   HttpServletRequest request, @PathVariable int pageNumber) {
		if (minday == (null)) {
			minday = new Date(2001 - 01 - 23);
		}
		if (maxday == (null)) {
			maxday = new Date();
		}

		List<Order> list = odao.findByAllKeyWord(username, minday, maxday, Status, Phone, unit_price, unit_price1);
		model.addAttribute("orders", list);
		model.addAttribute("sizepro", list.size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("orderlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("orderlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/admin/order/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "/admin/order/list";

	}



	@RequestMapping("/admin/order/delete/{order_id}")
	public String deleteOrder_Id(Model model, @PathVariable("order_id") Integer order_id) {
		otddao.deleteOrderId(order_id);
		odao.deleteById(order_id);
		return "admin/order/list";
	}

	@RequestMapping("/admin/order/delete/form/{order_id}")
	public String deleteformOrder_Id(Model model, @PathVariable("order_id") Integer order_id) {
		otddao.deleteOrderId(order_id);
		odao.deleteById(order_id);
		return "admin/order/list";
	}

	@RequestMapping("/admin/order/update")
	public String updateOrder(Model model, Order ord) {
		if (odao.existsById(ord.getOrder_id())) {
			if (ord.getStatus() == 1) {

				List<OrderDetail> listOrder = otddao.findByOrderID(ord.getOrder_id());
				for (int i = 0; i < listOrder.size(); i++) {
					Optional<Product> pro = prodao.findById(listOrder.get(i).getProduct().getProduct_id());
					pro.orElseThrow().setQuantity(pro.get().getQuantity() - listOrder.get(i).getQuantity());
					prodao.save(pro.get());
				}
			}

			if (ord.getStatus() == 2) {

				List<OrderDetail> listOrder = otddao.findByOrderID(ord.getOrder_id());
				for (int i = 0; i < listOrder.size(); i++) {
					Optional<Product> pro = prodao.findById(listOrder.get(i).getProduct().getProduct_id());
					pro.orElseThrow().setQuantity(pro.get().getQuantity() +  listOrder.get(i).getQuantity());
					prodao.save(pro.get());
				}
			}
			if (ord.getStatus() == 3) {
				ord.setDescription("Đã thanh toán");

			}
			model.addAttribute("message", "Thao tác thành công");

			odao.save(ord);
			return "redirect:/admin/order/edit?order_id=" + ord.getOrder_id();
		} else {
			model.addAttribute("message", "Thao tác thất bại");
			return "redirect:/admin/order/edit?order_id=" + ord.getOrder_id();
		}

	}
	@RequestMapping("/admin/order/sent/{order_id}")
	public String sendMail(Model model,@PathVariable("order_id") Integer order_id ) {
		String sta = "Đang sử lý";
		Order order = odao.findById(order_id).get();
		if (order.getStatus() == 1) {
			sta = "Đang giao hàng";
		}
		if (order.getStatus() == 2) {
			sta = "Hủy giao hàng";
		}
		if (order.getStatus() == 3) {
			sta = "Hoàn thành đơn hàng";
		}
		if (order.getStatus() == 4) {
			sta = "Đã hủy";
		}
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(order.getAccount().getEmail());
		msg.setText(
				"	               HÓA ĐƠN "   +"\n"
						+"\n"
						+ "   Tên người đặt hàng : " + order.getAccount().getFullname()          + "   \n"
						+ "   Phương thức thanh toán :  " + order.getMethod()                    + "   \n"
						+ "   Sô điện thoại người đặt hàng : " + order.getPhone()                + "   \n"
						+ "   Đơn hàng có giá trị :" + order.getPrice()                          + "   \n"
						+ "   Loại tiền tệ : " + order.getCurrency()                             + "   \n"
						+ "   Ngày tạo đơn : " + order.getCreateDate()                           + "   \n"
						+ "   Tình trạng đơn hàng : " + sta                                      + "   \n"
						+ "   Địa chỉ nhận hàng : " + order.getAddress() + "\n"
						+ "\n"
						+ "Cảm ơn bạn đã mua hàng tại QhShop ❤ "
						+ "\n"
						+ "________________________________");

		msg.setSubject("Đơn hàng số : " + order.getOrder_id());
		javaMailSender.send(msg);
		model.addAttribute("message", "Gửi mail thành công");
		return "redirect:/admin/order/edit?order_id=" + order_id;
	}

	@GetMapping("/admin/orderpending/edit/{order_id}")
	public String orderDetailPeding(Model model, @PathVariable("order_id") Integer order_id) {

		Order ListOrder = odao.findById(order_id).get();
		List<OrderDetail> ListOrderDetail = service.findByOrderID(order_id);

		model.addAttribute("ord", ListOrder);
		model.addAttribute("odetail", ListOrderDetail);
		model.addAttribute("message", "Thao tác thành công");
		return "admin/order/editpending";
	}

	@PostMapping("/admin/orderpending/edit/{order_id}")
	public String orderDetailPeding(Model model, @PathVariable("order_id") Integer order_id, Order ord) {
		Order old = orderService.findById(order_id);
		Account account = old.getAccount();
		account.setFullname(ord.getAccount().getFullname());
		old.setPhone(ord.getPhone());
		old.setMoney_give(ord.getMoney_give());
		old.setDescription(ord.getDescription());
		old.setAddress(ord.getAddress());
		old.setStatus(3);
		orderService.update(old);
		return "redirect:/admin/order/edit?order_id=" + order_id;
	}



	@GetMapping("/admin/orderpending/edit/{order_id}/add")
	public String addOrderDetailPeding(Model model,
									   @PathVariable("order_id") Integer order_id,
									   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
		if (pageNumber < 0) {
			return "redirect:/admin/orderpending/edit/" + order_id + "/add?pageNumber=" + (pageNumber + 1);
		}
		long countPage = prodao.findAll().stream().count() % 15 == 0 ? prodao.findAll().stream().count() / 15 : prodao.findAll().stream().count() / 15 + 1;
		if (pageNumber > countPage - 1) {
			return "redirect:/admin/orderpending/edit/" + order_id + "/add?pageNumber=" + (pageNumber - 1);
		}
		Order ListOrder = odao.findById(order_id).get();
		List<OrderDetail> ListOrderDetail = service.findByOrderID(order_id);

		// Duyệt qua danh sách OrderDetail và thiết lập giá
		for (OrderDetail orderDetail : ListOrderDetail) {
			// Lấy giá từ đối tượng Product
			Double price = orderDetail.getProduct().getUnit_price();
			// Gán giá cho thuộc tính Price của OrderDetail
			orderDetail.setPrice(price);
		}

		Page<Product> products = productService.findAllProductsWithCondition(pageNumber, 15, "");
		model.addAttribute("ord", ListOrder);
		model.addAttribute("odetail", ListOrderDetail);
		model.addAttribute("products", products);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("countPage", countPage);
		model.addAttribute("message", "Thao tác thành công");
		return "admin/order/addpending";
	}


	//	Xóa và cập nhập product
	@RequestMapping(value = "/admin/order/deleteDetail/{orderdetail_id}", method = RequestMethod.POST)
	public String deleteOrderDetail(@PathVariable("orderdetail_id") Integer orderDetailId) {
		Optional<OrderDetail> orderDetailOptional = otddao.findById(orderDetailId);
		if (orderDetailOptional.isPresent()) {
			OrderDetail deletedOrderDetail = orderDetailOptional.get();
			Order order = deletedOrderDetail.getOrder();
			Product product = deletedOrderDetail.getProduct();
			Integer deletedQuantity = deletedOrderDetail.getQuantity();
			Double deletedPrice = deletedOrderDetail.getPrice();

			// Xóa chi tiết đơn hàng
			otddao.delete(deletedOrderDetail);

			// Cập nhật lại số lượng sản phẩm trong bảng Product
			if (product != null) {
				Integer currentQuantity = product.getQuantity();
				product.setQuantity(currentQuantity + deletedQuantity);
				prodao.save(product);
			}

			// Cập nhật lại tổng giá của đơn hàng
			Double currentOrderPrice = order.getPrice();
			Double updatedOrderPrice = currentOrderPrice - deletedPrice;
			order.setPrice(updatedOrderPrice);
			odao.save(order);

			return "redirect:/admin/order/edit?order_id=" + order.getOrder_id();
		} else {
			// Xử lý trường hợp không tìm thấy chi tiết đơn hàng
			return "redirect:/admin/order";
		}
	}



	@PostMapping("/admin/order/addProductToOrder")
	public String addProductToOrder(@RequestParam("productId") Integer productId, @RequestParam("quantity") Integer quantity) {
		Product product = prodao.findById(productId).orElse(null);
		if (product != null && product.getQuantity() >= quantity) {
			// Tạo mới hoặc cập nhật OrderDetail
			OrderDetail existingOrderDetail = otddao.findByProduct(product);
			if (existingOrderDetail != null) {
				existingOrderDetail.setQuantity(existingOrderDetail.getQuantity() + quantity);
				existingOrderDetail.setPrice(existingOrderDetail.getPrice() + (product.getUnit_price() * quantity));
				otddao.save(existingOrderDetail);
			} else {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setProduct(product);
				orderDetail.setQuantity(quantity);
				orderDetail.setPrice(product.getUnit_price() * quantity);
				otddao.save(orderDetail);
			}

			// Cập nhật số lượng sản phẩm trong bảng Product
			product.setQuantity(product.getQuantity() - quantity);
			prodao.save(product);
		}
		// Redirect hoặc trả về view tùy thuộc vào yêu cầu của bạn
		return "redirect:/admin/order/detail";
	}
	@RequestMapping("/admin/order/edit")
	public String orderDetail(Model model, @RequestParam("order_id") Integer order_id) {
		Order ListOrder = odao.findById(order_id).get();
		List<OrderDetail> ListOrderDetail = service.findByOrderID(order_id);

		model.addAttribute("ord", ListOrder);
		model.addAttribute("odetail", ListOrderDetail);
		model.addAttribute("message", "Thao tác thành công");
		return "admin/order/edit";

	}

	@PostMapping("/admin/order/edit/{order_id}")
	public String addProductToOrder(@PathVariable("order_id") Integer order_id,
									Model model,
									@RequestParam("productId") Integer productId,
									RedirectAttributes redirectAttributes) {
		try {
			// Kiểm tra và lấy thông tin Order
			Order order = odao.findById(order_id).orElse(null);
			if (order == null) {
				redirectAttributes.addFlashAttribute("error", "Order not found");
				return "redirect:/admin/order/edit?order_id=" + order_id;
			}

			// Lấy thông tin sản phẩm từ productId
			Product existingProduct = productService1.findProductById(productId);
			if (existingProduct == null) {
				redirectAttributes.addFlashAttribute("error", "Invalid Product ID");
				return "redirect:/admin/order/edit?order_id=" + order_id;
			}

			// Kiểm tra số lượng sản phẩm
			if (existingProduct.getQuantity() <= 0) {
				redirectAttributes.addFlashAttribute("error", "San pham đã hết không thể thêm vào đơn hàng");
				return "redirect:/admin/order/edit?order_id=" + order_id;
			}

			// Kiểm tra xem sản phẩm đã tồn tại trong đơn hàng chưa
			boolean productExistsInOrder = false;
			for (OrderDetail od : order.getOrderDetails()) {
				if (od.getProduct().getProduct_id().equals(productId)) {
					// Nếu sản phẩm đã tồn tại trong đơn hàng, cộng dồn số lượng
					od.setQuantity(od.getQuantity() + 1);
					od.setPrice(od.getPrice() + existingProduct.getUnit_price());
					// Lưu thay đổi số lượng vào cơ sở dữ liệu
					otddao.save(od);
					productExistsInOrder = true;
					break;
				}
			}

			// Nếu sản phẩm chưa tồn tại trong đơn hàng, tạo mới OrderDetail
			if (!productExistsInOrder) {
				OrderDetail newOrderDetail = new OrderDetail();
				newOrderDetail.setProduct(existingProduct);
				newOrderDetail.setOrder(order);
				newOrderDetail.setQuantity(1);
				newOrderDetail.setPrice(existingProduct.getUnit_price());
				// Lưu OrderDetail mới vào cơ sở dữ liệu
				otddao.save(newOrderDetail);
				order.getOrderDetails().add(newOrderDetail);
			}

			// Cập nhật lại thông tin của Order
			Double totalPrice = order.getOrderDetails().stream()
					.mapToDouble(od -> od.getPrice())
					.sum();
			order.setPrice(totalPrice);
			orderService.updateOrder(order);

			// Cập nhật lại số lượng sản phẩm trong Product
			existingProduct.setQuantity(existingProduct.getQuantity() - 1);
			productService1.updateProduct(existingProduct);

			redirectAttributes.addFlashAttribute("success", "Product added successfully");
			return "redirect:/admin/order/edit?order_id=" + order_id;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error occurred while adding product to order");
			return "redirect:/admin/order/edit";
		}
	}

//	@PostMapping("/admin/order/updateAll")
//	public String updateAllOrderDetails(@RequestParam("quantities") List<Integer> quantities,
//										@RequestParam("orderDetailIds") List<Integer> orderDetailIds, RedirectAttributes redirectAttributes) {
//		try {
//			Integer orderId = null; // Khởi tạo orderId
//			Double totalOrderPrice = 0.0; // Khởi tạo tổng giá của đơn hàng
//
//			for (int i = 0; i < orderDetailIds.size(); i++) {
//				Integer orderDetailId = orderDetailIds.get(i);
//				OrderDetail orderDetail = otddao.findById(orderDetailId)
//						.orElseThrow(() -> new RuntimeException("OrderDetail not found"));
//
//				// Lấy orderId từ một trong các orderDetail
//				if (i == 0) {
//					orderId = orderDetail.getOrder().getOrder_id();
//				}
//
//				// Lấy số lượng trước khi cập nhật
//				Integer oldQuantity = orderDetail.getQuantity();
//
//				// Cập nhật số lượng OrderDetail
//				Integer quantity = quantities.get(i);
//
//				// Lấy sản phẩm tương ứng
//				Product product = orderDetail.getProduct();
//				Integer productQuantity = product.getQuantity();
//
//				// Tính toán sự khác biệt về số lượng
//				Integer quantityDiff = quantity - oldQuantity;
//
//				// Kiểm tra xem số lượng cập nhật có vượt quá số lượng hiện có trong kho hay không
//				if (quantityDiff > productQuantity) {
//					redirectAttributes.addFlashAttribute("errorMessage", "Số lượng cập nhật vượt quá số lượng sản phẩm trong kho.");
//					return "redirect:/admin/order/edit?order_id=" + orderId;
//				}
//
//				// Cập nhật số lượng sản phẩm trong OrderDetail
//				orderDetail.setQuantity(quantity);
//
//				// Cập nhật giá cho OrderDetail dựa trên số lượng mới
//				Double pricePerUnit = orderDetail.getProduct().getUnit_price();
//				Double newPrice = pricePerUnit * quantity;
//				orderDetail.setPrice(newPrice);
//
//				// Lưu thay đổi số lượng và giá vào cơ sở dữ liệu
//				otddao.save(orderDetail);
//
//				// Cập nhật tổng giá của đơn hàng
//				totalOrderPrice += newPrice;
//
//				// Cập nhật số lượng của sản phẩm tương ứng trong bảng Product
//				Integer updatedProductQuantity = productQuantity - quantityDiff;
//				product.setQuantity(updatedProductQuantity);
//				prodao.save(product);
//			}
//
//			// Lấy thông tin đơn hàng từ orderId
//			Order order = odao.findById(orderId)
//					.orElseThrow(() -> new RuntimeException("Order not found"));
//
//			// Cập nhật tổng giá của đơn hàng
//			order.setPrice(totalOrderPrice);
//
//			// Lưu thay đổi giá của đơn hàng vào cơ sở dữ liệu
//			odao.save(order);
//
//			// Trả về trang chỉnh sửa đơn hàng với orderId
//			return "redirect:/admin/order/edit?order_id=" + orderId;
//		} catch (Exception e) {
//			// Xử lý nếu có lỗi xảy ra
//			return "redirect:/admin/order/edit";
//		}
//	}

	@PostMapping("/admin/order/updateAll")
	public String updateAllOrderDetails(@RequestParam("quantities") List<Integer> quantities,
										@RequestParam("orderDetailIds") List<Integer> orderDetailIds, RedirectAttributes redirectAttributes) {
		try {
			Integer orderId = null;
			Double totalOrderPrice = 0.0;

			for (int i = 0; i < orderDetailIds.size(); i++) {
				Integer orderDetailId = orderDetailIds.get(i);
				OrderDetail orderDetail = otddao.findById(orderDetailId)
						.orElseThrow(() -> new RuntimeException("OrderDetail not found"));

				if (i == 0) {
					orderId = orderDetail.getOrder().getOrder_id();
				}

				// Lấy số lượng trước khi cập nhật
				Integer oldQuantity = orderDetail.getQuantity();

				// Cập nhật số lượng OrderDetail
				Integer quantity = quantities.get(i);

				// Lấy sản phẩm tương ứng
				Product product = orderDetail.getProduct();
				Integer productQuantity = product.getQuantity();

				// Tính toán sự khác biệt về số lượng
				Integer quantityDiff = quantity - oldQuantity;

				// Kiểm tra xem số lượng cập nhật có vượt quá số lượng hiện có trong kho hay không
				if (quantityDiff > productQuantity) {
					redirectAttributes.addFlashAttribute("errorMessage", "Số lượng cập nhật vượt quá số lượng sản phẩm trong kho.");
					return "redirect:/admin/order/edit?order_id=" + orderId;
				}

				// Cập nhật số lượng sản phẩm trong OrderDetail
				orderDetail.setQuantity(quantity);

				// Cập nhật giá cho OrderDetail dựa trên số lượng mới
				Double pricePerUnit = orderDetail.getProduct().getUnit_price();
				Double newPrice = pricePerUnit * quantity;
				orderDetail.setPrice(newPrice);

				// Lưu thay đổi số lượng và giá vào cơ sở dữ liệu
				otddao.save(orderDetail);

				// Cập nhật tổng giá của đơn hàng
				totalOrderPrice += newPrice;

				// Cập nhật số lượng của sản phẩm tương ứng trong bảng Product
				Integer updatedProductQuantity = productQuantity - quantityDiff;
				product.setQuantity(updatedProductQuantity);
				prodao.save(product);
			}

			// Lấy thông tin đơn hàng từ orderId
			Order order = odao.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order not found"));

			order.setPrice(totalOrderPrice);
			odao.save(order);


			return "redirect:/admin/order/edit?order_id=" + orderId;
		} catch (Exception e) {
			return "redirect:/admin/order/edit";
		}
	}


//	@PostMapping("/admin/order/updateAll")
//	public String updateAllOrderDetails(@RequestParam("quantities") List<Integer> quantities,
//										@RequestParam("orderDetailIds") List<Integer> orderDetailIds, RedirectAttributes redirectAttributes) {
//		try {
//			Integer orderId = null; // Khởi tạo orderId
//			Double totalOrderPrice = 0.0; // Khởi tạo tổng giá của đơn hàng
//			for (int i = 0; i < orderDetailIds.size(); i++) {
//				Integer orderDetailId = orderDetailIds.get(i);
//				OrderDetail orderDetail = otddao.findById(orderDetailId)
//						.orElseThrow(() -> new RuntimeException("OrderDetail not found"));
//
//				// Lấy orderId từ một trong các orderDetail
//				if (i == 0) {
//					orderId = orderDetail.getOrder().getOrder_id();
//				}
//
//				// Lấy số lượng trước khi cập nhật
//				Integer oldQuantity = orderDetail.getQuantity();
//
//				// Cập nhật số lượng OrderDetail
//				Integer quantity = quantities.get(i);
//
//				Integer quantityDiff = quantity - oldQuantity;
//				orderDetail.setQuantity(quantity);
//
//				// Cập nhật giá cho OrderDetail dựa trên số lượng mới
//				Double pricePerUnit = orderDetail.getProduct().getUnit_price();
//				Double newPrice = pricePerUnit * quantity;
//				orderDetail.setPrice(newPrice);
//
//				// Lưu thay đổi số lượng và giá vào cơ sở dữ liệu
//				otddao.save(orderDetail);
//
//				// Cập nhật tổng giá của đơn hàng
//				totalOrderPrice += newPrice;
//
//				// Cập nhật số lượng của sản phẩm tương ứng trong bảng Product
//				Product product = orderDetail.getProduct();
//				Integer productQuantity = product.getQuantity();
//				Integer updatedProductQuantity = productQuantity - quantityDiff;
//				updatedProductQuantity = Math.max(updatedProductQuantity, 0);
//				product.setQuantity(updatedProductQuantity);
//				prodao.save(product);
//			}
//
//			// Lấy thông tin đơn hàng từ orderId
//			Order order = odao.findById(orderId)
//					.orElseThrow(() -> new RuntimeException("Order not found"));
//
//			// Cập nhật tổng giá của đơn hàng
//			order.setPrice(totalOrderPrice);
//
//			// Lưu thay đổi giá của đơn hàng vào cơ sở dữ liệu
//			odao.save(order);
//
//			// Trả về trang chỉnh sửa đơn hàng với orderId
//			return "redirect:/admin/order/edit?order_id=" + orderId;
//		} catch (Exception e) {
//			// Xử lý nếu có lỗi xảy ra
//			return "redirect:/admin/order/edit";
//		}
//	}

}