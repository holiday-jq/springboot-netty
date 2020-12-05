package com.holiday.matcloud.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.holiday.matcloud.dto.User;

public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		User user = new User();
		user.setUserId(rs.getString("userId"));
		user.setUserName(rs.getString("userName"));
		user.setPassword(rs.getString("password"));
		user.setSex(rs.getString("sex"));
		user.setImage(rs.getString("image"));
		user.setShowName(rs.getString("showName"));
		return user;
	}

}
