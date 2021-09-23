package org.commerce.commercebackend.admin.user.controller;

import org.commerce.commercebackend.admin.user.services.UserService;
import org.commerce.commercebackend.admin.user.services.exceptions.UserNotFoundException;
import org.commerce.commercebackend.admin.util.FileUploadUtil;
import org.commerce.common.entity.Role;
import org.commerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model) {
        List<User> listUser = userService.listAll();
        model.addAttribute("listUsers", listUser);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create new User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            user.setPhotos(fileName);
            User savedUser = userService.save(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {

            if (user.getPhotosImagePath().isEmpty())  user.setPhotos(null);
                userService.save(user);

        }



        redirectAttributes.addFlashAttribute("message","The user has been saved successfully");

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model)  {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("listRoles", listRoles);
            return "user_form";
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","The user ID" + id + "has been deleted sucessfully");
        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + "has been " + status;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }



}