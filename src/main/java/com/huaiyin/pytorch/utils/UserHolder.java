package com.huaiyin.pytorch.utils;

import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.entity.User;

/**
 * ClassName:UserHolder
 * Package:com.huaiyin.pytorch.utils
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/4/27 15:33
 * @Version 1.0
 */
public class UserHolder {
	private static ThreadLocal<UserDTOLoginForm> userHolder = new ThreadLocal<>();

	public static void set(UserDTOLoginForm user) {
		userHolder.set(user);
	}

	public static UserDTOLoginForm get() {
		return userHolder.get();
	}

	public static void remove() {
		userHolder.remove();
	}
}
