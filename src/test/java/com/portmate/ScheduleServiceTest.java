package com.portmate;

import com.portmate.domain.schedule.dto.request.ScheduleCreateRequest;
import com.portmate.domain.schedule.entity.Schedule;
import com.portmate.domain.schedule.exception.FileNotSupportException;
import com.portmate.domain.schedule.repository.ScheduleRepository;

import com.portmate.domain.schedule.service.ScheduleService;
import com.portmate.domain.schedule.vo.ScheduleContent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ScheduleServiceTest {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    List<String> createdIds = new ArrayList<>();

    @Test
    public void 일정표를_등록한다() throws IOException {
        // given
        var request = new ScheduleCreateRequest("부산항", LocalDate.now(), LocalDate.now().plusDays(7));
        var inputStream = getClass().getResourceAsStream("/test-data/test.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                inputStream
        );

        // when
        Schedule saved = scheduleService.uploadExcel(mockMultipartFile, request);

        // then
        var all = scheduleRepository.findAll();
        assertThat(all).isNotEmpty();

        // scheduleContents는 이제 VO 객체 리스트이므로 fieldName 등을 추출해야 함
        assertThat(all.get(0).getScheduleContents())
                .extracting(ScheduleContent::getColumn)
                .containsExactly(
                        "prtAgCd",
                        "prtAgNm",
                        "etryptYear",
                        "etryptCo",
                        "clsgn",
                        "vsslNm",
                        "vsslNltyCd",
                        "vsslNltyNm",
                        "vsslKndCd",
                        "vsslKndNm",
                        "etryptPurpsCd",
                        "etryptPurpsNm",
                        "frstDpmprtNatPrtCd",
                        "frstDpmprtPrtNm",
                        "prvsDpmprtNatPrtCd",
                        "prvsDpmprtPrtNm",
                        "nxlnptNatPrtCd",
                        "nxlnptPrtNm",
                        "dstnNatPrtCd",
                        "dstnPrtNm",
                        "details/detail/0/reqstSeNm",
                        "details/detail/0/etryndNm",
                        "details/detail/0/etryptDt",
                        "details/detail/0/ibobprtNm",
                        "details/detail/0/laidupFcltyCd",
                        "details/detail/0/laidupFcltySubCd",
                        "details/detail/0/laidupFcltyNm",
                        "details/detail/0/tugYn",
                        "details/detail/0/piltgYn",
                        "details/detail/0/ldadngFrghtClCd",
                        "details/detail/0/ldadngTon",
                        "details/detail/0/trnpdtTon",
                        "details/detail/0/landngFrghtTon",
                        "details/detail/0/grtg",
                        "details/detail/0/intrlGrtg",
                        "details/detail/0/satmntEntrpsNm",
                        "details/detail/0/crewCo",
                        "details/detail/0/koranCrewCo",
                        "details/detail/0/frgnrCrewCo",
                        "details/detail/0/mrNum",
                        "details/detail/0/tkoffPrrrnDt",
                        "details/detail/1/reqstSeNm",
                        "details/detail/1/etryndNm",
                        "details/detail/1/tkoffDt",
                        "details/detail/1/ibobprtNm",
                        "details/detail/1/laidupFcltyCd",
                        "details/detail/1/laidupFcltySubCd",
                        "details/detail/1/laidupFcltyNm",
                        "details/detail/1/tugYn",
                        "details/detail/1/piltgYn",
                        "details/detail/1/ldadngFrghtClCd",
                        "details/detail/1/ldadngTon",
                        "details/detail/1/trnpdtTon",
                        "details/detail/1/ldFrghtTon",
                        "details/detail/1/grtg",
                        "details/detail/1/intrlGrtg",
                        "details/detail/1/satmntEntrpsNm",
                        "details/detail/1/crewCo",
                        "details/detail/1/koranCrewCo",
                        "details/detail/1/frgnrCrewCo",
                        "details/detail/1/mrNum",
                        "details/detail/1/dstnEtryptDt",
                        "tkoffDt",
                        "tkoffPrrrnDt",
                        "delay_minutes",
                        "delay_flag"
                );
    }


    @Test
    void 잘못된_파일을_업로드한다() throws IOException {
        var request = new ScheduleCreateRequest("부산항", LocalDate.now(), LocalDate.now().plusDays(7));
        var inputStream = getClass().getResourceAsStream("/test-data/test.img");

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.img",
                "image/png",
                inputStream
        );

        assertThrows(FileNotSupportException.class,
                () -> scheduleService.uploadExcel(mockMultipartFile, request));
    }
}
