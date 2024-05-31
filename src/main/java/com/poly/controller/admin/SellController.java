package com.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SellController {
    @RequestMapping("/admin/sell/view")
    public String cellView() {
        return "/admin/sell/index";
    }
}
