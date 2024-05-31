package com.poly.controller.user;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dao.VoteDao;
import com.poly.entity.Account;
import com.poly.entity.Product;
import com.poly.entity.Vote;
import com.poly.service.CommentService;
import com.poly.service.ProductService;

@Controller
public class VoteController {
	@Autowired
	HttpServletRequest request;
	@Autowired
	VoteDao dao;
	
	@Autowired
	CommentService commservice;

	@RequestMapping("/vote/add")
	public String commentAdd(Model model,
							 @RequestParam("product_id") Integer product_id,
							 @RequestParam("content") String content,
							 @RequestParam("vote") Integer vote,
							 @RequestParam("image_comment") MultipartFile file) throws IOException {
		// Lấy username từ request
		String username = request.getRemoteUser();

		// Kiểm tra xem tài khoản đã đánh giá sản phẩm đó chưa
		boolean hasVoted = dao.existsByAccount_UsernameAndProduct_ProductId(username, product_id);

		// Nếu đã đánh giá, xử lý ở đây (ví dụ: trả về thông báo lỗi)
		if (hasVoted) {
			return "redirect:/product/detail/" + product_id + "?error=already_voted";
		} else {
			// Nếu chưa đánh giá, thêm đánh giá mới vào cơ sở dữ liệu
			Vote vo = new Vote();
			Account acc = new Account();
			acc.setUsername(username);
			Product pro = new Product();
			pro.setProduct_id(product_id);
			vo.setAccount(acc);
			vo.setProduct(pro);
			vo.setVote_Content(content);
			vo.setVote(vote);
			if (file != null && !file.isEmpty()) {
				File saveFile = commservice.save(file, "/assets/images/comments/");
				vo.setImage(file.getOriginalFilename());
			}
			dao.save(vo);
			return "redirect:/product/detail/" + product_id;
		}
	}
	
	
}
