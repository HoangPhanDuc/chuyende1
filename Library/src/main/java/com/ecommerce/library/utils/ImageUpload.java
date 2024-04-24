package com.ecommerce.library.utils;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER ="D:\\JavaProject\\ChuyenDe1_Java\\Admin\\src\\main\\resources\\static\\img\\imageProduct";

    public boolean uploadImage(MultipartFile imageProduct) {
        boolean isUpload = false;
        try {
            Files.copy(imageProduct.getInputStream(), Paths.get(UPLOAD_FOLDER + File.separator,
                            imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isUpload;
    };

    public boolean checkExisted(MultipartFile imageProduct) {
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imageProduct.getOriginalFilename());
            isExisted = file.exists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isExisted;
    }
}