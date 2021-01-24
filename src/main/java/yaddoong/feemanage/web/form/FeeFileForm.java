package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter @Setter
public class FeeFileForm {

    private File file;
    private String extraField;
}
