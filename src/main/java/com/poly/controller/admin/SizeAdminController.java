package com.poly.controller.admin;

import com.poly.dao.SizeDao;
import com.poly.entity.Size;
import com.poly.service.SizeServices;
import com.poly.service.impl.SizeServicesIPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SizeAdminController {

    @Autowired
    private SizeServices sizeServices;

    @Autowired
    private SizeDao sizeDao;

    @RequestMapping("/admin/size/list")
    public String home(Model model) {
//        List<Size> sizes = sizeServices.findAll();
//        model.addAttribute("sizes", sizes);

        return "admin/Size/index.html";
    }


}
