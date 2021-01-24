package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.service.fee.FeeService;
import yaddoong.feemanage.web.form.FeeFileForm;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@Controller
public class FeeController {

    private final FeeService feeService;

    @GetMapping(value = "/new")
    public String saveForm() {
        return "fee/saveForm";
    }

    @GetMapping(value = "/list")
    public String list() {
        //미납금 + (오늘 날짜 - 오늘 날짜가 15보다 적으면 저번 달 15일 or 이번달 15일)*
        return "fee/list";
    }

    @PostMapping("/save")
    public String insertFeeLog(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        feeService.save(file);
        return "redirect:/";
    }

}
