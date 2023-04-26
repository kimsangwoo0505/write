package com.sparta.write.repository;

import com.sparta.write.entity.Write;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WriteRepository extends JpaRepository<Write, Long> {
    List<Write> findAllByOrderByModifiedAtDesc();
    Optional<Write> findByIdAndUsername(Long id, String username);//product id와 user id가 일치하는 Product를 가져옴(?)
}
