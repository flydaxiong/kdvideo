package com.kd.provider.mapper;





import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

     List<String> imageUrl();

}
