package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistory;
import yaddoong.feemanage.domain.transactionHistory.TransactionHistoryEtc;
import yaddoong.feemanage.service.transactionHistory.TransactionHistoryService;
import yaddoong.feemanage.utils.CommonUtils;
import yaddoong.feemanage.web.transactionHistory.dto.TransactionHistoryQueryDto;
import yaddoong.feemanage.web.transactionHistory.form.TransactionHistoryEtcUpdateForm;
import yaddoong.feemanage.web.transactionHistory.form.TransactionHistorySearchForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 거래내역 controller class
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/transactionHistory")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    private final CommonUtils commonUtils;

    /**
     * 거래내역 조회 메뉴 이동 메소드
     *
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

        return "transaction-history/list";
    }

    /**
     * 거래내역 검색 조회 메소드
     *
     * @param form
     * @param model
     * @return
     */
    @PostMapping
    public String getTransactionHistory(TransactionHistorySearchForm form, Model model) {

        TransactionHistoryQueryDto dto = TransactionHistoryQueryDto.builder()
                .form(form)
                .build();
        List<TransactionHistory> transactionHistoryList =
                transactionHistoryService.getTransactionHistoryList(dto);

        model.addAttribute("transactionHistoryList", transactionHistoryList);
        model.addAttribute("form", form);

        // TODO: 2022/03/06 로그인 한 모임 기준 데이터 불러오기 추가 로직 구현
        return "transaction-history/list";

    }

    /**
     * 기타내역을 조회하는 메서드
     *
     * @param model
     * @return
     */
    @GetMapping("/etcList")
    public String etcList(Model model) {
        List<TransactionHistoryEtc> transactionHistories =
                transactionHistoryService.findTransactionHistoryEtcAll();

        model.addAttribute("transactionHistoryEtcs", transactionHistories);
        model.addAttribute("form", new TransactionHistoryEtcUpdateForm());

        return "transaction-history/etcList";
    }

    /**
     * 기타 내역 개별 변경 메서드
     * @param form
     * @return
     */
    @PostMapping("/transactionHistoryEtcUpdate")
    public String transactionHistoryAllUpdate(TransactionHistoryEtcUpdateForm form) {

        Optional<TransactionHistoryEtc> transactionHistoryEtc =
                transactionHistoryService.findTransactionHistoryEtcById(form.getId());
        transactionHistoryEtc.get().updateMemo(form.getMemo());
        transactionHistoryService.transactionHistoryEtcSave(transactionHistoryEtc.get());

        return "redirect:/transactionHistory/etcList";
    }

    @GetMapping("/transactionHistoryEtcAllUpdate")
    public String transactionHistoryEtcAllUpdate() {
        transactionHistoryService.transactionHistoryEtcAllUpdate();
        return "redirect:/transactionHistory/etcList";
    }
}
