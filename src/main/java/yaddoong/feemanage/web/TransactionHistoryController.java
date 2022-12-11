package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistory;
import yaddoong.feemanage.service.transactionhistory.TransactionHistoryService;
import yaddoong.feemanage.utils.CommonUtils;
import yaddoong.feemanage.web.transactionhistory.dto.TransactionHistoryQueryDto;
import yaddoong.feemanage.web.transactionhistory.form.TransactionHistorySearchForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 거래내역 controller class
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/transactionhistory")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    private final CommonUtils commonUtils;

    /**
     * 거래내역 조회 메뉴 이동 메소드
     * @param model
     * @return
     */
    @GetMapping
    public String getTransactionHistory(Model model) {

        Map<String, Object> map = new HashMap<>();

        // 기본 검색 조건 세팅
        map.put("startDateTime", commonUtils.dayCalcLocalDateTime(60));
        map.put("endDateTime", commonUtils.dayCalcLocalDateTime(0));
        map.put("contents", "");
        
        // 검색 조건에 맞는 거래내역 조회
        TransactionHistorySearchForm form =
                new TransactionHistorySearchForm(map);
        TransactionHistoryQueryDto dto = TransactionHistoryQueryDto.builder()
                .form(form)
                .build();
        List<TransactionHistory> transactionHistoryList =
                transactionHistoryService.getTransactionHistoryList(dto);

        model.addAttribute("transactionHistoryList", transactionHistoryList);
        model.addAttribute("form", form);

        return "transactionhistory/list";
    }

}
