package com.poly.service.impl;

import com.poly.dao.MaterialDao;
import com.poly.entity.Material;
import com.poly.entity.Size;
import com.poly.service.MaterialServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServicesIPL implements MaterialServices {

    @Autowired
    private MaterialDao materialDao;

    @Override
    public List<Material> findAll() {
        // TODO Auto-generated method stub
        return materialDao.findAll();
    }

    @Override
    public Material findById(Integer Material_id) {
        // TODO Auto-generated method stub
        return materialDao.findById(Material_id).get();
    }

    @Override
    public Material create(Material material) {
        // TODO Auto-generated method stub
        return materialDao.save(material);
    }

    @Override
    public Material update(Material material) {
        // TODO Auto-generated method stub
        return materialDao.save(material);
    }

    @Override
    public void delete(Integer Material_id) {
        // TODO Auto-generated method stub
        materialDao.deleteById(Material_id);
    }
}
