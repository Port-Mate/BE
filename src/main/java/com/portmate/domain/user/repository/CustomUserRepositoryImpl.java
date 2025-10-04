package com.portmate.domain.user.repository;

import com.portmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<User> findByStatus(User.UserStatus status, Pageable pageable) {
        Query query = new Query()
                .addCriteria(Criteria.where("userStatus").is(status))
                .with(pageable);


        List<User> users = mongoTemplate.find(query, User.class);

        // 전체 데이터 개수 조회 (페이지 적용 없이)
        long totalElements = mongoTemplate.count(query, User.class);

        // PageImpl을 통해 페이징 정보와 함께 반환
        return new PageImpl<>(users, pageable, totalElements);
    }
}
