package com.poly.rest.controller;

import com.poly.entity.Category;
import com.poly.entity.Size;
import com.poly.service.SizeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/size")
public class SizeRestController {

    @Autowired
    private SizeServices sizeServices;

    @GetMapping()
    public List<Size> getAll() {
        return sizeServices.findAll();
    }


    @GetMapping("{Size_id}")
    public Size getOne(@PathVariable("Size_id")Integer Size_id) {
        return sizeServices.findById(Size_id);
    }


    @PostMapping()
    public Size create(@RequestBody Size Size_id) {
        return sizeServices.create(Size_id);
    }


    @PutMapping("{Size_id}")
    public Size put(@PathVariable("Size_id")Integer Size_id,@RequestBody Size size) {
        return sizeServices.update(size);
    }


    @DeleteMapping("{Size_id}")
    public void delete(@PathVariable("Size_id")Integer Size_id) {
        sizeServices.delete(Size_id);
    }
}
