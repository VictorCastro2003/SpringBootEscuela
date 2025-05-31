package edu.tecjerez.controller;

import edu.tecjerez.entity.Alumno;
import edu.tecjerez.entity.AlumnoMaterias;
import edu.tecjerez.entity.AlumnoActividades;
import edu.tecjerez.repository.AlumnoRepository;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@RestController
@RequestMapping("/alumnos")
public class AlumnoRestController {

    @Autowired
    AlumnoRepository alumnoRepository;

    private final WebClient.Builder webClientBuilder;

    public AlumnoRestController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    @GetMapping()
    public List<Alumno> list() {
        List<Alumno> alumnos = alumnoRepository.findAll();

        for (Alumno alumno : alumnos) {
            for (AlumnoMaterias materia : alumno.getMaterias()) {
                materia.setNombreMateria(getMateriaName(materia.getId()));
                materia.setCveMateria(getMateriaClave(materia.getId()));
            }
            for (AlumnoActividades actividad : alumno.getActividades()) {
                actividad.setNombreActividad(getActividadName(actividad.getId()));
                actividad.setCveActividad(getActividadClave(actividad.getId()));
            }
        }

        return alumnos;
    }

    @GetMapping("/{id}")
    public Alumno get(@PathVariable(name = "id") long id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Alumno input) {
        Optional<Alumno> optionalAlumno = alumnoRepository.findById(id);
        if (optionalAlumno.isPresent()) {
            Alumno find = optionalAlumno.get();
            find.setNumControl(input.getNumControl());
            find.setNombre(input.getNombre());
            find.setEdad(input.getEdad());
            Alumno save = alumnoRepository.save(find);
            return ResponseEntity.ok(save);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody AlumnoDTO input) {
        Alumno nuevo = new Alumno();
        nuevo.setNumControl(input.numControl);
        nuevo.setNombre(input.nombre);
        nuevo.setEdad((byte) input.edad);

        // Materias
        List<AlumnoMaterias> listaMaterias = new ArrayList<>();
        Set<Long> materiasUnicas = new HashSet<>();
        for (Long idMateria : input.materias) {
            if (materiasUnicas.add(idMateria)) {
                AlumnoMaterias am = new AlumnoMaterias();
                am.setId(idMateria);
                am.setAlumno(nuevo);
                listaMaterias.add(am);
            }
        }
        nuevo.setMaterias(listaMaterias);

        // Actividades
        List<AlumnoActividades> listaActividades = new ArrayList<>();
        Set<Long> actividadesUnicas = new HashSet<>();
        for (String idActividadStr : input.actividades) {
            Long idActividad = Long.parseLong(idActividadStr);
            if (actividadesUnicas.add(idActividad)) {
                AlumnoActividades aa = new AlumnoActividades();
                aa.setId(idActividad);
                aa.setAlumno(nuevo);
                listaActividades.add(aa);
            }
        }
        nuevo.setActividades(listaActividades);

        Alumno save = alumnoRepository.save(nuevo);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/alumnos_completo")
    public Alumno getByCode(@RequestParam(name = "nc") String nc) {
        Alumno alumno = alumnoRepository.findByNumControl(nc);

        for (AlumnoMaterias m : alumno.getMaterias()) {
            m.setNombreMateria(getMateriaName(m.getId()));
            m.setCveMateria(getMateriaClave(m.getId()));
        }

        for (AlumnoActividades a : alumno.getActividades()) {
            a.setNombreActividad(getActividadName(a.getId()));
            a.setCveActividad(getActividadClave(a.getId()));
        }

        return alumno;
    }

    // Métodos para WebClient → materias
    private String getMateriaName(long id) {
        JsonNode block = getMateriaJson(id);
        return block != null ? block.get("nombreMateria").asText() : "Desconocida";
    }

    private String getMateriaClave(long id) {
        JsonNode block = getMateriaJson(id);
        return block != null ? block.get("cveMateria").asText() : "SIN-CVE";
    }

    private JsonNode getMateriaJson(long id) {
        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8082/materias")
                .build()
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    // Métodos para WebClient → actividades
    private String getActividadName(long id) {
        JsonNode block = getActividadJson(id);
        return block != null ? block.get("nombreActividad").asText() : "Desconocida";
    }

    private String getActividadClave(long id) {
        JsonNode block = getActividadJson(id);
        return block != null ? block.get("cveActividad").asText() : "SIN-CVE";
    }

    private JsonNode getActividadJson(long id) {
        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8083/actividades")
                .build()
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}

// DTO para crear alumno
class AlumnoDTO {
    public String numControl;
    public String nombre;
    public int edad;
    public List<Long> materias;
    public List<String> actividades; // se esperan IDs como string
}
