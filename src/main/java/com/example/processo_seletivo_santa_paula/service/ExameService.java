package com.example.processo_seletivo_santa_paula.service;

import com.example.processo_seletivo_santa_paula.entity.Exame;
import com.example.processo_seletivo_santa_paula.repository.ExameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExameService {


    private final ExameRepository exameRepository;

    public List<Exame> findAll() {
        return exameRepository.findAll();
    }

    public Optional<Exame> findById(Long id) {
        return exameRepository.findById(id);
    }

    public void save(Exame exame) {
        exameRepository.save(exame);
    }

    public void deleteById(Long id) {
        exameRepository.deleteById(id);
    }

    public void saveAll(List<Exame> exames) {
        exameRepository.saveAll(exames);
    }
}
