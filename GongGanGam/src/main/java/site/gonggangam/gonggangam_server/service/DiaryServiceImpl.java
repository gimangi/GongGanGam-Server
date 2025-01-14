package site.gonggangam.gonggangam_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gonggangam.gonggangam_server.config.ResponseCode;
import site.gonggangam.gonggangam_server.config.exceptions.GeneralException;
import site.gonggangam.gonggangam_server.domain.diary.Diary;
import site.gonggangam.gonggangam_server.domain.diary.ShareDiary;
import site.gonggangam.gonggangam_server.domain.users.Users;
import site.gonggangam.gonggangam_server.service.dto.diary.*;
import site.gonggangam.gonggangam_server.service.dto.upload_file.UploadFileDto;
import site.gonggangam.gonggangam_server.domain.repository.DiaryRepository;
import site.gonggangam.gonggangam_server.domain.repository.ShareDiaryRepository;
import site.gonggangam.gonggangam_server.domain.repository.UsersRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService {

    private static final Long CALENDAR_SCOPE_WEEKS = 2L;

    private final UploadFileService uploadFileService;

    private final UsersRepository usersRepository;
    private final DiaryRepository diaryRepository;
    private final ShareDiaryRepository shareDiaryRepository;


    @Override
    @Transactional
    public DiaryResponseDto postDiary(Long userId, DiaryRequestDto.PostDiary request) throws GeneralException {
        Users writer = usersRepository.findById(userId).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND_USER);
        });

        validateDiaryDate(request.getDate(), writer);

        UploadFileDto uploadFileDto = null;

        if (request.getImgFile() != null) {
            uploadFileDto = uploadFileService.save(request.getImgFile());
        }

        Diary diary = Diary.builder()
                .content(request.getContent())
                .emoji(request.getEmoji())
                .isVisible(true)
                .shareAgreed(request.getShareAgreed())
                .writer(writer)
                .diaryDate(request.getDate())
                .imgUrl(uploadFileDto != null ? uploadFileDto.getUploadedUrl() : null)
                .build();

        diaryRepository.save(diary);
        return DiaryResponseDto.of(diary);
    }

    private void validateDiaryDate(LocalDate date, Users writer) throws GeneralException {
        LocalDate today = LocalDate.now();

        // 오늘 이후의 날짜에 작성하려는 경우
        if (date.isAfter(today)) {
            throw new GeneralException(ResponseCode.DIARY_DATE_INVALID);
        }

        // 이미 작성된 일기가 있는 날짜인 경우
        if (!diaryRepository.getByUserIdAndDate(writer.getUserId(), date).isEmpty()) {
            throw new GeneralException(ResponseCode.DIARY_DATE_CONFLICT);
        }
    }

    @Override
    public Page<SharedDiaryResponseDto> getSharedDiaries(Long userId, Pageable pageable) {
        Page<ShareDiary> diaries = shareDiaryRepository.findByReceiverUserId(userId, pageable);

        return new PageImpl<>(
                diaries.stream()
                .map(SharedDiaryResponseDto::of)
                .collect(Collectors.toList())
        );
    }

    @Override
    public DiaryResponseDto getDiary(Long diaryId) throws GeneralException {
        Diary diary = diaryRepository.getByDiaryId(diaryId)
                .orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));

        return DiaryResponseDto.of(diary);
    }

    @Override
    @Transactional
    public CalendarResponseDto getDiaries(Long userId, Integer year, Integer month) {
        LocalDate dest = LocalDate.of(year, month, 1);
        LocalDate start = dest.minusWeeks(CALENDAR_SCOPE_WEEKS);
        LocalDate end = dest.plusMonths(1).plusWeeks(CALENDAR_SCOPE_WEEKS);

        List<Diary> diaries = diaryRepository.getByUserIdAndBetweenDate(userId, start, end);

        return CalendarResponseDto.builder()
                .prevMonth(getCalendarResponseByMonth(diaries, start.getMonthValue()))
                .destMonth(getCalendarResponseByMonth(diaries, dest.getMonthValue()))
                .nextMonth(getCalendarResponseByMonth(diaries, end.getMonthValue()))
                .build();
    }

    private List<DiaryPreviewResponseDto> getCalendarResponseByMonth(List<Diary> diaries, int month) {
        return diaries
                .stream()
                .filter(diary -> diary.getDiaryDate().getMonthValue() == month)
                .map(DiaryPreviewResponseDto::of)
                .toList();
    }

    @Override
    public DiaryResponseDto putDiary(Long diaryId, DiaryRequestDto.PutDiary request) throws GeneralException {
        Diary diary = diaryRepository.getByDiaryId(diaryId)
                .orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));

        diary.update(request.getEmoji(), request.getContent(), request.getShareAgreed());

        diaryRepository.save(diary);
        return DiaryResponseDto.of(diary);
    }

    @Override
    public void deleteDiary(Long diaryId) throws GeneralException {
        Diary diary = diaryRepository.getByDiaryId(diaryId)
                .orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));

        diary.delete();
        diaryRepository.save(diary);
    }

}
