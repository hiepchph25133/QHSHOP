package com.poly.service;

import com.poly.entity.Color;

import java.util.List;

public interface ColorServices {

    public List<Color> findAll();

    public Color findById(Integer Color_id);

    public Color create(Color color);

    public Color update(Color color);

    public void delete(Integer Color_id);
}
