package com.holiday.matcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.holiday.matcloud.dto.ResultDto;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.service.UserService;

@RestController
@RequestMapping("/wechat")
public class UserController {
    
	@Autowired
	private UserService userService;
	
    /**
     * 根据当前用户的userId 获取该用户的好友列表
     * @param userId
     * @return
     */
	@GetMapping("/friendList")
	public ResultDto getFriendList(String userId) {
		List<User> resultList = userService.getFriendListData(userId);
		return ResultDto.ok(resultList, "调用接口成功!");
	}
}
