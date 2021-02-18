package yaddoong.feemanage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaTest {

    @Test
    public void 운영체제_확인() {
        String property = System.getProperty("os.name").toUpperCase();
        System.out.println("property = " + property);

        if(property.indexOf("WIN") >= 0) {
            System.out.println("윈도우");
            return;
        }
        System.out.println("노윈도우");
    }
}
