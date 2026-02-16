package sistemaventa.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import sistemaventa.model.Venta;
import sistemaventa.service.VentaService;

@Controller
@RequestMapping("/clientes")
public class ClientesReporteController {

    private final VentaService ventaService;
    private static final Logger logger = LoggerFactory.getLogger(ClientesReporteController.class);

    public ClientesReporteController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/reporte")
    public String verReporteClientes(Model model) {
        List<Venta> ventas = ventaService.obtenerTodas();
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        Map<Integer, Double> totals = new HashMap<>();  // key: clienteIdentificacion

        for (Venta v : ventas) {
            if (v.getFecha() == null) continue;
            LocalDate fecha = v.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (fecha.equals(today)) {
                String id = v.getClienteId(); // campo desnormalizado
                if (id != null) {
                    totals.put(Integer.valueOf(id), totals.getOrDefault(id, 0.0) + v.getTotal());
                }
            }
        }

        List<Map<String, Object>> clientesHoyList = new ArrayList<>();
        java.text.NumberFormat currencyFmt = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("es-CO"));

        for (Map.Entry<Integer, Double> entry : totals.entrySet()) {
            Map<String, Object> row = new HashMap<>();
            row.put("identificacion", entry.getKey());
            // Nota: ya no tenemos el objeto Cliente completo, pero podemos obtener datos desnormalizados de alguna venta
            // Para simplificar, podríamos buscar el cliente por ID si es necesario, pero aquí solo mostramos ID y total.
            // Si se necesita nombre, habría que buscarlo.
            row.put("total", entry.getValue());
            row.put("formattedTotal", currencyFmt.format(entry.getValue()));
            clientesHoyList.add(row);
        }

        // Ordenar por total descendente
        clientesHoyList.sort((a, b) -> Double.compare((Double) b.get("total"), (Double) a.get("total")));

        Integer topId = clientesHoyList.isEmpty() ? null : (Integer) clientesHoyList.get(0).get("identificacion");
        Double topTotal = clientesHoyList.isEmpty() ? 0.0 : (Double) clientesHoyList.get(0).get("total");

        model.addAttribute("clientesHoy", clientesHoyList);
        model.addAttribute("topClienteId", topId);
        model.addAttribute("topTotal", topTotal);
        model.addAttribute("topTotalFormatted", currencyFmt.format(topTotal));
        model.addAttribute("fechaReporte", today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return "reporte_clientes";
    }

    @GetMapping("/reporte/pdf")
    public void descargarPdf(HttpServletResponse response) {
        List<Venta> ventas = ventaService.obtenerTodas();
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        Map<Integer, Double> totals = new HashMap<>();

        for (Venta v : ventas) {
            if (v.getFecha() == null) continue;
            LocalDate fecha = v.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (fecha.equals(today)) {
                Integer id = Integer.valueOf(v.getClienteId());
                if (id != null) {
                    totals.put(id, totals.getOrDefault(id, 0.0) + v.getTotal());
                }
            }
        }

        List<Map.Entry<Integer, Double>> sorted = new ArrayList<>(totals.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            com.lowagie.text.pdf.PdfWriter.getInstance(document, baos);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("Reporte de Clientes - " + today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), headerFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            document.add(titulo);

            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(new float[]{2f, 2f});
            table.setWidthPercentage(100);
            table.addCell("Identificación");
            table.addCell("Total Comprado Hoy");

            java.text.NumberFormat currencyFmtPdf = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("es-CO"));

            int limit = Math.min(200, sorted.size());
            for (int i = 0; i < limit; i++) {
                Map.Entry<Integer, Double> e = sorted.get(i);
                table.addCell(String.valueOf(e.getKey()));
                table.addCell(currencyFmtPdf.format(e.getValue()));
            }

            document.add(table);

            if (!sorted.isEmpty()) {
                Map.Entry<Integer, Double> top = sorted.get(0);
                Paragraph topPar = new Paragraph("\nTop comprador del día: ID " + top.getKey() + " - Total: " + currencyFmtPdf.format(top.getValue()));
                topPar.setSpacingBefore(12f);
                document.add(topPar);
            }

            document.close();

            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_clientes_" + today + ".pdf");
            response.setContentLength(baos.size());
            try (ServletOutputStream os = response.getOutputStream()) {
                baos.writeTo(os);
                os.flush();
            }

        } catch (IOException | DocumentException e) {
            logger.error("Error al generar PDF reporte clientes: {}", e.getMessage(), e);
        }
    }
}