package com.green.greengram4.entity;

import com.green.greengram4.common.ProviderTypeEnum;
import com.green.greengram4.common.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "t_user", uniqueConstraints = {
        @UniqueConstraint(
                columnNames={"provider_type", "uid"}
        )
})
public class UserEntity extends BaseEntity{

    @Id
    // 데이터베이스 테이블의 기본 키(PK)와 객체의 필드를 매핑시켜주는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 기본 키를 자동으로 생성해주는 어노테이션
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long iuser;
    // pk는 Long 타입 사용

    @Column(length = 10, name = "provider_type", nullable = false)
    // @Column은 객체 필드를 테이블의 컬럼에 매핑시켜주는 어노테이션
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'LOCAL'")
    private ProviderTypeEnum providerType;

    @Column(length = 100, nullable = false)
    private String uid;

    @Column(length = 300, nullable = false)
    private String upw;

    @Column(length = 25, nullable = false)
    private String nm;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    @ColumnDefault("'USER'")
    private RoleEnum role;

    @Column(length = 2100)
    private String pic;

    @Column(length = 2100, name = "firebase_token")
    private String firebaseToken;


}
