package com.example.processo_seletivo_santa_paula.service;

import com.example.processo_seletivo_santa_paula.entity.Exame;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final ExameService exameService;

    public void processarExcel(MultipartFile file) throws IOException {
        List<Exame> exames = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        // Pular cabeçalho
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Exame exame = criarExameFromRow(row);
            if (exame != null) {
                exames.add(exame);
            }
        }

        workbook.close();
        exameService.saveAll(exames);
    }

    private Exame criarExameFromRow(Row row) {
        try {
            Exame exame = new Exame();

            // Data
            if (getCellValue(row.getCell(0)) != null) {
                exame.setData(LocalDate.parse(getCellValue(row.getCell(0)).split(" ")[0]));
            }

            // Hora
            if (getCellValue(row.getCell(1)) != null) {
                String horaStr = getCellValue(row.getCell(1));
                if (horaStr.contains(" ")) {
                    horaStr = horaStr.split(" ")[1];
                }
                exame.setHora(LocalTime.parse(horaStr));
            }

            exame.setOs(getCellValue(row.getCell(2)));
            exame.setPaciente(getCellValue(row.getCell(3)));
            exame.setSexo(getCellValue(row.getCell(4)));

            // Data Nascimento
            if (getCellValue(row.getCell(5)) != null) {
                exame.setDataNascimento(LocalDate.parse(getCellValue(row.getCell(5)).split(" ")[0]));
            }

            exame.setBairro(getCellValue(row.getCell(6)));
            exame.setSolicitante(getCellValue(row.getCell(7)));
            exame.setUnidadeColeta(getCellValue(row.getCell(8)));
            exame.setFontePagadora(getCellValue(row.getCell(9)));
            exame.setAtendente(getCellValue(row.getCell(10)));
            exame.setMnemonic(getCellValue(row.getCell(11)));
            exame.setProcedimento(getCellValue(row.getCell(12)));

            // Valores numéricos
            exame.setValorFaturamento(getBigDecimalValue(row.getCell(13)));
            exame.setValorBruto(getBigDecimalValue(row.getCell(14)));
            exame.setValorDesconto(getBigDecimalValue(row.getCell(15)));
            exame.setValorImposto(getBigDecimalValue(row.getCell(16)));
            exame.setValorLiquido(getBigDecimalValue(row.getCell(17)));

            return exame;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private BigDecimal getBigDecimalValue(Cell cell) {
        try {
            if (cell == null) return BigDecimal.ZERO;

            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);
                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public void exportarExcel(HttpServletResponse response) throws IOException {
        List<Exame> exames = exameService.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Exames");

        // Cabeçalhos
        Row header = sheet.createRow(0);
        String[] colunas = {"ID", "DATA", "HORA", "OS", "PACIENTE", "SEXO", "DN", "BAIRRO", "Solicitante", "Un. coleta", "Fonte pag", "Atendente", "Mnemônico", "Procedimento", "Vl Fat", "Vl. Blc Bruto", "Vl. Blc Desconto O.S.", "Vl. Blc Imposto", "Vl. Blc Líquido"};
        for (int i = 0; i < colunas.length; i++) {
            header.createCell(i).setCellValue(colunas[i]);
        }

        int rowIdx = 1;
        for (Exame exame : exames) {
            Row row = sheet.createRow(rowIdx++);
            int col = 0;
            row.createCell(col++).setCellValue(exame.getId() != null ? exame.getId() : 0);
            row.createCell(col++).setCellValue(exame.getData() != null ? exame.getData().toString() : "");
            row.createCell(col++).setCellValue(exame.getHora() != null ? exame.getHora().toString() : "");
            row.createCell(col++).setCellValue(exame.getOs() != null ? exame.getOs() : "");
            row.createCell(col++).setCellValue(exame.getPaciente() != null ? exame.getPaciente() : "");
            row.createCell(col++).setCellValue(exame.getSexo() != null ? exame.getSexo() : "");
            row.createCell(col++).setCellValue(exame.getDataNascimento() != null ? exame.getDataNascimento().toString() : "");
            row.createCell(col++).setCellValue(exame.getBairro() != null ? exame.getBairro() : "");
            row.createCell(col++).setCellValue(exame.getSolicitante() != null ? exame.getSolicitante() : "");
            row.createCell(col++).setCellValue(exame.getUnidadeColeta() != null ? exame.getUnidadeColeta() : "");
            row.createCell(col++).setCellValue(exame.getFontePagadora() != null ? exame.getFontePagadora() : "");
            row.createCell(col++).setCellValue(exame.getAtendente() != null ? exame.getAtendente() : "");
            row.createCell(col++).setCellValue(exame.getMnemonic() != null ? exame.getMnemonic() : "");
            row.createCell(col++).setCellValue(exame.getProcedimento() != null ? exame.getProcedimento() : "");
            row.createCell(col++).setCellValue(exame.getValorFaturamento() != null ? exame.getValorFaturamento().doubleValue() : 0);
            row.createCell(col++).setCellValue(exame.getValorBruto() != null ? exame.getValorBruto().doubleValue() : 0);
            row.createCell(col++).setCellValue(exame.getValorDesconto() != null ? exame.getValorDesconto().doubleValue() : 0);
            row.createCell(col++).setCellValue(exame.getValorImposto() != null ? exame.getValorImposto().doubleValue() : 0);
            row.createCell(col++).setCellValue(exame.getValorLiquido() != null ? exame.getValorLiquido().doubleValue() : 0);
        }

        for (int i = 0; i < colunas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=exames.xlsx");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }
}
