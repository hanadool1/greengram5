package com.green.greengram4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
// 나를 상속받으면 가지고 있는 것을 자식들에게 전달
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 변화를 감지하여 엔티티와 매핑된 테이블의 데이터를 조작(괄호 안 : 이벤트 리스너로 엔티티의 영속, 수정 이벤트를 감지하는 역할)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // 생성일을 기록하기 위해 LocalDateTime 타입의 필드에 @CreateDate 를 적용한다. 또한 생성일자는 수정되어서는 안되므로 @Column(updatable = false) 를 적용한다.
    // 이렇게 적용하면, 엔티티가 생성됨을 감지하고 그 시점을 createdAt 필드에 기록한다.

    @LastModifiedDate
    private LocalDateTime updatedAt;
    // 수정일을 기록하기 위해 LocalDateTime 타입의 필드에 @LastModifiedDate 를 적용한다. 이렇게 적용하면, 엔티티가 수정됨을 감지하고 그 시점을 updatedAt 필드에 기록한다.

}
