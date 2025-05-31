/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.tecjerez.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author salvatore
 */

/*Materia
	id
	CveMateria
	Nombre
	Creditos
	Carrera
	Especialidad
	Semestre */

@Data //codigo automatizado de LOMBOK para Getters y Setters
@Entity
public class Materia {
    
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private long id;
    private String cveMateria;
    private String nombreMateria;
    private byte creditos;
    
}
