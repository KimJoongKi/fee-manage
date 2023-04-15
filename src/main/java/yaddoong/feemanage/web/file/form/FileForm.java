package yaddoong.feemanage.web.file.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FileForm {
    private List<MultipartFile> uploadFiles;
}
