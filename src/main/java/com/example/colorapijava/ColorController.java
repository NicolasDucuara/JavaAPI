package com.example.colorapijava;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ColorController {

    private final SaludoService saludoService;

    // Inyección de dependencias por constructor
    public ColorController(SaludoService saludoService) {
        this.saludoService = saludoService;
    }

    // GET / → página HTML inicial con color aleatorio
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("color", saludoService.generarColorAleatorio());
        return "color";
    }

    // GET /color → JSON con color aleatorio
    @GetMapping("/color")
    @ResponseBody
    public Map<String, String> getColor() {
        Map<String, String> response = new HashMap<>();
        response.put("color", saludoService.generarColorAleatorio());
        return response;
    }

    // POST /saludar → guarda el saludo en PostgreSQL y retorna respuesta
    @PostMapping("/saludar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saludar(@RequestBody NombreRequest request) {
        String nombre = request.getNombre();

        if (nombre == null || nombre.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("detail", "El nombre no puede estar vacío");
            return ResponseEntity.badRequest().body(error);
        }

        // Persistimos en PostgreSQL a través del servicio
        Saludo guardado = saludoService.registrarSaludo(nombre);

        Map<String, Object> response = new HashMap<>();
        response.put("id", guardado.getId());
        response.put("saludo", guardado.getMensaje());
        response.put("color", guardado.getColor());
        response.put("fechaHora", guardado.getFechaHora());
        return ResponseEntity.ok(response);
    }

    // GET /historial → Devuelve lista de todos los saludos guardados en la BD
    @GetMapping("/historial")
    @ResponseBody
    public ResponseEntity<List<Saludo>> getHistorial() {
        return ResponseEntity.ok(saludoService.obtenerHistorial());
    }
}