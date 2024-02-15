package com.green.greengram4.user;

import com.green.greengram4.common.ProviderTypeEnum;
import com.green.greengram4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByProviderTypeAndUid(ProviderTypeEnum providerType, String uid);
    // findBy를 네이밍을 사용하여 select 쿼리문 작성하는 효과
    // findBy = where절, where절에 providerType과 Uid를 넣어줌
}
