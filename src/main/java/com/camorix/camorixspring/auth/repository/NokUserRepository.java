package com.camorix.camorixspring.auth.repository;

import com.camorix.camorixspring.auth.entity.NokUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NokUserRepository extends JpaRepository<NokUser, UUID> {
    NokUser findByEmail(String email);
}
