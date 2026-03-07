package com.server.mentorgrowth.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

        private final TemplateEngine templateEngine;

        public byte[] generateHtmlPdf(Transaction transaction) {
            // 1. Prepare data for the HTML template
            Context context = new Context();
            context.setVariable("transaction", transaction);

            // 2. Render HTML to a String
            String renderedHtml = templateEngine.process("receipt", context);

            // 3. Convert HTML String to PDF bytes
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(renderedHtml, null);
                builder.toStream(out);
                builder.run();
                return out.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Error generating PDF", e);
            }
        }
}
