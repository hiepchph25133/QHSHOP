package com.poly.rest.controller;

import com.poly.entity.Category;
import com.poly.entity.Color;
import com.poly.entity.Size;
import com.poly.service.ColorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/color")
public class ColorRestController {

    @Autowired
    private ColorServices colorServices;

    @GetMapping()
    public List<Color> getAll() {
        return colorServices.findAll();
    }


    @GetMapping("{Color_id}")
    public Color getOne(@PathVariable("Color_id")Integer Color_id) {
        return colorServices.findById(Color_id);
    }


    @PostMapping()
    public Color create(@RequestBody Color Color_id) {
        return colorServices.create(Color_id);
    }


    @PutMapping("{Color_id}")
    public Color put(@PathVariable("Color_id")Integer Color_id, @RequestBody Color color) {
        return colorServices.update(color);
    }


    @DeleteMapping("{Color_id}")
    public void delete(@PathVariable("Color_id")Integer Color_id) {
        colorServices.delete(Color_id);
    }
}
