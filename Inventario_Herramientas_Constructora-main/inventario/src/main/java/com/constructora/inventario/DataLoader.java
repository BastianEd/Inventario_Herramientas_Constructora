package com.constructora.inventario;

import com.constructora.inventario.model.*;
import com.constructora.inventario.repository.*;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<String> tiposHerramientas = List.of(
            "Herramientas manuales", "Herramientas eléctricas", "Maquinaria pesada",
            "Equipos de seguridad", "Herramientas de medición"
    );

    private final List<String> marcasHerramientas = List.of(
            "Bosch", "Makita", "DeWalt", "Stanley", "Black & Decker", "Hilti", "Hitachi"
    );

    private final Map<String, List<String>> herramientasPorTipo = Map.of(
            "Herramientas manuales", List.of("Martillo", "Destornillador", "Llave inglesa", "Alicate", "Cinta métrica"),
            "Herramientas eléctricas", List.of("Taladro inalámbrico", "Sierra circular", "Lijadora", "Atornillador eléctrico"),
            "Maquinaria pesada", List.of("Retroexcavadora", "Compactadora", "Grúa", "Generador"),
            "Equipos de seguridad", List.of("Casco de seguridad", "Guantes", "Botas de seguridad", "Arnés"),
            "Herramientas de medición", List.of("Nivel láser", "Flexómetro", "Escuadra", "Calibrador")
    );

    private final List<String> cargosTrabajadores = List.of(
            "Albañil", "Electricista", "Carpintero", "Supervisor de obra", "Maquinista", "Soldador", "Ayudante"
    );

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        cargarCategorias();
        cargarHerramientas();
        cargarTrabajadores();
        generarPrestamos();
        sincronizarCantidadPrestadas();
        actualizarInventario();
    }

    private void cargarCategorias() {
        for (String tipo : tiposHerramientas) {
            CategoriaHerramienta categoria = new CategoriaHerramienta();
            categoria.setTipo(tipo);
            categoria.setMarca(marcasHerramientas.get(random.nextInt(marcasHerramientas.size())));
            categoria.setAnioAntiguedad(random.nextInt(10) + 1);
            categoriaRepository.save(categoria);
        }
    }

    private void cargarHerramientas() {
        List<CategoriaHerramienta> categorias = categoriaRepository.findAll();
        for (CategoriaHerramienta categoria : categorias) {
            List<String> nombresHerramientas = herramientasPorTipo.getOrDefault(categoria.getTipo(), List.of("Herramienta genérica"));
            int cantidadHerramientas = 4 + random.nextInt(4);
            for (int i = 0; i < cantidadHerramientas; i++) {
                Herramienta herramienta = new Herramienta();
                String nombre = nombresHerramientas.get(random.nextInt(nombresHerramientas.size()));
                herramienta.setNombre(nombre);

                int cantidadDisponible = faker.number().numberBetween(5, 50);
                int cantidadDanadas = faker.number().numberBetween(0, 5);
                // Inicialmente ponemos cantidadPrestadas a 0, se actualizará después
                herramienta.setCantidadDisponible(cantidadDisponible);
                herramienta.setCantidadDanadas(cantidadDanadas);
                herramienta.setCantidadPrestadas(0);
                herramienta.setCategoria(categoria);
                herramientaRepository.save(herramienta);
            }
        }
    }

    private void cargarTrabajadores() {
        for (int i = 0; i < 20; i++) {
            Trabajador trabajador = new Trabajador();
            trabajador.setNombre(faker.name().fullName());
            trabajador.setCargo(cargosTrabajadores.get(random.nextInt(cargosTrabajadores.size())));
            trabajadorRepository.save(trabajador);
        }
    }

    private void generarPrestamos() {
        List<Herramienta> herramientas = herramientaRepository.findAll();
        List<Trabajador> trabajadores = trabajadorRepository.findAll();

        for (int i = 0; i < 15; i++) {
            Herramienta herramienta = herramientas.get(random.nextInt(herramientas.size()));

            // Solo prestamos si hay disponibles
            if (herramienta.getCantidadDisponible() > 0) {
                Trabajador trabajador = trabajadores.get(random.nextInt(trabajadores.size()));
                Prestamo prestamo = new Prestamo();
                prestamo.setHerramienta(herramienta);
                prestamo.setTrabajador(trabajador);

                LocalDate fechaPrestamo = LocalDate.now().minusDays(random.nextInt(30));
                prestamo.setFechaPrestamo(fechaAString(fechaPrestamo));

                // 50% probabilidad de devolución
                if (random.nextBoolean()) {
                    LocalDate fechaDevolucion = fechaPrestamo.plusDays(random.nextInt((int)(LocalDate.now().toEpochDay() - fechaPrestamo.toEpochDay()) + 1));
                    prestamo.setFechaDevolucion(fechaAString(fechaDevolucion));
                }

                prestamoRepository.save(prestamo);
            }
        }
    }

    @Transactional
    protected void sincronizarCantidadPrestadas() {
        List<Herramienta> herramientas = herramientaRepository.findAll();

        for (Herramienta herramienta : herramientas) {
            long prestamosActivos = prestamoRepository.countByHerramientaAndFechaDevolucionIsNull(herramienta);
            herramienta.setCantidadPrestadas((int) prestamosActivos);

            // Ajustamos cantidadDisponible para que la suma total (disponible + prestadas + dañadas) se mantenga igual
            int stockTotal = herramienta.getCantidadDisponible() + herramienta.getCantidadPrestadas() + herramienta.getCantidadDanadas();
            herramienta.setCantidadDisponible(stockTotal - herramienta.getCantidadPrestadas() - herramienta.getCantidadDanadas());

            herramientaRepository.save(herramienta);
        }
    }

    private void actualizarInventario() {
        List<Herramienta> herramientas = herramientaRepository.findAll();

        int stockTotal = herramientas.stream()
                .mapToInt(h -> h.getCantidadDisponible() + h.getCantidadPrestadas() + h.getCantidadDanadas())
                .sum();

        int prestadasTotal = herramientas.stream().mapToInt(Herramienta::getCantidadPrestadas).sum();
        int daniadasTotal = herramientas.stream().mapToInt(Herramienta::getCantidadDanadas).sum();

        Inventario inventario = new Inventario();
        inventario.setStock(stockTotal);
        inventario.setPrestadas(prestadasTotal);
        inventario.setDaniadas(daniadasTotal);
        inventarioRepository.save(inventario);
    }

    private String fechaAString(LocalDate fecha) {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
