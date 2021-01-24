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

    @PostMapping("/save")
    public String insertFeeLog(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        feeService.save(file);
        return "redirect:/";
    }

}
