package com.holiday.matcloud.serviceimpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.respository.UserRespositoryDao;
import com.holiday.matcloud.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRespositoryDao userRespositoryDao;
	
	@Override
	public List<User> getFriendListData(String userId) {
		// TODO Auto-generated method stub
		return userRespositoryDao.getFriendListById(userId);
	}

}
