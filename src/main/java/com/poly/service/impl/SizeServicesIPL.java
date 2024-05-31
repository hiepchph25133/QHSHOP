package com.poly.service.impl;


import com.poly.dao.SizeDao;
import com.poly.entity.Category;
import com.poly.entity.Size;
import com.poly.service.SizeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServicesIPL implements SizeServices {

    @Autowired
    private SizeDao sizeDao;

    @Override
    public List<Size> findAll() {
        // TODO Auto-generated method stub
        return sizeDao.findAll();
    }

    @Override
    public Size findById(Integer size_id) {
        // TODO Auto-generated method stub
        return sizeDao.findById(size_id).get();
    }

    @Override
    public Size create(Size size) {
        // TODO Auto-generated method stub
        return sizeDao.save(size);
    }

    @Override
    public Size update(Size size) {
        // TODO Auto-generated method stub
        return sizeDao.save(size);
    }

    @Override
    public void delete(Integer size_id) {
        // TODO Auto-generated method stub
        sizeDao.deleteById(size_id);
    }

    @Override
    public Size saveProductSize(Size productSize) {
        return sizeDao.save(productSize);
    }


}
