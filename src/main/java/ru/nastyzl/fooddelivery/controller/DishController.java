package ru.nastyzl.fooddelivery.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;
import ru.nastyzl.fooddelivery.util.FileUploadUtil;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;

    private final UserService userService;

    public DishController(DishService dishService, UserService userService) {
        this.dishService = dishService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String createDish(@ModelAttribute("dishDto") DishDto dishDto) {
        return "dish/dish-form";
    }

    @PostMapping("/save")
    public String saveDish(@ModelAttribute("dishDto") DishDto dishDto, Authentication authentication, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            dishDto.setImgPath(fileName);
            String upload = "images/" + dishDto.getDishName();
            FileUploadUtil.saveFile(upload, fileName, multipartFile);
        } else {
            if (dishDto.getImgPath().isEmpty()) {
                throw new RuntimeException("Ошибка с фоткой, тут пусто");
            }
        }
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        dishDto.setVendorEntity((VendorEntity) userService.getByUsername(principal.getUsername()).get());
        dishService.save(dishDto);
        return "redirect:/vendor/dishes";
    }

    @DeleteMapping("/{id}")
    public String deleteDish(@PathVariable("id") Long id) {
        dishService.deleteById(id);
        return "redirect:/vendor/dishes";
    }

    @PatchMapping("/{id}")
    public String updateDish(@PathVariable("id") Long id) {
        return "dish/update";
    }
}
