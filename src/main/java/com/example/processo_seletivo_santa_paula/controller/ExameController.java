package com.example.processo_seletivo_santa_paula.controller;

import com.example.processo_seletivo_santa_paula.entity.Exame;
import com.example.processo_seletivo_santa_paula.service.ExameService;
import com.example.processo_seletivo_santa_paula.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExameController {


    private final ExameService exameService;


    private final ExcelService excelService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file, Model model) {
        try {
            excelService.processarExcel(file);
            model.addAttribute("message", "Arquivo processado com sucesso!");
        } catch (IOException e) {
            model.addAttribute("message", "Erro ao processar arquivo: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/lista")
    public String listarExames(Model model) {
        List<Exame> exames = exameService.findAll();
        model.addAttribute("exames", exames);
        return "lista";
    }

    @GetMapping("/editar/{id}")
    public String editarExame(@PathVariable Long id, Model model) {
        Optional<Exame> exame = exameService.findById(id);
        if (exame.isPresent()) {
            model.addAttribute("exame", exame.get());
            return "editar";
        }
        return "redirect:/lista";
    }

    @PostMapping("/salvar")
    public String salvarExame(@ModelAttribute Exame exame) {
        exameService.save(exame);
        return "redirect:/lista";
    }

    @GetMapping("/excluir/{id}")
    public String excluirExame(@PathVariable Long id) {
        exameService.deleteById(id);
        return "redirect:/lista";
    }

    @GetMapping("/exportar")
    public void exportarExcel(HttpServletResponse response) throws IOException {
        excelService.exportarExcel(response);
    }
}