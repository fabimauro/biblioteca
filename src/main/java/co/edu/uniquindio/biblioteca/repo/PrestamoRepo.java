package co.edu.uniquindio.biblioteca.repo;

import co.edu.uniquindio.biblioteca.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrestamoRepo extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByClienteCodigo(long codigoCliente);

    @Query("SELECT p FROM Prestamo p WHERE p.fechaPrestamo >= :fechaInicio AND p.fechaPrestamo <= :fechaFin")
    List<Prestamo> findByFechaPrestamoBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(p) FROM Prestamo p JOIN p.libros l WHERE l.isbn = :isbn")
    int countByLibroIsbn(@Param("isbn") String isbn);

    ///query se usa para sctulizar  si esta activo o inactvo
   // la consulta personalizada que establece el valor del
    // campo activo en false para el registro de préstamo con el código especificado.
    // Luego, en el servicio de la aplicación, se podría llamar a este método en lugar del método
    // deleteById heredado de JpaRepository para marcar el registro como inactivo en lugar
    // de eliminarlo físicamente.
    @Modifying
    @Query("UPDATE Prestamo p SET p.activo = false WHERE p.codigo = :codigo")
    void deleteByCodigo(@Param("codigo") long codigo);
}
