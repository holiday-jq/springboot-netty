package com.holiday.matcloud.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.respository.UserRespositoryDao;
import com.holiday.matcloud.service.loginService;

@Service
public class loginServiceImpl implements loginService{
	
	@Autowired
	private UserRespositoryDao userRespositoryDao;
	
	@Override
	public User login(User user) {
		// TODO Auto-generated method stub
		return userRespositoryDao.getByUsername(user.getUserName());
	}

}
