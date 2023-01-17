package site.gonggangam.gonggangam_server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import site.gonggangam.gonggangam_server.config.ResponseCode;
import site.gonggangam.gonggangam_server.dto.DataResponseDto;
import site.gonggangam.gonggangam_server.dto.ResponseDto;
import site.gonggangam.gonggangam_server.dto.SuccessResponseDto;
import site.gonggangam.gonggangam_server.dto.report.ReportRequestDto;
import site.gonggangam.gonggangam_server.dto.report.ReportResponseDto;
import site.gonggangam.gonggangam_server.service.ReportService;

import java.util.List;

@Tag(name = "report", description = "신고 관련 API")
@RestController
@RequestMapping(value = "/api/report", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor

// TODO : userID
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "신고 등록", description = "type = { diary, reply, chat }. chat일 경우 chatRoomId를 입력해주세요.")
    @PostMapping
    public DataResponseDto<ReportResponseDto> postReport(
            @RequestBody ReportRequestDto.PostReport body
            ) {
        return DataResponseDto.of(reportService.postReport(1L, body));
    }

    @Operation(summary = "신고 내역 조회", description = "관리자 계정만 이용 가능. 조회할 신고 타입 type = { all, diary, reply, chat }")
    @GetMapping
    public DataResponseDto<List<ReportResponseDto>> getReport(
            @Schema(defaultValue = "diary")
            @RequestParam("type") String type
    ) {
        return DataResponseDto.of(reportService.getReports(type));
    }

    @Operation(summary = "신고 처리상태 변경", description = "신고 내역의 처리 상태를 변경합니다. 관리자 계정만 이용 가능합니다. 처리 상태 progress = { before, processing, completed }")
    @PutMapping("/{reportId}")
    public ResponseDto putReport(
            @RequestBody ReportRequestDto.PutReport body
    ) {
        reportService.putReport(body);
        return new SuccessResponseDto(ResponseCode.OK);
    }

}
