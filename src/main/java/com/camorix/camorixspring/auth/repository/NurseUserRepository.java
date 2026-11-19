package com.camorix.camorixspring.auth.repository;

import com.camorix.camorixspring.auth.entity.NurseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NurseUserRepository extends JpaRepository<NurseUser, UUID> {
    NurseUser findByEmail(String email);
}
