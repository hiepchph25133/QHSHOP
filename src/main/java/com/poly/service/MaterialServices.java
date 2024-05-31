package com.poly.service;

import com.poly.entity.Material;

import java.util.List;

public interface MaterialServices {

    public List<Material> findAll();

    public Material findById(Integer Material_id);

    public Material create(Material material);

    public Material update(Material material);

    public void delete(Integer Material_id);
}
