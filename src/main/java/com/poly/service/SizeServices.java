package com.poly.service;

import com.poly.entity.Size;

import java.util.List;

public interface SizeServices {

    public List<Size> findAll();

    public Size findById(Integer size_id);

    public Size create(Size size);

    public Size update(Size size);

    public void delete(Integer size_id);

    public Size saveProductSize(Size productSize);
}
