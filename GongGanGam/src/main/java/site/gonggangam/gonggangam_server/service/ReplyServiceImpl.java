package site.gonggangam.gonggangam_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gonggangam.gonggangam_server.config.ResponseCode;
import site.gonggangam.gonggangam_server.config.exceptions.GeneralException;
import site.gonggangam.gonggangam_server.domain.diary.Diary;
import site.gonggangam.gonggangam_server.domain.reply.Reply;
import site.gonggangam.gonggangam_server.domain.users.Users;
import site.gonggangam.gonggangam_server.dto.diary.DiaryResponseDto;
import site.gonggangam.gonggangam_server.dto.reply.ReplyPreviewResponseDto;
import site.gonggangam.gonggangam_server.dto.reply.ReplyRequestDto;
import site.gonggangam.gonggangam_server.dto.reply.ReplyResponseDto;
import site.gonggangam.gonggangam_server.repository.DiaryRepository;
import site.gonggangam.gonggangam_server.repository.ReplyRepository;
import site.gonggangam.gonggangam_server.repository.UsersRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final UsersRepository usersRepository;
    private final DiaryRepository diaryRepository;
    private final ReplyRepository replyRepository;

    @Override
    public ReplyResponseDto getReply(Long replyId) throws GeneralException {
        Reply reply = replyRepository.findByReplyIdAndRejectedAndIsVisible(replyId, false, true).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND);
        });

        return ReplyResponseDto.toDto(reply);
    }

    @Override
    public List<ReplyPreviewResponseDto> getReplies(Long userId, Integer page, Integer pageSize) {
        List<Reply> replies = replyRepository.findAllByWriter_UserIdAndRejectedAndIsVisible(userId, false, true);

        return replies.stream()
                .map(ReplyPreviewResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReplyResponseDto postReply(Long userId, ReplyRequestDto.Post body) throws GeneralException {
        Diary targetDiary = diaryRepository.findByDiaryIdAndIsVisible(body.getDiaryId(), true).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND);
        });

        Users writer = usersRepository.findById(userId).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND_USER);
        });

        Reply newReply = Reply.builder()
                .diary(targetDiary)
                .writer(writer)
                .content(body.getContent())
                .isVisible(true)
                .build();

        replyRepository.save(newReply);
        return ReplyResponseDto.toDto(newReply);
    }

    @Override
    @Transactional
    public ReplyResponseDto putReply(Long replyId, ReplyRequestDto.Put body) throws GeneralException {
        Reply reply = replyRepository.findByReplyIdAndRejectedAndIsVisible(replyId, false, true).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND);
        });

        reply.update(body.getContent());
        replyRepository.save(reply);

        return ReplyResponseDto.toDto(reply);
    }

    @Override
    @Transactional
    public void rejectReply(Long replyId) {
        Reply reply = replyRepository.findByReplyIdAndRejectedAndIsVisible(replyId, false, true).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND);
        });

        reply.reject();
        replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findByReplyIdAndIsVisible(replyId, true).orElseThrow(() -> {
            throw new GeneralException(ResponseCode.NOT_FOUND);
        });

        reply.delete();
        replyRepository.save(reply);
    }
}