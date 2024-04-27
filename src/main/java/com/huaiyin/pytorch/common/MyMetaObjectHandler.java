package com.huaiyin.pytorch.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.huaiyin.pytorch.utils.UserNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * ClassName:MyMetaObjectHandler
 * Package:com.itheima.reggie.common
 * Description:
 * 自定义的元数据处理器，将公共字段在相应的操作时自动填充进去,
 * 比如创建时间，创建人，修改时间，修改人
 *
 * @Author 卜翔威
 * @Create 2024/3/5 20:47
 * @Version 1.0
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
	/**
	 * 插入操作自动填充
	 *
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("公共字段自动填充[insert]");
		log.info("metaObject:{}", metaObject.toString());
		// 设置公共字段的值
		metaObject.setValue("createTime", LocalDateTime.now());
		metaObject.setValue("updateTime", LocalDateTime.now());
		metaObject.setValue("userName", UserNameGenerator.getStringRandom(6));
	}

	/**
	 * 更新操作自动填充
	 *
	 * @param metaObject
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("公共字段自动填充[update]");
		metaObject.setValue("updateTime", LocalDateTime.now());
	}
}
