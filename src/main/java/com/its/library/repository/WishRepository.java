package com.its.library.repository;

import com.its.library.entity.MemberEntity;
import com.its.library.entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<WishEntity, Long> {
    Optional<WishEntity> findByMemberEntityAndMemberName(MemberEntity memberEntity, String memberName);


}