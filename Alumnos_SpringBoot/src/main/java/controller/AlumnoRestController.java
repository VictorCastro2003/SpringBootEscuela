/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package controller;


import com.fasterxml.jackson.databind.JsonNode;
import entity.Alumno;
import entity.AlumnoMaterias;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Collections;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import repository.AlumnoRepository;

/**
 *
 * @author salvatore
 */
@RestController
@RequestMapping("/alumnos")
public class AlumnoRestController {
    // ------------ Metodos CRUD --------------
    @Autowired
    AlumnoRepository alumnoRepository;
    
     private final WebClient.Builder webClientBuilder;
     
      public AlumnoRestController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
      
      
      //webClient requires HttpClient library to work propertly       
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain 
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
      
    
    @GetMapping()
    public List<Alumno> list() {
        return alumnoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Alumno get(@PathVariable(name = "id") long id) {
        return alumnoRepository.findById(id).get();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Alumno input) {
         Alumno find = alumnoRepository.findById(id).get();   
        if(find != null){     
            find.setNumControl(input.getNumControl());
            find.setNombre(input.getNombre());
            find.setEdad(input.getEdad());
        }
        Alumno save = alumnoRepository.save(find);
           return ResponseEntity.ok(save);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Alumno input) {
        input.getMaterias().forEach(x -> x.setAlumno(input));
        Alumno save = alumnoRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
          Optional<Alumno> findById = alumnoRepository.findById(id);   
        if(findById.get() != null){               
                  alumnoRepository.delete(findById.get());  
        }
        return ResponseEntity.ok().build();
    }
    
    
     @GetMapping("/alumnos_completo")
    public Alumno getByCode(@RequestParam(name = "nc") String nc) {
        
        Alumno alumno = alumnoRepository.findByNumControl(nc);
        
        List<AlumnoMaterias> products = alumno.getMaterias();
        products.forEach(x ->{
            String nombreMateria = getMateriaName(x.getId());
            x.setNombreMateria(nombreMateria);
        });
        return alumno;
       
    }
    
    
    
       
    private String getMateriaName(long id) { 
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8081/materias")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081/materias"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("nombreMateria").asText();
        return name;
    }
    
}