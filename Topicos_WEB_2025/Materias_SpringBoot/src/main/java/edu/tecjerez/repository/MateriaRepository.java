/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.tecjerez.repository;

import edu.tecjerez.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author salvatore
 */
public interface MateriaRepository extends JpaRepository<Materia, Long>{
    
}
