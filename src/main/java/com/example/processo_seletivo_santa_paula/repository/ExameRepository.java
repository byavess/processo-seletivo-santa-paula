package com.example.processo_seletivo_santa_paula.repository;


import com.example.processo_seletivo_santa_paula.entity.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
}
