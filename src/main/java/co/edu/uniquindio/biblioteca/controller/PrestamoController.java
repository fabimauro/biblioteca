package co.edu.uniquindio.biblioteca.controller;

import co.edu.uniquindio.biblioteca.dto.PrestamoDTO;
import co.edu.uniquindio.biblioteca.dto.Respuesta;
import co.edu.uniquindio.biblioteca.entity.Prestamo;
import co.edu.uniquindio.biblioteca.servicios.LibroServicio;
import co.edu.uniquindio.biblioteca.servicios.PrestamoServicio;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prestamo")
@AllArgsConstructor
public class PrestamoController {

    private final PrestamoServicio prestamoServicio;


    @PostMapping
    public ResponseEntity<?> save(@RequestBody PrestamoDTO prestamoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Respuesta<>("Prestamo  creado correctamente", prestamoServicio.save(prestamoDTO)));
    }

    @GetMapping
    public ResponseEntity<Respuesta<List<PrestamoDTO>>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("", prestamoServicio.findAll()));
    }

    @GetMapping("/{codigoPrestamo}")
    public ResponseEntity<Respuesta<PrestamoDTO>> findById(@PathVariable long codigoPrestamo) {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("", prestamoServicio.findById(codigoPrestamo)));
    }

    @GetMapping("/cliente/{codigoCliente}")
    public ResponseEntity<Respuesta<List<PrestamoDTO>>> findByCodigoCliente(@PathVariable long codigoCliente) {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("", prestamoServicio.findByCodigoCliente(codigoCliente)));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Respuesta<List<PrestamoDTO>>> getPrestamosByFechaPrestamo(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("",  prestamoServicio.getPrestamosByFechaPrestamo(fecha)));

    }
    @GetMapping("/libro/{isbn}/count")
    public ResponseEntity<?> getCountPrestamosByLibroIsbn(@PathVariable String isbn) {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("",  prestamoServicio.getCountPrestamosByLibroIsbn(isbn)));
    }

    @PutMapping("/{codigoPrestamo}")
    public ResponseEntity<?> update(@PathVariable long codigoPrestamo, @RequestBody PrestamoDTO prestamoDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("",  prestamoServicio.update(codigoPrestamo,prestamoDTO) ));
    }


    @DeleteMapping("/{codigoPrestamo}")
        public ResponseEntity<?> delete( @PathVariable long codigoPrestamo){
            return ResponseEntity.status(HttpStatus.OK).body(new Respuesta<>("", prestamoServicio.delete(codigoPrestamo)));

        }



}