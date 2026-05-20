package auth.auth.Service;

import auth.auth.dto.AuthResponseDTO;
import auth.auth.dto.LoginRequestDTO;
import auth.auth.dto.RegisterRequestDTO;
import auth.auth.model.Usuario;
import auth.auth.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(LoginRequestDTO dto) {
        log.info("Intento de login para usuario: {}", dto.getUsername());

        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol().name());
        log.info("Login exitoso para usuario: {}", dto.getUsername());

        return new AuthResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol().name(),
                "Login exitoso"
        );
    }

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        log.info("Registrando nuevo usuario: {}", dto.getUsername());

        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombre(dto.getNombre());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
        log.info("Usuario registrado: {}", dto.getUsername());

        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol().name());

        return new AuthResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol().name(),
                "Usuario registrado correctamente"
        );
    }

    public AuthResponseDTO validarToken(String token) {
        if (!jwtService.validarToken(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        String username = jwtService.extraerUsername(token);
        String rol = jwtService.extraerRol(token);

        return new AuthResponseDTO(token, username, null, rol, "Token válido");
    }
}
