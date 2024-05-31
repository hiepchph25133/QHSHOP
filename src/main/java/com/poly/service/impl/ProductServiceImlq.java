package com.poly.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import com.poly.dao.*;
import com.poly.dtos.ProductEnoughQuantityDTO;
import com.poly.entity.ImageProduct;
import com.poly.entity.OrderDetail;
import com.poly.entity.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.entity.Product;
import com.poly.service.AccountService;
import com.poly.service.AuthorityService;
import com.poly.service.ProductService;



@Service
public class ProductServiceImlq implements ProductService {
	@Autowired
	private ProductDao pdao;

	@Autowired
	private ImageProductDao imageProductDao;

	@Autowired
	private ProductDetailDao productDetailDao;

	@Autowired
	HttpServletRequest request;

	@Transient
	String Detail ;
	@Transient
	String Description;


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

	@Override
	public List<Product> findAll() {
		List<Product> updatedProducts = pdao.findAll().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;
	}

	@Override
	public List<Product> finbyIdOrName(String keywords) {
		List<Product> updatedProducts = pdao.finbyIdOrName(keywords).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;
	}

	@Override
	public List<Product> findByAllKeyWordAdmin(Integer unit_price, Integer unit_price1, String Category_id, String Trademark_id, String Status) {
		List<Product> updatedProducts = pdao.findByAllKeyWordAdmin( unit_price, unit_price1,
				Category_id ,Trademark_id ,
				Status ).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;
	}

	//	@Override
//	public Product findById(Integer id) {
//
//		Product product =  pdao.findById(id).get();
//		if(product!=null){
//			ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
//			List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
//			this.updateInformationProduct(product, detail, images);
//		}
//		return product;
//	}
	@Override
	public Product findById(Integer id) {
		Optional<Product> optionalProduct = pdao.findById(id);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
			List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
			this.updateInformationProduct(product, detail, images);
			return product;
		} else {
			return null; // hoặc throw exception tùy theo yêu cầu của bạn
		}
	}


	@Override
	public void saveAll(List<Product> products) {
		pdao.saveAll(products);
	}


	@Override
	public List<Product> findByCategoryId(Integer cid) {
		// TODO Auto-generated method stub
		//	return pdao.findByCategoryId(cid);

		List<Product> updatedProducts = pdao.findByCategoryId(cid).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;

	}

	@Override
	public Product create(Product product) {
		// TODO Auto-generated method stub
		return pdao.save(product);
	}

	@Override
	public Product update(Product product) {
		// TODO Auto-generated method stub
		return pdao.save(product);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		pdao.deleteById(id);;
	}

	@Override
	public List<Product> findByNameContaining(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> findByKeywords(String keywords) {
		// TODO Auto-generated method stub
		//	return pdao.findByKeywords(keywords);

		List<Product> updatedProducts = pdao.findByKeywords(keywords).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;

	}

	@Override
	public List<Product> findByTrademarkId(Integer integer) {
		// TODO Auto-generated method stub
		//return pdao.findByTrademarkId(integer);

		List<Product> updatedProducts = pdao.findByTrademarkId(integer).stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;
	}

	@Override
	public List<Product> findByLaptop() {
		// TODO Auto-generated method stub
		//	return pdao.findByLaptop();

		List<Product> updatedProducts = pdao.findByLaptop().stream()
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return  product;
				})
				.collect(Collectors.toList());
		return updatedProducts;
	}
	@Override
	public File save(MultipartFile file, String path) throws IOException {
		if(!file.isEmpty()) {
			String path1 = "E:\\netbean\\GoalShop\\src\\main\\resources\\static";
			File directory = new File(path1 + path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			File f = new File(directory , fileName);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
			byte[] data = file.getBytes();
			bufferedOutputStream.write(data);
			bufferedOutputStream.close();

			File dir = new File(request.getServletContext().getRealPath(path));
			if(!dir.exists()) {
				dir.mkdirs();
			}
			try {
				File savedFile = new File(dir, file.getOriginalFilename());
				file.transferTo(savedFile);
				return savedFile;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	@Override
	public Page<Product> findAllProductsWithCondition(int pageNumber, int pageSize , String productName) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);

		Specification<Product> hasPositiveQuantity = (root, query, criteriaBuilder) -> {
			return criteriaBuilder.gt(root.get("Quantity"), 0);
		};

		Specification<Product> combinedSpecification = Specification.where(hasPositiveQuantity);

		if (!productName.isEmpty()) {
			Specification<Product> hasName = (root, query, criteriaBuilder) -> {
				return criteriaBuilder.like(root.get("Name"), "%" + productName + "%");
			};
			combinedSpecification = combinedSpecification.and(hasName);
		}

		Page<Product> updatedProducts = pdao.findAll(combinedSpecification, pageable)
				.map(product -> {
					ProductDetail detail = productDetailDao.findByProductID(product.getProduct_id());
					List<ImageProduct> images = imageProductDao.findByProductID(product.getProduct_id());
					this.updateInformationProduct(product, detail, images);
					return product;
				});
		return updatedProducts;


	}

	@Override
	public ProductEnoughQuantityDTO checkQuantityEnough(List<OrderDetail> orderDetails) throws Throwable {

		List<String> errors = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		for(OrderDetail item: orderDetails){
			Optional optional = pdao.findById(item.getProduct().getProduct_id());
			Product product = (Product) optional.orElseThrow(() -> new NullPointerException("Sản phẩm không tồn tại."));
			if(item.getProduct().getQuantity()>product.getQuantity()) {
				var error = "Số lượng sản phẩm: "+product.getName()+" trong kho hiện không đủ. Số lượng khả dụng: "+product.getQuantity();
				errors.add(error);
			}else{
				var remainingAmount = product.getQuantity() - item.getProduct().getQuantity();
				product.setQuantity(remainingAmount);
			}
		}
		ProductEnoughQuantityDTO dto = ProductEnoughQuantityDTO.builder().products(products).errors(errors).build();
		return dto;
	}


	public Product findProductById(Integer productId) {
		return pdao.findById(productId).orElse(null);
	}

	public void updateProduct(Product product) {
		pdao.save(product);
	}

}