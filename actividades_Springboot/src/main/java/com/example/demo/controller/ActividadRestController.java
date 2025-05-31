package com.example.demo.controller;

import com.example.demo.entity.Actividad;
import com.example.demo.repository.ActividadRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/actividades")
public class ActividadRestController {

    @Autowired
    private ActividadRepository actividadRepository;

    @GetMapping()
    public List<Actividad> list() {
        return actividadRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        Optional<Actividad> actividad = actividadRepository.findById(id);
        if (actividad.isPresent()) {
            return new ResponseEntity<>(actividad.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Actividad input) {
        Actividad nuevaActividad = actividadRepository.save(input);
        return ResponseEntity.ok(nuevaActividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable long id, @RequestBody Actividad input) {
        Optional<Actividad> actividad = actividadRepository.findById(id);
        if (actividad.isPresent()) {
            Actividad act = actividad.get();
            act.setCveActividad(input.getCveActividad());
            act.setNombreActividad(input.getNombreActividad());
            actividadRepository.save(act);
            return new ResponseEntity<>(act, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        actividadRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
