package com.holiday.matcloud.service;

import java.util.List;
import com.holiday.matcloud.dto.User;

public interface UserService {	
     List<User> getFriendListData(String userId);
}
