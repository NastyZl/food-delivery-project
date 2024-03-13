package ru.nastyzl.fooddelivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(target = "imgPath", expression = "java(convertMultipartFileToString(dishDto.getImgPath()))")
    DishEntity dishDtoToEntity(DishDto dishDto);

    @Mapping(target = "imgPath", expression = "java(convertStringToMultipartFile(dishEntity.getImgPath()))")
    DishDto dishEntityToDto(DishEntity dishEntity);

    default MultipartFile convertStringToMultipartFile(String path) {
       return null;
    }

    default String convertMultipartFileToString(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }
}
