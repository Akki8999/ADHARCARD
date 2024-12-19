package com.example.ReadAadhar.Service;

import org.apache.pdfbox.pdmodel.PDDocument;


import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@Service
public class AdharUploadService {
    public void uploadAdharCard(MultipartFile file, String password) throws Exception {
        boolean isNameFound = false;
        boolean isAddressFound = false;

        boolean isEmailFound = false;
        String name = "";
        String addharCardNo = "";
StringBuilder  addressBuilder = new StringBuilder();
        String adhardata = loadAdharPdf(file.getInputStream(), password);

        StringTokenizer tokenizer = new StringTokenizer(adhardata, "\r\n");
        int count = 0;

        while (tokenizer.hasMoreTokens()) {
            String inputLine = tokenizer.nextToken().trim();
            if (inputLine.contains("C/O")&&containsOnlyEnglish(inputLine)) {
                addressBuilder.append(inputLine);
                isAddressFound=true;
            }

            else if (inputLine.contains("VID")&&containsOnlyEnglish(inputLine)) {
                addharCardNo=inputLine;
                isAddressFound = false;
            }

            else if (isAddressFound) {
                addressBuilder.append(inputLine);

            }


            else if (count==3){
                isNameFound = true;
                name = inputLine;
            }


            count++;
        }
        System.err.println("NAme as per adharcard-"+name);
        System.err.println("address-"+addressBuilder);

    }

    private String extractAddress(String inputLine) {
        return inputLine.replaceAll("[^a-zA-Z0-9, ]", "").trim();
    }

    private String extractName(String inputLine) {
        return inputLine.replaceAll("[^a-zA-Z ]", "").trim();

    }

    private String loadAdharPdf(InputStream inputStream, String pan) throws Exception {
        String pdfdata = "";
        try (PDDocument document = PDDocument.load(inputStream, pan)) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setSortByPosition(true);
            pdfTextStripper.setStartPage(1);
            pdfTextStripper.setEndPage(document.getNumberOfPages());
            pdfdata = pdfTextStripper.getText(document);
        } catch (IOException e) {
            System.err.println("Error loading document: " + e.getMessage());
            throw new Exception("Invalid PDF password kindly check and  try again");
        }
        return pdfdata;
    }

    private String[] parseInputLine(String inputLine) {
        try {
            String trimmedLine = inputLine.trim();

            return trimmedLine.trim().split("\\s+");

        } catch (Exception e) {
            // Handle any exceptions that might occur during parsing
            System.err.println("Error parsing input line: " + e.getMessage());
            return new String[0]; // Return an empty array in case of an error
        }
    }



    public static boolean containsOnlyEnglish(String input) {
        // Regular expression to match only English alphabets (A-Z, a-z) and spaces.
        String hindiRegex = "[\\u0900-\\u097F]";

        // If the input string contains any Hindi characters, return false
        return !Pattern.compile(hindiRegex).matcher(input).find();
    }
}
