package com.its.library.entity;

import com.its.library.dto.WishDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter@Getter
@Table(name = "wishlist")
public class WishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlistId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private BookEntity bookEntity;

    @Column(name = "memberName",length = 20)
    private String memberName;

    @OneToMany(mappedBy = "wishEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NoticeEntity> noticeEntityList = new ArrayList<>();

    public static WishEntity saveWriterEntity(WishDTO wishDTO, MemberEntity memberEntity) {
        WishEntity wishEntity = new WishEntity();
        wishEntity.setMemberEntity(memberEntity);
        wishEntity.setMemberName(wishDTO.getMemberName());
        return wishEntity;
    }

    public static WishEntity saveBookEntity(WishDTO wishDTO, BookEntity bookEntity) {
        WishEntity wishEntity = new WishEntity();
        wishEntity.setBookEntity(bookEntity);
        wishEntity.setMemberName(wishDTO.getMemberName());
        return wishEntity;
    }


    //관심 - 맴버 manyToOne ㅇ
    //관심 - 책 manyToOne ㅇ

}
