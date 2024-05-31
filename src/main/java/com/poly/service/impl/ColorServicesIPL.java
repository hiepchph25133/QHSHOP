package com.poly.service.impl;

import com.poly.dao.ColorDao;
import com.poly.entity.Color;
import com.poly.entity.Size;
import com.poly.service.ColorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServicesIPL implements ColorServices {

    @Autowired
    private ColorDao colorDao;

    @Override
    public List<Color> findAll() {
        // TODO Auto-generated method stub
        return colorDao.findAll();
    }

    @Override
    public Color findById(Integer Color_id) {
        // TODO Auto-generated method stub
        return colorDao.findById(Color_id).get();
    }

    @Override
    public Color create(Color color) {
        // TODO Auto-generated method stub
        return colorDao.save(color);
    }

    @Override
    public Color update(Color color) {
        // TODO Auto-generated method stub
        return colorDao.save(color);
    }

    @Override
    public void delete(Integer Color_id) {
        // TODO Auto-generated method stub
        colorDao.deleteById(Color_id);
    }

}
