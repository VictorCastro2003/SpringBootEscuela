/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.tecjerez.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

/**
 *
 * @author salvatore
 */
@Data
@Entity
public class AlumnoMaterias {
    
    @Id
    
    private Long id;
    
    private String cveMateria;  // Clave de la materia (ej: "MAT-101")
    
    @Transient  // Campo no persistido (se calcula dinámicamente)
    private String nombreMateria;  // Ej: "Cálculo Diferencial"
    
    @JsonIgnore  // Evita serialización recursiva
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")  // Nombre de columna en la tabla AlumnoMaterias
    private Alumno alumno;
}