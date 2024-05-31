package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MaterialAdminController {

    @RequestMapping("/admin/material/list")
    public String home(Model model) {

        return "admin/material/index.html";
    }
}
