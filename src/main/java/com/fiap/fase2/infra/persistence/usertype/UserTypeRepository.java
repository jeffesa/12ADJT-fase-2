package com.fiap.fase2.infra.persistence.usertype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeJpaEntity, UUID> {
}
