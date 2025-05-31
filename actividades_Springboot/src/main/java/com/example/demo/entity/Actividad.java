package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author victo
 */


@Data //codigo automatizado de LOMBOK para Getters y Setters
@Entity
public class Actividad {
    
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private long id;
    private String cveActividad;
    private String nombreActividad;
    
}
