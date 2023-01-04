package site.gonggangam.gonggangam_server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.gonggangam.gonggangam_server.dto.reply.ReplyPreviewResponseDto;
import site.gonggangam.gonggangam_server.dto.reply.ReplyRequestDto;
import site.gonggangam.gonggangam_server.dto.reply.ReplyResponseDto;

import java.util.List;

@Tag(name = "reply", description = "일기 답장 관련 API")
@RestController
@RequestMapping(value = "/api/reply", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReplyController {

    @Operation(summary = "답장 상세조회", description = "답장 내용과 원본 일기 내용을 상세조회합니다.")
    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDto> getReply(
            @PathVariable("replyId") Long replyId
    ) {
        return null;
    }

    @Operation(summary = "받은 답장목록 조회", description = "받은 답장 목록을 조회합니다. ")
    @GetMapping
    public ResponseEntity<List<ReplyPreviewResponseDto>> getReplies(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "pageSize") Integer pageSize
            ) {
        return null;
    }

    @Operation(summary = "답장 작성하기", description = "공유받은 일기에 답장을 작성합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "작성 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 답장을 작성한 일기입니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "403", description = "만료된 토큰입니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ReplyResponseDto> postReply(
            @RequestBody ReplyRequestDto.Post body
            ) {
        return null;
    }

    @Operation(summary = "답장 삭제하기", description = "작성한 답장을 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "403", description = "만료된 토큰입니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "403", description = "권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "404", description = "올바르지 않은 경로입니다.", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
            }
    )
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(
            @PathVariable("replyId") Long replyId
    ) {
        return null;
    }

    @Operation(summary = "답장 수정하기", description = "작성한 답장의 내용을 수정합니다.")
    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDto> putReply(
            @PathVariable("replyId") Long replyId,
            @RequestBody ReplyRequestDto.Put body
    ) {
        return null;
    }
}