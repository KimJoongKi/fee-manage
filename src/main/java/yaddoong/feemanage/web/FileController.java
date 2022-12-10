package yaddoong.feemanage.web;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.web.file.form.FileForm;

import java.io.IOException;

/**
 * @RequiredArgsConstructor
 * final필드나 @NonNull이 붙은 필드에 대해 생성자를 생성해줍니다.
 */
@RequiredArgsConstructor
@RequestMapping(value = "/file")
@Controller
public class FileController {

    @Autowired
    MessageSource messageSource;

    /**
     * 파일업로드 화면 이동
     * @return
     */
    @GetMapping(value = "/upload")
    public String upload(@ModelAttribute("file") FileForm form) {
        return "file/upload";
    }

    /**
     * 파일 업로드
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
                bindingResult.reject("upload.file.warning.notice", null);
                return "file/upload";
            }
        }

        for (MultipartFile uploadFile : form.getUploadFiles()) {

            try (XSSFWorkbook excel = new XSSFWorkbook(uploadFile.getInputStream())) {

            } catch (OLE2NotOfficeXmlFileException e) {
                bindingResult.reject("upload.file.password.notice", null);
                return "file/upload";
            }


        }


        return "file/upload";
    }
}
