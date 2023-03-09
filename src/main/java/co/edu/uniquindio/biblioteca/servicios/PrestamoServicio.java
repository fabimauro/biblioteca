package co.edu.uniquindio.biblioteca.servicios;

import co.edu.uniquindio.biblioteca.dto.*;
import co.edu.uniquindio.biblioteca.entity.Cliente;
import co.edu.uniquindio.biblioteca.entity.Libro;
import co.edu.uniquindio.biblioteca.entity.Prestamo;
import co.edu.uniquindio.biblioteca.repo.ClienteRepo;
import co.edu.uniquindio.biblioteca.repo.LibroRepo;
import co.edu.uniquindio.biblioteca.repo.PrestamoRepo;
import co.edu.uniquindio.biblioteca.servicios.excepciones.ClienteNoEncontradoException;
import co.edu.uniquindio.biblioteca.servicios.excepciones.LibroNoEncontradoException;
import co.edu.uniquindio.biblioteca.servicios.excepciones.PrestamoNoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrestamoServicio {

    private final PrestamoRepo prestamoRepo;
    private final ClienteRepo clienteRepo;
    private final LibroRepo libroRepo;


    public long save(PrestamoDTO prestamoDTO){

        long codigoCliente = prestamoDTO.clienteID();
        Optional<Cliente> consulta = clienteRepo.findById(codigoCliente);

        if(consulta.isEmpty()){
            throw new ClienteNoEncontradoException("No existe");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setCliente(consulta.get());
        prestamo.setFechaPrestamo(LocalDateTime.now());
        List<Libro> buscados = getLibros(prestamoDTO.isbnLibros());
        prestamo.setLibros(buscados);
        prestamo.setFechaDevolucion(prestamoDTO.fechaDevolucion());
        Prestamo guardado = prestamoRepo.save(prestamo);
        return guardado.getCodigo();
    }

    private List<Libro> getLibros(List<String> codigosLibros){
        List<Libro> buscados = libroRepo.findAllById(codigosLibros);
        if(buscados.size() != codigosLibros.size()) {
            throw new LibroNoEncontradoException("No hay codigos asociados a ningun libro");
        }
        return buscados;
    }

    //RETORNAR LISTA DE PRESTAMOS
    public List<PrestamoDTO> findAll(){
       return prestamoRepo.findAll().stream().map(this::convertir).toList();
    }

    //PRESTAMO DADO SU CODIGO
    public PrestamoDTO findById(long codigoPrestamo){
        Prestamo prestamo = prestamoRepo.findById(codigoPrestamo).orElseThrow(()-> new PrestamoNoEncontradoException("No exixte el codigo del Prestamo"));
        return convertir(prestamo);
    }


    //TODO Completar
    //RETORNA LA LISTA DE PRESTAMOS  DE UN CLIENTES DANDO SU CODIGO
    public List<PrestamoDTO> findByCodigoCliente(long codigoCliente){
     clienteRepo.findById(codigoCliente).orElseThrow(()-> new RuntimeException("No existe el codigo del cliente"));
     return  prestamoRepo.findByClienteCodigo(codigoCliente).stream().map(this::convertir).toList();
    }

    //Convertir PrestamoDTO
    private PrestamoDTO convertir(Prestamo prestamo){
        return new PrestamoDTO(
                prestamo.getCliente().getCodigo(),
                prestamo.getLibros().stream().map(Libro::getIsbn).toList(),
                prestamo.getFechaDevolucion()
        );
    }


    //RETORNAR LA LISTA DE PRESTAMOS REALIZADOS EN UNA FEHCA DADA
    public List<PrestamoDTO> getPrestamosByFechaPrestamo(LocalDateTime fecha) {
        LocalDateTime fechaInicio = fecha.with(LocalTime.MIN);
        LocalDateTime fechaFin = fecha.with(LocalTime.MAX);
        return prestamoRepo.findByFechaPrestamoBetween(fechaInicio, fechaFin).stream().map(this::convertir).toList();
    }

    //RETORNE LA CANTIDAD DE PRESTAMOS QUE HA TENIDO UN LIBRO DADO SU ISBN
    public int getCountPrestamosByLibroIsbn(String isbn) {
        return prestamoRepo.countByLibroIsbn(isbn);
    }

    public void eliminarPrestamoPorCodigo(long codigo) {
        prestamoRepo.deleteByCodigo(codigo);
    }


    //actualizar prestamo
    public long update(long codigoPrestamo, PrestamoDTO prestamoDTO){
        Prestamo prestamo = prestamoRepo.findById(codigoPrestamo).orElseThrow(()-> new PrestamoNoEncontradoException("codigo de prestamo no encontrado"));

        List<Libro> buscados = getLibros(prestamoDTO.isbnLibros());
        prestamo.setLibros(buscados);
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaDevolucion(prestamoDTO.fechaDevolucion());
        return prestamoRepo.save(prestamo).getCodigo();
    }

    //cambiar estado del prestamo
    public long delete(long codigoPrestamo){
        Prestamo prestamo = prestamoRepo.findById(codigoPrestamo).orElseThrow(()-> new PrestamoNoEncontradoException("codigo de prestamo no encontrado"));
        prestamo.setActivo(false);
        return prestamoRepo.save(prestamo).getCodigo();
    }

}
