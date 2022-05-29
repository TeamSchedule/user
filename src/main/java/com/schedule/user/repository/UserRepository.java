package com.schedule.user.repository;

import com.schedule.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);
    User getByLogin(String login);
    @Query("select m from app_user m where m.login LIKE CONCAT('%',:criteria,'%')")
    List<User> searchByNameContains(String criteria);
}
