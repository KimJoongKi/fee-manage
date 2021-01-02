package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yaddoong.feemanage.service.fee.FeeService;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@RestController
public class FeeController {

    private final FeeService feeService;

    @GetMapping("/save")
    public void insertFeeLog() throws IOException {
        feeService.save();
    }
}
