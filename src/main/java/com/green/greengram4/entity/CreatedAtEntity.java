package com.green.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
// 나를 상속받으면 가지고 있는 것을 자식들에게 전달
@EntityListeners(AuditingEntityListener.class)
public class CreatedAtEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // 생성일을 기록하기 위해 LocalDateTime 타입의 필드에 @CreateDate 를 적용한다. 또한 생성일자는 수정되어서는 안되므로 @Column(updatable = false) 를 적용한다.
    // 이렇게 적용하면, 엔티티가 생성됨을 감지하고 그 시점을 createdAt 필드에 기록한다.
}
