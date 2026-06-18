package auth.auth.Controller;

import auth.auth.dto.AuthResponseDTO;
import auth.auth.dto.LoginRequestDTO;
import auth.auth.dto.RegisterRequestDTO;
import auth.auth.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        log.info("POST /v1/auth/login - LOGIN usuario={}", dto.getUsername());
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        log.info("POST /v1/auth/register - REGISTRAR usuario={}", dto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @GetMapping("/validar")
    public ResponseEntity<AuthResponseDTO> validar(@RequestHeader("Authorization") String authHeader) {
        log.info("GET /v1/auth/validar - VALIDAR TOKEN");
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(authService.validarToken(token));
    }
}
