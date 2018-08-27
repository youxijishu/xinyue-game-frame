package com.xinyue.web.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("app")
public class AppController {
	@RequestMapping("/index")
	public String showIndex() {
		return "app/index";
	}

	@RequestMapping("zhaopin")
	public String zhaopin() {
		return "henan/zhaopin";
	}

	@RequestMapping("mifyy")
	public String mifyy() {
		return "mifyy/index";
	}
}
