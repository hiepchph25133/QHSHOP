package com.poly.rest.controller;

import com.poly.entity.Category;
import com.poly.entity.Voucher;
import com.poly.service.CategoryService;
import com.poly.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/voucher")
public class VoucherRestController {
	@Autowired
	VoucherService service;
	@GetMapping()
	public List<Voucher> getAll() {
		return service.findAll();
	}

	@GetMapping("/vadidate")
	public Integer vadidateVoucher(@RequestParam("code") String code,
								   @RequestParam("total") Double total){
		return service.checkIsvalidVoucher(code,total);
	}
	@GetMapping("/{id}")
	public Voucher getOne(@PathVariable("id")Integer id) {
		return service.findById(id);
	}

	@GetMapping("/code")
	public Voucher getVoucherByName(@RequestParam("code") String code) {
		return service.findByVoucherName(code);
	}

	@PostMapping()
	public Voucher create(@RequestBody Voucher voucher) {
		return service.update(voucher);
	}

	@PutMapping("/{id}")
	public Voucher put(@PathVariable("id")Integer id,@RequestBody Voucher voucher) {
		return service.update(voucher);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id")Integer id) {
		service.deleteById(id);
	}
}
