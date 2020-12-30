package com.holiday.matcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.holiday.matcloud.dto.ResultDto;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.service.loginService;

@RestController
@RequestMapping("/wechat")
public class loginController {
	
	@Autowired
	private loginService loginService;
	
     @PostMapping("/login/getUser")
     public ResultDto login(@RequestBody User user) {
    	 User loginUser =  loginService.login(user);
         return ResultDto.ok(loginUser, "调用接口成功！");
     }
}
