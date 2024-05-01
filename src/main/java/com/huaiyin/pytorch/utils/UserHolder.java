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
	private static ThreadLocal<Long> userHolder = new ThreadLocal<>();

	public static void set(Long userId) {
		userHolder.set(userId);
	}

	public static Long get() {
		return userHolder.get();
	}

	public static void remove() {
		userHolder.remove();
	}
}
