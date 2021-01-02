package yaddoong.feemanage.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExcelToDbController {

    @GetMapping("/insertFeeLog")
    public void feeLogSave() {
        System.out.println("hello world");
    }
}
