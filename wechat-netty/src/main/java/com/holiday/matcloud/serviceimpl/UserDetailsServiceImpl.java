package com.holiday.matcloud.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.respository.UserRespositoryDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Lazy
	private UserRespositoryDao userRespositoryDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
        User tbUser = userRespositoryDao.getByUsername(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        return new org.springframework.security.core.userdetails.User
        		(tbUser.getUserName(), tbUser.getPassword(), grantedAuthorities);
	}
}
