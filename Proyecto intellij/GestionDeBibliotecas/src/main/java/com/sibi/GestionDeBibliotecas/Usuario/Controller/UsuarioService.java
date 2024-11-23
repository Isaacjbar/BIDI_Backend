package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Security.Jwt.JwtUtil;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UserDetailsServiceImpl userDetailsServiceImpl, EmailService emailService, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        logger.info("La búsqueda de usuarios ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuarios, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> find(UsuarioDTO usuarioDTO, String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(usuarioDTO.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar ésta acción con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        logger.info("La búsqueda ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "Usuario", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> findForCustomer(UsuarioDTO usuarioDTO, String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(usuarioDTO.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar ésta acción con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("No existes", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        logger.info("La búsqueda de cliente ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "Usuario", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getContrasena().length() > 255) {
            return new ResponseEntity<>(new Message("La  contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        String contraseña = "(?=.*[A-Z])(?=.*\\d).{8,}";
        if (!usuarioDTO.getContrasena().matches(contraseña)) {
            return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (correoDuplicate != null) {
            if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
                return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        Usuario usuario = new Usuario(usuarioDTO.getNombre(), usuarioDTO.getApellidos(), usuarioDTO.getCorreo(), hashPassword, Rol.CLIENTE);
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(usuario, "El usuario se registró correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> modify(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getContrasena().length() > 255) {
            return new ResponseEntity<>(new Message("La  contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getNumeroTelefono().length() > 15) {
            return new ResponseEntity<>(new Message("El número de teléfono excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String contraseña = "(?=.*[A-Z])(?=.*\\d).{8,}";
        if (!usuarioDTO.getContrasena().matches(contraseña)) {
            return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (correoDuplicate != null) {
            if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
                return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        Usuario usuario = usuarioOptional.get();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
        usuario.setContrasena(hashPassword);

        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "El usuario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(UsuarioDTO usuarioDTO, String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(usuarioDTO.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar ésta acción con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("No existes", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getContrasena().length() > 255) {
            return new ResponseEntity<>(new Message("La  contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getNumeroTelefono().length() > 15) {
            return new ResponseEntity<>(new Message("El número de teléfono excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String contraseña = "(?=.*[A-Z])(?=.*\\d).{8,}";
        if (!usuarioDTO.getContrasena().matches(contraseña)) {
            return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (correoDuplicate != null) {
            if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
                return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        Usuario usuario = usuarioOptional.get();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
        usuario.setContrasena(hashPassword);

        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("Información editada exitósamente", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización de admin ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "Error al editar tu información", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> updateForCustomer(UsuarioDTO usuarioDTO, String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(usuarioDTO.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar ésta acción con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("No existes", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getApellidos().length() > 100) {
            return new ResponseEntity<>(new Message("Los apellidos exceden el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getCorreo().length() > 100) {
            return new ResponseEntity<>(new Message("El correo excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getContrasena().length() > 255) {
            return new ResponseEntity<>(new Message("La  contraseña excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }
        if (usuarioDTO.getNumeroTelefono().length() > 15) {
            return new ResponseEntity<>(new Message("El número de teléfono excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String contraseña = "(?=.*[A-Z])(?=.*\\d).{8,}";
        if (!usuarioDTO.getContrasena().matches(contraseña)) {
            return new ResponseEntity<>(new Message("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula y un número", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (correoDuplicate != null) {
            if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
                return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
            }
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        Usuario usuario = usuarioOptional.get();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setNumeroTelefono(usuarioDTO.getNumeroTelefono());
        usuario.setContrasena(hashPassword);

        usuario = usuarioRepository.saveAndFlush(usuario);
        if (usuario == null) {
            return new ResponseEntity<>(new Message("Información editada exitósamente", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        logger.info("La actualización de usuario ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "Error al editar tu información", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        Estado estado = usuario.getEstado().equals(Estado.ACTIVO) ? Estado.INACTIVO : Estado.ACTIVO;

        if (usuario.getEstado().equals(Estado.ACTIVO)) {
            Long prestamosCount = usuarioRepository.countPrestamosByUsuarioId(usuarioDTO.getUsuarioId());
            if (prestamosCount > 0) {
                return new ResponseEntity<>(new Message("El usuario tiene préstamos activos y no se puede desactivar", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
        }

        usuario.setEstado(estado);
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El estado del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("El cambio de estado ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(usuario, "El estado del usuario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findByStatus(String status) {
        Estado estado;
        try {
            estado = Estado.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Message("Estado inválido", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        List<Usuario> usuarios = usuarioRepository.findByEstado(estado);
        logger.info("La búsqueda de usuarios por estado '{}' ha sido realizada correctamente", estado);
        return new ResponseEntity<>(new Message(usuarios, "Listado de usuarios por estado", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> requestPasswordReset(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(usuarioDTO.getCorreo());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("Usuario con el correo proporcionado no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();
        if (usuario.getEstado() != Estado.ACTIVO) {
            return new ResponseEntity<>(new Message("El usuario está deshabilitado", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String caracteres = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", codigo;
        char[] caracter = new char[6];

        for (int i = 0; i < caracter.length; i++) {
            int indice = (int) (Math.random() * 62);
            caracter[i] = caracteres.charAt(indice);
        }
        codigo = new String(caracter);

        usuario.setCodigo(codigo);
        usuario.setCodigoGeneradoEn(new Date(System.currentTimeMillis()));
        usuarioRepository.saveAndFlush(usuario);

        emailService.sendEmail(
                usuarioDTO.getCorreo(),
                "Recuperación de contraseña BIDI",
                "Tu código de verificación es: " + codigo
        );

        return new ResponseEntity<>(new Message(usuario, "Correo de recuperación enviado correctamente, verifica tu bandeja de entrada", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> resetPassword(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCodigoAndCorreo(usuarioDTO.getCodigo(), usuarioDTO.getCorreo());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El código proporcionado no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        Date codigoGeneradoEn = usuario.getCodigoGeneradoEn();
        Instant codigoGeneradoInstant = codigoGeneradoEn.toInstant();
        Instant ahora = Instant.now();
        long minutosTranscurridos = ChronoUnit.MINUTES.between(codigoGeneradoInstant, ahora);
        if (minutosTranscurridos > 15) {
            return new ResponseEntity<>(new Message("El código ha expirado", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        usuario.setContrasena(hashPassword);
        usuario.setCodigo("");
        usuario.setCodigoGeneradoEn(null);
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("Hubo un error al restablecer la contraseña", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Message("Contraseña restablecida correctamente, inicia sesión.", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> resetPasswordProfile(UsuarioDTO usuarioDTO, String token) {
        token = token.substring(7);
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(usuarioDTO.getUsuarioId())) {
            return new ResponseEntity<>(new Message("No tienes permiso para realizar ésta acción con otro ID de usuario", TypesResponse.ERROR), HttpStatus.FORBIDDEN);
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("No existes", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());
        Usuario usuario = usuarioOptional.get();

        usuario.setContrasena(hashPassword);
        usuarioRepository.saveAndFlush(usuario);

        return new ResponseEntity<>(new Message("Contraseña actualizada correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}