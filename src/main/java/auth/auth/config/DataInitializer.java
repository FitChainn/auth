package auth.auth.config;

import auth.auth.model.Rol;
import auth.auth.model.Usuario;
import auth.auth.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("DataInitializer: ya existen usuarios, se omite la carga inicial.");
            return;
        }

        log.info("DataInitializer: cargando usuarios de prueba...");

        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNombre("Administrador FitChain");
        admin.setRol(Rol.ADMIN);
        admin.setActivo(true);

        Usuario entrenador = new Usuario();
        entrenador.setUsername("entrenador1");
        entrenador.setPassword(passwordEncoder.encode("entrenador123"));
        entrenador.setNombre("Guillermo Salas");
        entrenador.setRol(Rol.ENTRENADOR);
        entrenador.setActivo(true);

        Usuario cliente = new Usuario();
        cliente.setUsername("cliente1");
        cliente.setPassword(passwordEncoder.encode("cliente123"));
        cliente.setNombre("Juan Pérez");
        cliente.setRol(Rol.CLIENTE);
        cliente.setActivo(true);

        usuarioRepository.save(admin);
        usuarioRepository.save(entrenador);
        usuarioRepository.save(cliente);

        log.info("DataInitializer: {} usuarios cargados.", usuarioRepository.count());
        log.info("  ADMIN      → username: admin       | password: admin123");
        log.info("  ENTRENADOR → username: entrenador1 | password: entrenador123");
        log.info("  CLIENTE    → username: cliente1    | password: cliente123");
    }
}
