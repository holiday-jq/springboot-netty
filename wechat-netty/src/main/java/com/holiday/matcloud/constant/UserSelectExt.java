package com.holiday.matcloud.constant;

public class UserSelectExt {
	
	public static String getUserByUserNameSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("select userId, userName, password, sex, image, showName from t_user ");
		sql.append("where userName = ? and password = ?");
		return sql.toString();
	}
	
	public static String getFriendListSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("select b.* from (select user_id, friend_id from t_friend where user_id = ?) a ");
		sql.append(" left join t_user b on a.friend_id = b.userId");
		return sql.toString();
	}
}
