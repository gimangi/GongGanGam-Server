package site.gonggangam.gonggangam_server.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class DiaryRequestDto {

    @Data
    @Builder
    @Schema(description = "일기 작성")
    public static class Post {
        @Schema(description = "작성 일자", defaultValue = "2023-01-04", required = true)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private final LocalDate date;
        @Schema(description = "이모지", defaultValue = "happy", required = true)
        private final String emoji;
        @Schema(description = "내용", defaultValue = "내 손 잡아준 너 매일 아침 눈을 뜰 때면", required = true)
        private final String content;
        @Schema(description = "공유 여부", defaultValue = "true", required = true)
        private final Boolean shareAgreed;
    }

    @Data
    @Builder
    @Schema(description = "일기 수정")
    public static class Put {
        @Schema(description = "일기 ID", defaultValue = "12", required = true)
        private final Long diaryId;
        @Schema(description = "이모지", defaultValue = "happy", required = true)
        private final String emoji;
        @Schema(description = "내용", defaultValue = "내 손 잡아준 너 매일 아침 눈을 뜰 때면", required = true)
        private final String content;
        @Schema(description = "공유 여부", defaultValue = "true", required = true)
        private final Boolean shareAgreed;
    }
}
