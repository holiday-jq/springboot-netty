package com.holiday.matcloud.respository;

import java.util.List;

import com.holiday.matcloud.dto.User;

public interface UserRespositoryDao {
	
    User getUserByUserName(String userName, String password);
    
    List<User> getFriendListById(String userId);
    
    User getByUsername(String userName);
}
