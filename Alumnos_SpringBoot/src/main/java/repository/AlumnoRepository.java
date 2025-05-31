/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.Alumno;
import org.springframework.data.jpa.repository.Query;
/**
 *
 * @author victo
 */
public interface AlumnoRepository extends JpaRepository<Alumno, Long>{
    
    @Query("SELECT numControl FROM Alumno WHERE = numControl =?1")
    public Alumno findByNumControl(String numControl);
    
    @Query("SELECT nombre FROM Alumno WHERE = nombre =?1")
    public Alumno findByNombre(String nombre);
}
