package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.file.FileUpload;
import yaddoong.feemanage.service.file.FileService;
import yaddoong.feemanage.web.file.dto.FileDto;
import yaddoong.feemanage.web.file.form.FileForm;

import java.io.IOException;
import java.util.List;

/**
 * @RequiredArgsConstructor final필드나 @NonNull이 붙은 필드에 대해 생성자를 생성해줍니다.
 */
@RequiredArgsConstructor
@RequestMapping(value = "/file")
@Controller
public class FileController {

    @Autowired
    MessageSource messageSource;

    // 파일 서비스
    private final FileService fileService;

    /**
     * 파일업로드 화면 이동
     *
     * @return
     */
    @GetMapping(value = "/upload")
    public String upload(@ModelAttribute("file") FileForm form) {
        return "file/upload";
    }

    /**
     * 파일 업로드
     *
     * @param form
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @PostMapping("/upload")
    public String upload(
            @ModelAttribute("file") FileForm form,
            BindingResult bindingResult) throws Exception {

        // 첨부된 파일들의 확장자를 확인한다.(.xlsx 확장자만 업로드 가능)
        for (MultipartFile uploadFile : form.getUploadFiles()) {
            // 업로드된 파일의 확장자를 가져온다.
            String extension = FilenameUtils.getExtension(
                    uploadFile.getOriginalFilename()
            );

            // xlsx 파일이 아니면 알림 메시지를 보낸다.
            if (!messageSource.getMessage(
                            "upload.file.extention",
                            null,
                            null)
                    .equals(extension)) {
                bindingResult.reject("upload.file.xlsx.notice", null);
                return "file/upload";
            }
        }

        /**
         * 카카오거래내역 엑셀파일이 맞는지 확인
         */
        for (MultipartFile uploadFile : form.getUploadFiles()) {

            try (XSSFWorkbook excel = new XSSFWorkbook(uploadFile.getInputStream())) {
                String sheetName = excel.getSheetName(0); //sheet 명
                XSSFRow row = excel.getSheetAt(0) // 첫 행
                        .getRow(0);
                XSSFCell cell = row.getCell(1); // 제목 셀

                // 카카오뱅크 거래내역 문자로 카카오뱅크 거래내역 파일인지 확인
                if (!(sheetName.equals(
                        messageSource.getMessage("kakaobank.transaction.history", null, null)
                ) && cell.toString().equals(
                        messageSource.getMessage("kakaobank.transaction.history", null, null)
                ))) {
                    bindingResult.reject("xlsx.gathering.passbook.notice", null);
                    return "file/upload";
                }

            } catch (OLE2NotOfficeXmlFileException e) {
                bindingResult.reject("upload.file.password.notice", null);
                return "file/upload";
            }
        }

        // form 파일에 담겨진 정보를 dto 파일에 저장한다.
        FileDto fileDto = FileDto.builder()
                .form(form)
                .build();

        // 파일에 들어있는 정보를 DB에 입력하는 작업을 진행한다.
        fileService.saveFileInfo(fileDto);

        return "file/upload";
    }

    /**
     * 파일 업로드 내역 메소드
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) throws Exception {
        List<FileUpload> fileUploadHistory = fileService.getFileUploadHistory();
        model.addAttribute("transactionHistoryList", fileUploadHistory);
        return "file/list";
    }
}
