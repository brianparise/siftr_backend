package com.siftr.Repository;

import com.siftr.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    @Query("select u from AppUser u where u.email = ?1")
    AppUser findByEmailAddress(String emailAddress);
}
