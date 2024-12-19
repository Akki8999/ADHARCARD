package com.example.ReadAadhar.Controller;

import com.example.ReadAadhar.Service.AdharUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AdharReadController {
    @Autowired
    AdharUploadService adharUploadService;

    @PostMapping(path = "/uploadAdharcard", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})

    public String uploadAdharcard(@RequestParam("file") MultipartFile file, @RequestParam(value = "password", required = false, defaultValue = "") String password) throws Exception {
        adharUploadService.uploadAdharCard(file, password);


        return password;


    }


}
