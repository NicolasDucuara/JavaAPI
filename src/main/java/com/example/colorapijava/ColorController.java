package com.example.colorapijava;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class ColorController {

    private final Random random = new Random();

    private String generarColor() {
        return String.format("#%06x", random.nextInt(0xFFFFFF + 1));
    }

    // GET / → página HTML
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("color", generarColor());
        return "color";
    }

    // GET /color → JSON con color
    @GetMapping("/color")
    @ResponseBody
    public Map<String, String> getColor() {
        Map<String, String> response = new HashMap<>();
        response.put("color", generarColor());
        return response;
    }

    // POST /saludar → recibe JSON y devuelve JSON
    @PostMapping("/saludar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saludar(@RequestBody NombreRequest request) {
        String nombre = request.getNombre();

        if (nombre == null || nombre.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("detail", "El nombre no puede estar vacío");
            return ResponseEntity.badRequest().body(error);
        }

        Map<String, String> response = new HashMap<>();
        response.put("saludo", "¡Hola " + nombre.trim() + "!");
        response.put("color", generarColor());
        return ResponseEntity.ok(response);
    }
}