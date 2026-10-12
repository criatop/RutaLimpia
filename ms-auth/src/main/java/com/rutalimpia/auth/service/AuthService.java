package com.rutalimpia.auth.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rutalimpia.auth.dto.AuthResponse;
import com.rutalimpia.auth.dto.LoginRequest;
import com.rutalimpia.auth.dto.RegisterRequest;
import com.rutalimpia.auth.dto.TokenInfo;
import com.rutalimpia.auth.dto.UsuarioResponse;
import com.rutalimpia.auth.dto.UsuarioUpdateRequest;
import com.rutalimpia.auth.exception.BadRequestException;
import com.rutalimpia.auth.exception.ConflictException;
import com.rutalimpia.auth.exception.NotFoundException;
import com.rutalimpia.auth.exception.UnauthorizedException;
import com.rutalimpia.auth.model.Rol;
import com.rutalimpia.auth.model.Usuario;
import com.rutalimpia.auth.repository.RolRepository;
import com.rutalimpia.auth.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioResponse registrar(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new ConflictException("Ya existe un usuario con el email " + req.email());
        }
        String nombreRol = req.rol().trim().toUpperCase();
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new BadRequestException("Rol invalido: " + req.rol() + " (use VECINO o CONDUCTOR)"));
        if ("CONDUCTOR".equals(nombreRol) && req.camionId() == null) {
            throw new BadRequestException("Un CONDUCTOR debe tener camionId");
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setRol(rol);
        u.setCamionId("CONDUCTOR".equals(nombreRol) ? req.camionId() : null);
        return toResponse(usuarioRepository.save(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByEmail(req.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));
        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new UnauthorizedException("Credenciales invalidas");
        }
        return new AuthResponse(jwtService.generar(u), "Bearer", u.getId(), u.getRol().getNombre(), u.getCamionId());
    }

    @Transactional(readOnly = true)
    public TokenInfo validar(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Falta el header Authorization: Bearer <token>");
        }
        Claims c = jwtService.parsear(authorizationHeader.substring(7));
        Number usuarioId = c.get("usuarioId", Number.class);
        Number camionId = c.get("camionId", Number.class);
        return new TokenInfo(true,
                usuarioId == null ? null : usuarioId.longValue(),
                c.getSubject(),
                c.get("rol", String.class),
                camionId == null ? null : camionId.longValue());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return toResponse(buscar(id));
    }

    public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest req) {
        Usuario u = buscar(id);
        u.setNombre(req.nombre());
        if ("CONDUCTOR".equals(u.getRol().getNombre())) {
            u.setCamionId(req.camionId());
        }
        return toResponse(usuarioRepository.save(u));
    }

    public void eliminar(Long id) {
        usuarioRepository.delete(buscar(id));
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + id));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol().getNombre(), u.getCamionId());
    }
}
