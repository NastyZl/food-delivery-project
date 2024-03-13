package ru.nastyzl.fooddelivery.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String saveDish(@Valid @ModelAttribute("dishDto") DishDto dishDto, BindingResult bindingResult, Authentication authentication) throws IOException {
        if (dishDto.getImgPath().isEmpty()) {
            bindingResult.addError(new FieldError("dishDto", "imgPath", "Необходимо добавить фотографию блюда."));
        }
        if (bindingResult.hasErrors()) {
            return "dish/dish-form";
        }
        MultipartFile multipartFile = dishDto.getImgPath();
        String storageFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            String uploadDir = "src/main/resources/static/assets/img/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        dishService.save(dishDto, userService.getByUsername(principal.getUsername()).get().getId());
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
