package ru.nastyzl.fooddelivery.mapper;
;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.nastyzl.fooddelivery.dto.VendorDto;
import ru.nastyzl.fooddelivery.model.VendorEntity;

import static org.junit.Assert.assertEquals;

public class DishMapperUnitTest {
    DishMapper mapper = Mappers.getMapper(DishMapper.class);

    @Test
    public void givenVendorDtoNestedMappingToEntity_whenMaps_thenCorrect() {

        VendorDto vendorDto = new VendorDto();
        vendorDto.setId(1L);
        vendorDto.setUsername("test name");


        VendorEntity vendorEntity = mapper.vendorDtoToVendorEntity(vendorDto);

        assertEquals(vendorDto.getId(), vendorEntity.getId());
        assertEquals(vendorDto.getUsername(), vendorEntity.getUsername());
    }
}
