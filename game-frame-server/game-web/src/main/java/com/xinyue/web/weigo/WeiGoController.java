package com.xinyue.web.weigo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("weigo")
public class WeiGoController {
	@RequestMapping("index")
	public String index() {
		return "weigo/index";
	}
}
