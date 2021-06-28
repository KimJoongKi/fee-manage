package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.form.UserForm;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public String findUserList(Model model) {
        List<User> userList = userService.findUserList();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @PostMapping("/secessionUpdate")
    public String secessionUpdate(UserForm form) {


        userService.secessionUpdate(form.getId(), form.getSecessionDate());

        return "redirect:/user/list";
    }

}
