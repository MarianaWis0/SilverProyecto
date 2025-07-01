package com.silver.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.silver.demo.dto.RespuestaDto;
import com.silver.demo.dto.TemaDto;
import com.silver.demo.model.Respuesta;
import com.silver.demo.model.Tema;
import com.silver.demo.model.Usuario;
import com.silver.demo.repository.RespuestaRepository;
import com.silver.demo.repository.TemaRepository;
import com.silver.demo.security.UserDetailsImp;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/foro")
@RequiredArgsConstructor
public class TemaController {

    private final TemaRepository temaRepository;
    private final RespuestaRepository respuestaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/crear")
    public ResponseEntity<TemaDto> crearTema(@RequestBody TemaDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = ((UserDetailsImp) auth.getPrincipal()).getUsuario();

        Tema tema = new Tema();
        tema.setTitulo(dto.getTitulo());
        tema.setContenido(dto.getContenido());
        tema.setFechaCreacion(LocalDateTime.now());
        tema.setAutor(usuario);

        Tema guardado = temaRepository.save(tema);

        TemaDto salida = new TemaDto();
        salida.setId(guardado.getId());
        salida.setTitulo(guardado.getTitulo());
        salida.setContenido(guardado.getContenido());
        salida.setFechaCreacion(guardado.getFechaCreacion());
        salida.setAutorId(usuario.getId());
        salida.setAutorUsername(usuario.getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(salida);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@PostMapping("/addRespuestas/{temaId}")
	public ResponseEntity<RespuestaDto> agregarRespuesta(@PathVariable Long temaId, @RequestBody RespuestaDto dto) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Usuario usuario = ((UserDetailsImp) auth.getPrincipal()).getUsuario();

	    Tema tema = temaRepository.findById(temaId).orElseThrow();

	    Respuesta respuesta = new Respuesta();
	    respuesta.setContenido(dto.getContenido());
	    respuesta.setFecha(LocalDateTime.now());
	    respuesta.setAutor(usuario);
	    respuesta.setTema(tema);

	    Respuesta guardada = respuestaRepository.save(respuesta);

	   
	    RespuestaDto vista = new RespuestaDto();
	    vista.setId(guardada.getId());
	    vista.setContenido(guardada.getContenido());
	    vista.setFecha(guardada.getFecha());
	    vista.setAutorId(usuario.getId());
	    vista.setAutorUsername(usuario.getNombre());
	    vista.setTemaId(temaId);

	    messagingTemplate.convertAndSend("/topic/respuestas", vista); 

	    return ResponseEntity.ok(vista);
	}

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("/lista")
	public ResponseEntity<List<TemaDto>> listarTemas() {
	    List<Tema> temas = temaRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));

	    if (temas.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    List<TemaDto> resultado = temas.stream().map(tema -> {
	        TemaDto dto = new TemaDto();
	        dto.setId(tema.getId());
	        dto.setTitulo(tema.getTitulo());
	        dto.setContenido(tema.getContenido());
	        dto.setFechaCreacion(tema.getFechaCreacion());

	        Usuario autor = tema.getAutor();
	        dto.setAutorId(autor.getId());
	        dto.setAutorUsername(autor.getNombre());

	        List<RespuestaDto> respuestas = tema.getRespuestas().stream().map(resp -> {
	            RespuestaDto rDto = new RespuestaDto();
	            rDto.setId(resp.getId());
	            rDto.setContenido(resp.getContenido());
	            rDto.setFecha(resp.getFecha());
	            rDto.setAutorId(resp.getAutor().getId());
	            rDto.setAutorUsername(resp.getAutor().getNombre());
	            return rDto;
	        }).toList();

	        dto.setRespuestas(respuestas);

	        return dto;
	    }).toList();

	    return ResponseEntity.ok(resultado); 
	}


    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("/temas/{id}")
	public ResponseEntity<TemaDto> verTema(@PathVariable Long id) {
	    return temaRepository.findById(id).map(tema -> {
	        TemaDto dto = new TemaDto();
	        dto.setId(tema.getId());
	        dto.setTitulo(tema.getTitulo());
	        dto.setContenido(tema.getContenido());
	        dto.setFechaCreacion(tema.getFechaCreacion());

	        Usuario autor = tema.getAutor();
	        dto.setAutorId(autor.getId());
	        dto.setAutorUsername(autor.getNombre());

	        List<RespuestaDto> respuestas = tema.getRespuestas().stream().map(resp -> {
	            RespuestaDto rDto = new RespuestaDto();
	            rDto.setId(resp.getId());
	            rDto.setContenido(resp.getContenido());
	            rDto.setFecha(resp.getFecha());
	            rDto.setAutorId(resp.getAutor().getId());
	            rDto.setAutorUsername(resp.getAutor().getNombre());
	            return rDto;
	        }).toList();

	        dto.setRespuestas(respuestas);

	        return ResponseEntity.ok(dto);
	    }).orElse(ResponseEntity.notFound().build());
	}

}
