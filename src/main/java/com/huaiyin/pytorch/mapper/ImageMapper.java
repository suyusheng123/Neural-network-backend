package com.huaiyin.pytorch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huaiyin.pytorch.entity.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName:ImageMapper
 * Package:com.huaiyin.pytorch.mapper
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/5/1 13:41
 * @Version 1.0
 */
@Mapper
public interface ImageMapper extends BaseMapper<Record> {
}
