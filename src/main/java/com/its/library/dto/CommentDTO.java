package com.its.library.dto;

import com.its.library.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private Long memberId;
    private Long episodeId;
    private String commentEpisode;
    private Long bookId;
    private String memberImgName;
    private String memberName;
    private String contents;
    private LocalDateTime createdTime;
    private boolean commentReport;

    public static CommentDTO findDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setMemberId(commentEntity.getMemberEntity().getId());
        commentDTO.setEpisodeId(commentEntity.getEpisodeEntity().getId());
        commentDTO.setCommentEpisode(commentEntity.getCommentEpisode());
        commentDTO.setBookId(commentEntity.getBookId());
        commentDTO.setMemberImgName(commentEntity.getMemberImgName());
        commentDTO.setMemberName(commentEntity.getMemberName());
        commentDTO.setContents(commentEntity.getContents());
        commentDTO.setCreatedTime(commentEntity.getCreatedDateTime());
        commentDTO.setCommentReport(false) ;
        return commentDTO;
    }

}
