package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;

@RestController
public class FileController {

    @SneakyThrows
    @GetMapping("/uploads/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse response){
        File f = new File("uploads" + File.separator + filename);
        if (!f.exists()){
            throw new NotFoundException("File not found!");
        }
        Files.copy(f.toPath(),response.getOutputStream());
    }
}
