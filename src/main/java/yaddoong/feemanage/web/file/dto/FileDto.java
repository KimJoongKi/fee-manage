package yaddoong.feemanage.web.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.web.file.form.FileForm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 업로드 하는 파일 리스트와 업로드 시간을 저장하는 class 파일
 */
@Getter
@NoArgsConstructor
public class FileDto {

    private List<MultipartFile> fileList = new ArrayList<MultipartFile>();
    private LocalDateTime uploadDateTime;

    @Builder
    public FileDto(FileForm form) {
        this.fileList = form.getUploadFiles();
        this.uploadDateTime = LocalDateTime.now();
    }

}
