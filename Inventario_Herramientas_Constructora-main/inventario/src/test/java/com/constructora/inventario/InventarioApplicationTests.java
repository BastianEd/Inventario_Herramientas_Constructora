package com.constructora.inventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventarioApplicationTests {
	@Test
	void contextLoads() {
		// Este test simplemente verifica que el contexto de Spring se carga correctamente
	}

	@Test
	public void main_WhenApplicationStarts_ShouldRunSuccessfully() {
		// Test para verificar que el método main inicia la aplicación sin errores
		InventarioApplication.main(new String[]{});
	}
}
