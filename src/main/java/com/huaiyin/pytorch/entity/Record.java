package com.huaiyin.pytorch.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName:Record
 * Package:com.huaiyin.pytorch.entity
 * Description:
 * 图片处理记录类
 * @Author 卜翔威
 * @Create 2024/5/1 13:23
 * @Version 1.0
 */
@Data
public class Record {
	private Long id;
	private Long userId;
//	private String UserName;
	private String originalAddress;
	private String newAddress;
	private String description;
	// 处理时间
	private Long recognizeTime;
	@TableField(fill = FieldFill.INSERT) //插入时填充字段
	private LocalDateTime createTime;
	@TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
	private LocalDateTime updateTime;
}
