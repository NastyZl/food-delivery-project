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

    @Mapping(target = "imgPath", ignore = true)
    DishEntity dishDtoToEntity(DishDto dishDto);

    @Mapping(target = "imgPath", ignore = true)
    DishDto dishEntityToDto(DishEntity dishEntity);

    default String convertMultipartFileToString(MultipartFile file) {
        return StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }
}
