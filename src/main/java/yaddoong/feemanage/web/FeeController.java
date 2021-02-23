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
import yaddoong.feemanage.domain.fee.*;
import yaddoong.feemanage.service.fee.FeeService;
import yaddoong.feemanage.web.form.FeeLogForm;
import yaddoong.feemanage.web.form.UserFeeForm;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/fee")
@Controller
public class FeeController {

    private final FeeService feeService;
    private final FeeLogRepository feeLogRepository;
    private final FeeFileLogRepository feeFileLogRepository;

    @GetMapping(value = "/new")
    public String saveForm() {
        return "fee/saveForm";
    }

    @PostMapping("/save")
    public String insertFeeLog(@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        feeService.save(file);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String listView(Model model) throws IOException, ParseException {

        LocalDateTime queryStartDate = LocalDate.now().minusMonths(1).atTime(LocalTime.MIN);
        LocalDateTime queryEndDate = LocalDate.now().atTime(LocalTime.MAX);
        String formStartDate = queryStartDate.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        String formEndDate = queryEndDate.toLocalDate().format(DateTimeFormatter.ISO_DATE);

        FeeLogForm form = new FeeLogForm();
        form.setStartDate(formStartDate);
        form.setEndDate(formEndDate);
        return "fee/list";
    }

    @PostMapping("/list")
    public String list(FeeLogForm form) {
        return "fee/list";
    }


    @GetMapping(value = "/userFeeList")
    public String userFeeList(Model model) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
        Page<FeeFileLog> all = feeFileLogRepository.findAll(pageable);
        if (all.getContent().size() == 0) {
            model.addAttribute("lastUpdateData", "업데이트 기록 없음");
            return "fee/userFeeList";
        }
        model.addAttribute
                ("lastUpdateData",all.getContent()
                        .get(0)
                        .getName()
                        .replaceAll(".xlsx","") + " 기준");
        //미납금 + (오늘 날짜 - 오늘 날짜가 15보다 적으면 저번 달 15일 or 이번달 15일)*
        List<FeeLogProjection> list = feeLogRepository.findGroupByName();
        List<UserFeeForm> feeFormList = new ArrayList<>();
        for (FeeLogProjection feeLogProjection : list) {
            UserFeeForm form = new UserFeeForm();
            form.setName(feeLogProjection.getName());
            form.setPrice(feeLogProjection.getPrice());
            form.setUnpaid(feeLogProjection.getUnpaid());
            feeFormList.add(form);
        }
        model.addAttribute("fees", feeFormList);

        return "fee/userFeeList";
    }

    @GetMapping("/etcList")
    public String etcList(Model model) {
        return "fee/etcList";
    }



}

