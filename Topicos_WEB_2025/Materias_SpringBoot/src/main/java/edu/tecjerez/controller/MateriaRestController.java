/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package edu.tecjerez.controller;

import edu.tecjerez.entity.Materia;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import edu.tecjerez.repository.MateriaRepository;

/**
 *
 * @author salvatore
 */
@RestController
@RequestMapping("/materias")
public class MateriaRestController {
    
    @Autowired
    MateriaRepository materiaRepository;
    
    @GetMapping()
    public List<Materia> list() {
        return materiaRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        Optional<Materia> materia = materiaRepository.findById(id);
        if(materia.isPresent())
            return  new ResponseEntity<>(materia.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Materia input) {
        Optional<Materia> materia = materiaRepository.findById(id);
        if(materia.isPresent()){
            Materia nuevaMateria = materia.get();
            nuevaMateria.setNombreMateria(input.getNombreMateria());
            nuevaMateria.setCreditos(input.getCreditos());
            Materia m = materiaRepository.save(nuevaMateria);
            return  new ResponseEntity<>(materia.get(), HttpStatus.OK); 
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Materia input) {
        Materia materia = materiaRepository.save(input);
        return ResponseEntity.ok(materia);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        materiaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
