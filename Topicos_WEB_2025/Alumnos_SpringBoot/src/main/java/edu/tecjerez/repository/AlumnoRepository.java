/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.tecjerez.repository;

import edu.tecjerez.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author salvatore
 */
public interface AlumnoRepository extends JpaRepository<Alumno, Long>{
    
    @Query("SELECT a FROM Alumno a WHERE a.numControl = ?1 ")
    public Alumno findByNumControl(String numControl);
    
    @Query("SELECT a FROM Alumno a WHERE a.nombre = ?1 ")
    public Alumno findByNombre(String nombre);
}
