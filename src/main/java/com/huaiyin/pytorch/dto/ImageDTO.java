package com.huaiyin.pytorch.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName:ImageDTO
 * Package:com.huaiyin.pytorch.dto
 * Description:
 * image数据传输对象
 * @Author 卜翔威
 * @Create 2024/5/4 21:43
 * @Version 1.0
 */

@Setter
@Getter
public class ImageDTO {
	// 图片文件名
	private String file;
	// 之后有什么需要的再往上加
}
