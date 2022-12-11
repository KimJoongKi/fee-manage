package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yaddoong.feemanage.domain.fee.FeeFileLog;
import yaddoong.feemanage.domain.fee.FeeLogProjection;
import yaddoong.feemanage.domain.file.FileUpload;
import yaddoong.feemanage.domain.user.User;
import yaddoong.feemanage.service.fee.FeeService;
import yaddoong.feemanage.service.file.FileService;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.form.UserFeeForm;
import yaddoong.feemanage.web.form.UserForm;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Controller
public class UserController {

    private final UserService userService;

    private final FeeService feeService;

    private final FileService fileService;

    @Autowired
    MessageSource messageSource;

    @GetMapping("/list")
    public String findUserList(Model model) {
        List<User> userList = userService.findUserList();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    @GetMapping("/secessionList")
    public String findSecessionList(Model model) {
        List<User> secessionUserList = userService.findSecessionUserList();
        model.addAttribute("userList", secessionUserList);
        return "user/secessionList";
    }

    /**
     * 회원별 회비 납부내역 조회 메소드
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/nonpayment")
    public String userFeeList(Model model) {
        // 페이징 처리
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "filename"));

        Page<FileUpload> all = fileService.selectFileUploadLog(pageable);
        // 파일 업로드 기록이 없을 경우
        if (all.getContent().size() == 0) {
            model.addAttribute("lastUpdateData", "업데이트 기록 없음");
            return "user/userFeeList";
        }
        // 파일 업로드 기록이 있을 때 가장 마지막일자 기준 메시지를 나타낸다.
        model.addAttribute
                ("lastUpdateData", "(" + messageSource.getMessage("class.start.date", null, null) + ") " + all.getContent()
                        .get(0)
                        .getFilename()
                        .replaceAll(".xlsx", "") + " 까지 기준");
        List<FeeLogProjection> list = feeService.findGroupByName();
        List<UserFeeForm> feeFormList = new ArrayList<>();
        for (FeeLogProjection feeLogProjection : list) {
            UserFeeForm form = new UserFeeForm();
            form.setName(feeLogProjection.getName());
            form.setPrice(feeLogProjection.getPrice());
            form.setUnpaid(feeService.feePriceCalc(feeLogProjection.getJoinDate())
                    - feeLogProjection.getPrice()
                    + feeLogProjection.getUnpaid());
            feeFormList.add(form);
        }
        model.addAttribute("fees", feeFormList);

        return "user/userFeeList";
    }

    @PostMapping("/rejoin")
    public String rejoin(UserForm form) {
        userService.rejoin(form.getId(), form.getRejoinDate());
        return "redirect:/user/secessionList";
    }

    @PostMapping("/secessionUpdate")
    public String secessionUpdate(UserForm form) {
        userService.secession(form.getId(), form.getSecessionDate());
        return "redirect:/user/list";
    }

}
