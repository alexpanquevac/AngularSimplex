/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.distrital.demo;

import com.distrital.modelo.Ecuacion;
import com.distrital.service.GraficaService;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ALEXANDER
 */
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/metodoGrafico/")
public class GraficaAPI {

    @Autowired
    GraficaService servicioGrafica;

    @PostMapping("grafica")
    public ResponseEntity<List<Ecuacion>> graficar(@RequestBody List<Ecuacion> ecuaciones) {
        List<Ecuacion> ecuacion = servicioGrafica.graficar(ecuaciones);
        return new ResponseEntity<List<Ecuacion>>(ecuacion, HttpStatus.OK);
    }

    @GetMapping("obtenerImagen")
    public ResponseEntity<List<String>> obtenerImagen() {
        List<String> imagenes = new ArrayList<>();
        String imagen = "";
        String filesPath = "D:\\Desarrollo\\SimplexService\\src\\webapp\\images";
        File fileFolder = new File(filesPath);
        File file = new File("D:\\Desarrollo\\SimplexAngular\\SimplexService\\src\\webapp\\images\\image.png");
//        if(fileFolder!=null){
//            for(final File file : fileFolder.listFiles()){
        if (!file.isDirectory()) {
            String encodeBase64 = null;
            try {
                String extension = FilenameUtils.getExtension(file.getName());
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                fileInputStream.read(bytes);
                encodeBase64 = Base64.getEncoder().encodeToString(bytes);
                imagen = imagen + "data:image/" + extension + ";base64," + encodeBase64;
                fileInputStream.close();
                imagenes.add(imagen);

            } catch (Exception e) {
            }
//                }
//            }
        }
        return new ResponseEntity<List<String>>(imagenes, HttpStatus.OK);
    }

}
