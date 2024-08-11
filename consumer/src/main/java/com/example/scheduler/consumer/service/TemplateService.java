package com.example.scheduler.consumer.service;

import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class TemplateService {

    private Logger logger = LoggerFactory.getLogger(TemplateService.class);

    @Autowired
    ResourceLoader resourceLoader;


    /**
     * Generate file and save it to provided output file path as a pdf document.
     *
     * @param templateFile   the template file
     * @param outputFilePath the output file path
     * @param data           the data
     * @param metadata       the metadata
     * @throws IOException         the io exception
     * @throws XDocReportException the x doc report exception
     */
    public void generateFile(String templateFile, String outputFilePath, Map<String, Object> data, FieldsMetadata metadata) throws IOException, XDocReportException {
        Resource resource = resourceLoader.getResource(templateFile);
        if(!resource.exists()){
            logger.error("Template file does not exist :"+templateFile);
            return;
        }
        try(InputStream templateInputStream = resource.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)){
            // Load ODT template
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateInputStream, null, TemplateEngineKind.Freemarker, false);
            report.setFieldsMetadata(metadata);

            // Create context and populate with data
            IContext context = report.createContext();
            context.putMap(data);

            //Generate pdf
            PdfOptions pdfOptions = PdfOptions.getDefault();
            pdfOptions.setConfiguration(pdfWriter -> {
                try {
                    pdfWriter.setEncryption(null,null,0,PdfWriter.ENCRYPTION_AES_128);
                } catch (DocumentException e) {
                    logger.error("Error while setting pdf writer : "+ e);
                    throw new RuntimeException(e);
                }
            });
            Options options = Options.getTo(ConverterTypeTo.PDF);
            options.subOptions(pdfOptions);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            report.convert(context, options.subOptions(pdfOptions), outputStream);

            //save pdf file
            fileOutputStream.write(outputStream.toByteArray());

            logger.info("File generated : "+outputFilePath);
        }
    }
}
