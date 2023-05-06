package com.sda.java3.ecommerce.controllers;

import com.sda.java3.ecommerce.domains.User;
import com.sda.java3.ecommerce.services.category.CategoryService;
import com.sda.java3.ecommerce.services.product.ProductService;
import com.sda.java3.ecommerce.services.user.UserServiceImpl;
import com.sda.java3.ecommerce.utils.Breadcrumb;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController extends BaseController {

  @Autowired private UserServiceImpl userService;

  public SignUpController(ProductService productService, CategoryService categoryService) {
    super(productService, categoryService);
    // TODO Auto-generated constructor stub
  }

  @GetMapping("/sign-up")
  public String showSignupForm(Model model, ModelMap modelMap) {
    initModelMap(modelMap);
    modelMap.addAttribute(
        "breadcrumbs",
        Arrays.asList(
            Breadcrumb.builder().name("Home").url("/").build(),
            Breadcrumb.builder().name("Sign-up").url("/sign-up").last(true).build()));
    User user = new User();
    model.addAttribute("user", user);
    return "sign-up";
  }

  @PostMapping("/sign-up")
  public String processSignupForm(
      @Valid @ModelAttribute("user") User user, BindingResult bindingResult, ModelMap modelMap) {
    initModelMap(modelMap);

    if (bindingResult.hasErrors()) {
      return "sign-up";
    }
    if (userService.checkEmailExist(user.getEmail())) {
      bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
      return "sign-up";
    }
    LocalDateTime currentTime = LocalDateTime.now();
    user.setRole(0);
    user.setCreated_at(currentTime);
    userService.saveUser(user);
    return "sign-in";
  }
}
