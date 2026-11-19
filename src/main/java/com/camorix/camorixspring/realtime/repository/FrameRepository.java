package com.camorix.camorixspring.realtime.repository;

import com.camorix.camorixspring.realtime.entity.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Frame 엔티티에 대한 JPA Repository 인터페이스
 * - 기본적인 CRUD 기능을 자동으로 제공함
 * - 식별자는 UUID 타입을 사용함
 */
public interface FrameRepository extends JpaRepository<Frame, UUID> {

}
