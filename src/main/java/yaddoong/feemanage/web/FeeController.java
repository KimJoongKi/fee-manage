package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.fee.FeeFileLog;
import yaddoong.feemanage.domain.fee.FeeFileLogRepository;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.service.fee.FeeService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@Controller
public class FeeController {

    private final FeeService feeService;
    private final FeeFileLogRepository feeFileLogRepository;

    @GetMapping(value = "/new")
    public String saveForm() {
        return "fee/saveForm";
    }

    @GetMapping(value = "/list")
    public String list(Model model) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
        Page<FeeFileLog> all = feeFileLogRepository.findAll(pageable);
        if (all.getContent().size() == 0) {
            model.addAttribute("lastUpdateData", "업데이트 기록 없음");
            return "fee/list";
        }
        model.addAttribute
                ("lastUpdateData",all.getContent()
                        .get(0)
                        .getName()
                        .replaceAll(".xlsx","") + " 기준");
        //미납금 + (오늘 날짜 - 오늘 날짜가 15보다 적으면 저번 달 15일 or 이번달 15일)*
        return "fee/list";
    }

    @PostMapping("/save")
    public String insertFeeLog(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        feeService.save(file);
        return "redirect:/";
    }

}

