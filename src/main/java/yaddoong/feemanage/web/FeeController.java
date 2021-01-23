package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.service.fee.FeeService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@RestController
public class FeeController {

    private final FeeService feeService;

    @GetMapping("/save")
    public String insertFeeLog() throws IOException, ParseException {
        feeService.save();
        return "success";
    }

}
