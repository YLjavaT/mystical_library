package com.its.library.entity;

import com.its.library.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Getter@Setter
@Table(name = "comment")
public class CommentEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episodeId")
    private EpisodeEntity episodeEntity;

    @Column(name = "memberName",nullable = false,length = 20)
    private String memberName;
    @Column(name = "contents",nullable = false,length = 500)
    private String contents;



    //댓글-회차 ManyToOne ㅇ
    //댓글-맴버 ManyToOne ㅇ
    public static CommentEntity saveEntity(CommentDTO commentDTO, MemberEntity memberEntity, EpisodeEntity episodeEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setMemberEntity(memberEntity);
        commentEntity.setEpisodeEntity(episodeEntity);
        commentEntity.setMemberName(memberEntity.getMemberName());
        commentEntity.setContents(commentDTO.getContents());
        return commentEntity;
    }
}
