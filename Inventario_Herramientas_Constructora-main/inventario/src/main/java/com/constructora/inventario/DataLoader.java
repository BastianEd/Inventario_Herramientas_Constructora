package com.constructora.inventario;

import com.constructora.inventario.model.*;
import com.constructora.inventario.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

//@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaHerramientaRepository categoriaRepository;

    @Autowired
    private HerramientaRepository herramientaRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        cargarCategorias();
        cargarHerramientas();
        cargarTrabajadores();
        actualizarInventario();
        generarPrestamos();
    }

    private void cargarCategorias() {
        for (int i = 0; i < 5; i++) {
            CategoriaHerramienta categoria = new CategoriaHerramienta();
            categoria.setTipo(faker.commerce().department());
            categoria.setMarca(faker.company().name());
            categoria.setAnioAntiguedad(random.nextInt(10) + 1);
            categoriaRepository.save(categoria);
        }
    }

    private void cargarHerramientas() {
        List<CategoriaHerramienta> categorias = categoriaRepository.findAll();
        for (int i = 0; i < 30; i++) {
            CategoriaHerramienta categoria = categorias.get(random.nextInt(categorias.size()));
            Herramienta herramienta = new Herramienta();
            herramienta.setNombre(faker.commerce().productName());
            herramienta.setCantidadDisponible(faker.number().numberBetween(1, 100));
            herramienta.setCantidadDanadas(faker.number().numberBetween(0, 5));
            herramienta.setCategoria(categoria);
            herramientaRepository.save(herramienta);
        }
    }

    private void cargarTrabajadores() {
        for (int i = 0; i < 20; i++) {
            Trabajador trabajador = new Trabajador();
            trabajador.setNombre(faker.name().fullName());
            trabajador.setCargo(faker.job().title());
            trabajadorRepository.save(trabajador);
        }
    }

    private void actualizarInventario() {
        List<Herramienta> herramientas = herramientaRepository.findAll();
        int stockTotal = herramientas.stream().mapToInt(Herramienta::getCantidadDisponible).sum();
        int daniadasTotal = herramientas.stream().mapToInt(Herramienta::getCantidadDanadas).sum();

        Inventario inventario = new Inventario();
        inventario.setStock(stockTotal);
        inventario.setDaniadas(daniadasTotal);
        inventario.setPrestadas(0);
        inventarioRepository.save(inventario);
    }

    private void generarPrestamos() {
        List<Herramienta> herramientas = herramientaRepository.findAll();
        List<Trabajador> trabajadores = trabajadorRepository.findAll();
        for (int i = 0; i < 10; i++) {
            Herramienta herramienta = herramientas.get(random.nextInt(herramientas.size()));
            if (herramienta.getCantidadDisponible() > 0) {
                Trabajador trabajador = trabajadores.get(random.nextInt(trabajadores.size()));
                Prestamo prestamo = new Prestamo();
                prestamo.setHerramienta(herramienta);
                prestamo.setTrabajador(trabajador);

                // Generar una fecha de préstamo aleatoria en el pasado
                LocalDate fechaPrestamo = LocalDate.now().minusDays(random.nextInt(30));
                prestamo.setFechaPrestamo(fechaPrestamo.toString());

                prestamoRepository.save(prestamo);
                herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() - 1);
                herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() + 1);
                herramientaRepository.save(herramienta);
            }
        }
    }
}
