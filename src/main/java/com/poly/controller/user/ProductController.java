package com.poly.controller.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.poly.dao.*;
import com.poly.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.poly.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService productService;
	@Autowired
	ProductDao pdao;
	@Autowired
	HttpServletRequest request;
	@Autowired
	FavoriteDao  fadao;
	@Autowired
	VoteDao votedao;
	@Autowired
	JavaMailSender javaMailSender;

//	@RequestMapping({ "/" })
//	public String home() {
//		return "redirect:/product/list";
//	}

	@Autowired
	private ImageProductDao imageProductDao;

	@Autowired
	private ProductDetailDao productDetailDao;

	public void updateInformationProduct(Product product, ProductDetail detail , List<ImageProduct> images){
		if(detail!=null){
			product.setDetail(detail.getDetail());
			product.setDescription(detail.getDescription());
		}
		for(int i = 0 ; i <images.size();i++){
			if(i==0) product.setImage1(images.get(0).getPath());
			if(i==1) product.setImage2(images.get(1).getPath());
			if(i==2) product.setImage3(images.get(2).getPath());
			if(i==3) product.setImage4(images.get(3).getPath());
			if(i==4) product.setImage5(images.get(4).getPath());
			if(i==5) product.setImage6(images.get(5).getPath());
			if(i==6) product.setImage7(images.get(6).getPath());
		}

	}


	@GetMapping("/product/list")
	public String index(Model model, HttpServletRequest request, RedirectAttributes redirect) {
		request.getSession().setAttribute("productlist", null);
		if (model.asMap().get("success") != null)
			redirect.addFlashAttribute("success", model.asMap().get("success").toString());
		return "redirect:/product/list/page/1";
	}

	@GetMapping("/product/list/page/{pageNumber}")
	public String showProductPage(HttpServletRequest request, @PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 12;
		List<Product> list = (List<Product>) productService.findAll().stream()
				.filter(product -> product.getQuantity() > 0)
				.collect(Collectors.toList()).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());

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
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}
	
	
	@GetMapping("/product/list/laptop")
	public String labtop( Model model) {
		model.addAttribute("items", pdao.findByLaptop().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList()));
		model.addAttribute("sizepro", pdao.findByLaptop().size());
		
		return "user/product/list/laptop";
	}

	@GetMapping("/product/list/laptop/{pageNumber}")
	public String labtop( Model model, HttpServletRequest request,
			@PathVariable int pageNumber) {
		List<Product> list = pdao.findByLaptop().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		if (list == null) {
			return "redirect:/product/list";
		}
		model.addAttribute("sizepro", pdao.findByLaptop().size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/laptop";
	}

	@RequestMapping("/product/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id) {
		double vote_arg = 0;
		Product item = productService.findById(id);
		if(item!=null){
			ProductDetail detail = productDetailDao.findByProductID(item.getProduct_id());
			List<ImageProduct> images = imageProductDao.findByProductID(item.getProduct_id());
			this.updateInformationProduct(item, detail, images);
		}
		String username= request.getRemoteUser();
		List<Vote> VoteList = votedao.findbyProductId(id);
		if(VoteList.size()!=0) {
		for(int i=0 ; i < VoteList.size() ; i++) {
			vote_arg +=VoteList.get(i).getVote(); 
		}
		model.addAttribute("vote_arage",vote_arg/VoteList.size());
		}
		else {
		model.addAttribute("vote_arage",0);
		}
		model.addAttribute("votes", VoteList);
		model.addAttribute("votesize", VoteList.size());
		
		
		List<Favorite> listcheck = fadao.checkFavaritesAdmin(id, username);
		model.addAttribute("checklist", listcheck.size());
		model.addAttribute("item", item);
		return "user/product/detail";
	}


	@RequestMapping("/product/discount/{pageNumber}")
	public String discount(Model model, @RequestParam("cid") Optional<Integer> cid,
			HttpServletRequest request,
			@PathVariable int pageNumber) {
		
			List<Product> list = pdao.findByDis().stream()
					.filter(product -> product.getQuantity() > 0)
					.collect(Collectors.toList()).stream()
					.map(product -> {
						ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
						List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
						this.updateInformationProduct(product, detail, images);
						return  product;
					})
					.collect(Collectors.toList());
			model.addAttribute("items", list);
			PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
			int pagesize = 12;
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
			request.getSession().setAttribute("productlist", pages);
			int current = pages.getPage() + 1;
			int begin = Math.max(1, current - list.size());
			int end = Math.min(begin + 5, pages.getPageCount());
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/product/discount/";
			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("items", pages);

		return "user/product/discount";
	}

	@RequestMapping("/product/latest")
	public String latest(Model model, @RequestParam("cid") Optional<Integer> cid) {
	
			List<Product> list = pdao.findByLatest().stream()
					.filter(product -> product.getQuantity() > 0)
					.collect(Collectors.toList()).stream()
					.map(product -> {
						ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
						List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
						this.updateInformationProduct(product, detail, images);
						return  product;
					})
					.collect(Collectors.toList());
			model.addAttribute("items", list);
		
		return "user/product/latest";
	}

	@RequestMapping("/product/special")
	public String special(Model model, @RequestParam("cid") Optional<Integer> cid,
			@RequestParam("page") Optional<Integer> page) {
		
			List<Product> list = pdao.findBySpecial().stream()
					.filter(product -> product.getQuantity() > 0)
					.collect(Collectors.toList()).stream()
					.map(product -> {
						ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
						List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
						this.updateInformationProduct(product, detail, images);
						return  product;
					})
					.collect(Collectors.toList());
			model.addAttribute("items", list);

		return "user/product/special";
	}

	@GetMapping("/product/list/search")
	public String search(@RequestParam("keywords") String keywords, Model model) {
		if (keywords.equals("")) {
			return "redirect:/product/list";
		}
		model.addAttribute("items", productService.findByKeywords(keywords).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList()));
		return "list";
	}

	@GetMapping("/product/list/search/{pageNumber}")
	public String search(@RequestParam("keywords") String keywords, Model model, HttpServletRequest request,
			@PathVariable int pageNumber) {
		if (keywords.equals("")) {
			return "redirect:/product/list";
		}
		List<Product> list = productService.findByKeywords(keywords).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		if (list == null) {
			return "redirect:/product/list";
		}
		model.addAttribute("sizepro", productService.findByKeywords(keywords).size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}

	@RequestMapping("product/list/find")
	public String find(Model model) {
		List<Product> list = pdao.findAll().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("items", list);
		return "list";
	}

	@RequestMapping("/product/list/find/{pageNumber}")
	public String find(Model model, @RequestParam("Category_id") String Category_id, @RequestParam("Trademark_id") String Trademark_id,
			@RequestParam("Size_id") String Size_id, @RequestParam("Color_id") String Color_id, @RequestParam("Material_id") String Material_id,
			@RequestParam("MinPrice") Integer unit_price, @RequestParam("MaxPrice") Integer unit_price1, HttpServletRequest request,
			@PathVariable int pageNumber) {
		List<Product> list = pdao.findByAllKeyWord(unit_price, unit_price1, Category_id, Trademark_id,
				Size_id, Color_id, Material_id).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("items.pageList", list);
		
		model.addAttribute("sizepro", pdao.findByAllKeyWord(unit_price, unit_price1, Category_id, Trademark_id,
				Size_id, Color_id, Material_id).size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 12;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";

	}


	@GetMapping("/product/list/findByTrademarkId")
	public String trademark(@RequestParam("tid") Integer tid, Model model) {
		if (tid.equals("")) {
			return "redirect:/product/list";
		}
		model.addAttribute("items", productService.findByTrademarkId(tid).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList()));
		model.addAttribute("sizepro", productService.findByTrademarkId(tid).size());
		
		return "list";
	}

	@GetMapping("/product/list/findByTrademarkId/{pageNumber}")
	public String trademark(@RequestParam("tid") Integer tid, Model model, HttpServletRequest request,
			@PathVariable int pageNumber) {
		if (tid.equals("")) {
			return "redirect:/product/list";
		}
		List<Product> list = productService.findByTrademarkId(tid).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		if (list == null) {
			return "redirect:/product/list";
		}
		model.addAttribute("sizepro", productService.findByTrademarkId(tid).size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/page/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}

	@GetMapping("/product/list/top10/{pageNumber}")
	public String getTop10(Model model, HttpServletRequest request, @PathVariable int pageNumber) {

		List<Product> list = pdao.getTop10().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		if (list == null) {
			return "redirect:/product/list";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/top10/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}
	
	@GetMapping("/product/list/desc/{pageNumber}")
	public String getDesc(Model model, HttpServletRequest request, @PathVariable int pageNumber) {
		List<Product> list = pdao.getDesc().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("sizepro", pdao.getDesc().size());
		if (list == null) {
			return "redirect:/product/list";
		}
		model.addAttribute("sizepro", pdao.getDesc().size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/desc/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}
	
	@GetMapping("/product/list/asc/{pageNumber}")
	public String getAsc(Model model, HttpServletRequest request, @PathVariable int pageNumber) {
		List<Product> list = pdao.getAsc().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		model.addAttribute("sizepro", pdao.getAsc().size());
		if (list == null) {
			return "redirect:/product/list";
		}
		model.addAttribute("sizepro", pdao.getAsc().size());
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productlist");
		int pagesize = 9;
		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productlist", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/product/list/asc/";
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("items", pages);
		return "user/product/list";
	}
	

	@RequestMapping("/send1")
	public String sendMailShare(HttpServletRequest request, @RequestParam("id") Integer id, @RequestParam("to") String to, @RequestParam("subject") String subject, @RequestParam("content") String content) {
		SimpleMailMessage msg = new SimpleMailMessage();
		String url = request.getRequestURL().toString().replace("send1", "product/detail/" + id);
		msg.setTo(to);
		msg.setText(content + "'" + url + "'");
		msg.setSubject(subject);
		javaMailSender.send(msg);
		return "user/result";
	
	}
	
	
	@RequestMapping("/contact-us")
	public String contact(Model model) {

		return "user/contact/contact_us";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {

		return "user/contact/about";
	}
	
	
	
	
}