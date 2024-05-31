package com.poly.rest.controller;

import com.poly.entity.Material;
import com.poly.entity.Size;
import com.poly.service.MaterialServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/material")
public class MaterialRestController {

    @Autowired
    private MaterialServices materialServices;

    @GetMapping()
    public List<Material> getAll() {
        return materialServices.findAll();
    }


    @GetMapping("{Material_id}")
    public Material getOne(@PathVariable("Material_id")Integer Material_id) {
        return materialServices.findById(Material_id);
    }


    @PostMapping()
    public Material create(@RequestBody Material Material_id) {
        return materialServices.create(Material_id);
    }


    @PutMapping("{Material_id}")
    public Material put(@PathVariable("Material_id")Integer Material_id,@RequestBody Material material) {
        return materialServices.update(material);
    }


    @DeleteMapping("{Material_id}")
    public void delete(@PathVariable("Material_id")Integer Material_id) {
        materialServices.delete(Material_id);
    }
}
