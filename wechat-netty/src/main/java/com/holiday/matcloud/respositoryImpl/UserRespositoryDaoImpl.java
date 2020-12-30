package com.holiday.matcloud.respositoryImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.holiday.matcloud.constant.UserSelectExt;
import com.holiday.matcloud.dto.User;
import com.holiday.matcloud.respository.UserRespositoryDao;
import com.holiday.matcloud.utils.UserRowMapper;

@Repository
public class UserRespositoryDaoImpl implements UserRespositoryDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public User getUserByUserName(String userName, String password) {
		// TODO Auto-generated method stub
		String sql = UserSelectExt.getUserByUserNameSql();
		User user = new User();
		try {
			user = jdbcTemplate.queryForObject(sql, 
					new UserRowMapper(), new Object[] { userName, password });
		} catch (Exception ex) {
			System.err.println(ex);
			return null;
		}
		return user;
	}

	@Override
	public List<User> getFriendListById(String userId) {
		// TODO Auto-generated method stub
		String sql = UserSelectExt.getFriendListSql();
		List<User> resultList = jdbcTemplate.query(sql, 
				new UserRowMapper() , new Object[] {userId});
		return resultList;
	}

	@Override
	public User getByUsername(String userName) {
		// TODO Auto-generated method stub
		String sql  = "select * from t_user where userName = ?";
		User user = new User();
		try {
			user = jdbcTemplate.queryForObject(sql, 
					new UserRowMapper(), new Object[] {userName});
		} catch (Exception ex) {
			System.err.println(ex);
			return null;
		}
		return user;
	}

}
