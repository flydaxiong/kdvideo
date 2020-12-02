package com.kd.provider.mapper;



import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

     List<String> imageUrl();

}
