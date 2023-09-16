package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yaddoong.feemanage.domain.fee.FeeLogProjection;
import yaddoong.feemanage.domain.member.Member;
import yaddoong.feemanage.service.fee.FeeService;
import yaddoong.feemanage.service.file.FileService;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.form.UserFeeForm;
import yaddoong.feemanage.web.form.UserForm;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/member")
@Controller
public class UserController {

    private final UserService userService;

    private final FeeService feeService;

    private final FileService fileService;

    @Autowired
    MessageSource messageSource;

    /**
     * 사용자 등록 화면 이동 메소드
     * @return
     */
    @GetMapping("/new")
    public String insertUser() {
        // TODO: 2023/04/25 사용자 등록 화면 이동 기능 추가
        return "members/createMemberForm";
    }

    @GetMapping("/list")
    public String findUserList(Model model) {
        List<Member> memberList = userService.findUserList(); // 현재 회원 목록 조회
        model.addAttribute("memberList", memberList);
        return "member/list";
    }

    @GetMapping("/secessionList")
    public String findSecessionList(Model model) {
        List<Member> secessionMemberList = userService.findSecessionUserList();
        model.addAttribute("userList", secessionMemberList);
        return "user/secessionList";
    }

    /**
     * 회원별 회비 납부내역 조회 메소드
     * @param model
     * @return
     */
    @GetMapping(value = "/nonpayment")
    public String userFeeList(Model model) {
        // 페이징 처리
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "filename"));


        List<FeeLogProjection> list = getFeeLogProjections(); // 회원별 납부,미납 내역 조회
        List<UserFeeForm> feeFormList = getUserFeeForms();
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

        return "member/userFeeList";
    }

    private static List<UserFeeForm> getUserFeeForms() {
        List<UserFeeForm> feeFormList = new ArrayList<>();
        return feeFormList;
    }

    /**
     * 회원별 납부내역 조회하기
     * @return
     */
    private List<FeeLogProjection> getFeeLogProjections() {
        return feeService.findGroupByName();
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
