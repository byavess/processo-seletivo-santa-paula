package com.example.processo_seletivo_santa_paula.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exames")
@Data
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private LocalTime hora;
    private String os;
    private String paciente;
    private String sexo;
    private LocalDate dataNascimento;
    private String bairro;
    private String solicitante;
    private String unidadeColeta;
    private String fontePagadora;
    private String atendente;
    private String mnemonic;
    private String procedimento;
    private BigDecimal valorFaturamento;
    private BigDecimal valorBruto;
    private BigDecimal valorDesconto;
    private BigDecimal valorImposto;
    private BigDecimal valorLiquido;
}