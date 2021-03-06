package org.commerce.commercebackend.admin.user.controller;

import org.commerce.commercebackend.admin.user.services.UserService;
import org.commerce.commercebackend.admin.user.services.exceptions.UserNotFoundException;
import org.commerce.commercebackend.admin.util.FileUploadUtil;
import org.commerce.commercebackend.admin.util.UserCsvExporter;
import org.commerce.commercebackend.admin.util.UserPdfExporter;
import org.commerce.common.entity.Role;
import org.commerce.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {
        return listByPage(1, model,"firstName","asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(
                             @PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param(" ") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword) {

        Page<User> pageUser = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = pageUser.getContent();

        long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE -1 ;

        if(endCount > pageUser.getTotalElements()){
            endCount = pageUser.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("startCount", startCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageUser.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageUser.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", reverseSortDir);

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

        return getRedirectUrlToAffectUser(user);
    }

    private String getRedirectUrlToAffectUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
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

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(userList, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();

        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(userList, response);
    }


}