package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.util.FileUploadUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/dish")
public class DishController {
    private final DishService dishService;
    private final CartService cartService;

    public DishController(DishService dishService, CartService cartService) {
        this.dishService = dishService;
        this.cartService = cartService;
    }

    @GetMapping("/new")
    public String createDish(@ModelAttribute("dishCreateDto") DishCreateDto dishCreateDto) {
        return "dish/dish-form";
    }

    @PostMapping("/save")
    public String saveDish(@Valid @ModelAttribute("dishCreateDto") DishCreateDto dishCreateDto, BindingResult bindingResult, @RequestParam("image") MultipartFile multipartFile, Principal principal) throws IOException, UserNotFoundException {
        if (multipartFile.isEmpty()) {
            bindingResult.addError(new FieldError("dishCreateDto", "imgPath", "Необходимо добавить фотографию блюда."));
        } else {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            dishCreateDto.setImgPath(fileName);
            DishEntity saveDish = dishService.save(dishCreateDto, principal.getName());
            String upload = "src/main/resources/static/assets/img/" + saveDish.getVendorEntity().getId();

            FileUploadUtil.saveFile(upload, fileName, multipartFile);
        }
        if (bindingResult.hasErrors()) {
            return "dish/dish-form";
        }
        return "redirect:/vendor/dishes";
    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deleteDish(@PathVariable("id") Long id) {
        if (!dishService.getById(id).isDeleted()) {
            cartService.removeProductFromCarts(id);
        }
        dishService.changeDeleteFlagById(id);
        return "redirect:/vendor/dishes";
    }

    @PatchMapping("/{id}")
    public String updateDish(@PathVariable("id") Long id) {
        return "dish/update";
    }
}
