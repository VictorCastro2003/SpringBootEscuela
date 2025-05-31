/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Data;
/**
 *
 * @author victo
 */
@Data
@Entity
public class Alumno {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private String numControl;
    private String nombre;
    private byte edad;
    
    @OneToMany(fetch=FetchType.LAZY,mappedBy="alumno",cascade=CascadeType.ALL,orphanRemoval=true)
    private List<AlumnoMaterias> materias;
    
    private List<?>actividades;
    
}
