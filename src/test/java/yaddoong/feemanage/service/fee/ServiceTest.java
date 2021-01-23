package yaddoong.feemanage.service.fee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.web.FeeController;


import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Autowired
    private FeeLogRepository feeLogRepository;

    @Test
    public void 엑셀저장_확인() throws Exception {
        //given

        mvc.perform(
                get("/fee/save"))
        .andExpect(status().isOk());

        List<FeeLog> all = feeLogRepository.findAll();

        //when

        //then
        assertThat(all.get(0).getContents()).isEqualTo("김중기");
        assertThat(all.get(0).getDate().toString()).isEqualTo("2020-01-24 18:16:00.0");
        assertThat(all.get(1).getContents()).isEqualTo("방정훈실수");
        assertThat(all.get(1).getDate().toString()).isEqualTo("2020-02-17 17:47:06.0");
    }

}
