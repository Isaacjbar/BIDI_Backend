package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Enum.Estado;
import com.sibi.GestionDeBibliotecas.Util.Response.Message;
import com.sibi.GestionDeBibliotecas.Util.Enum.Rol;
import com.sibi.GestionDeBibliotecas.Util.Enum.TypesResponse;
import com.sibi.GestionDeBibliotecas.Util.Services.EmailDto;
import com.sibi.GestionDeBibliotecas.Util.Services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final EmailService emailService;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UserDetailsServiceImpl userDetailsServiceImpl, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.emailService = emailService;
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        logger.info("La búsqueda ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuarios, "Listado de usuarios", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> findById(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        logger.info("La búsqueda ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "Usuario", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
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

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
            return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        String hashPassword = userDetailsServiceImpl.encodePassword(usuarioDTO.getContrasena());

        Usuario usuario = new Usuario(usuarioDTO.getNombre(), usuarioDTO.getCorreo(), hashPassword, Rol.CLIENTE, usuarioDTO.getNumeroTelefono());
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El usuario no se registró", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("El registro ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "El usuario se registró correctamente", TypesResponse.SUCCESS), HttpStatus.CREATED);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioDTO.getUsuarioId());
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }
        if (usuarioDTO.getNombre().length() > 100) {
            return new ResponseEntity<>(new Message("El nombre excede el número de caracteres", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
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

        String correoDuplicate = usuarioRepository.findUsuarioByCorreo(usuarioDTO.getCorreo());
        if (usuarioDTO.getCorreo().trim().equalsIgnoreCase(correoDuplicate.trim())) {
            return new ResponseEntity<>(new Message("El correo ya existe", TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Message> changeStatus(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        Estado estado = usuario.getEstado().equals(Estado.ACTIVO) ? Estado.INACTIVO : Estado.ACTIVO;

        if (usuario.getEstado().equals(Estado.ACTIVO)) {
            Long prestamosCount = usuarioRepository.countPrestamosByUsuarioId(id);
            if (prestamosCount > 0) {
                return new ResponseEntity<>(new Message("El usuario tiene préstamos activos y no se puede desactivar", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
        }

        usuario.setEstado(estado);
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El estado del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("La actualización ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "El estado del usuario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    // 1. Endpoint para solicitar la recuperación de contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<Message> requestPasswordReset(@Validated @RequestBody EmailDto emailDto) {
        try {
            if (!usuarioRepository.findByCorreo(emailDto.getCorreo()).isPresent()) {
                return new ResponseEntity<>(new Message("El correo no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
            }

            String token = UUID.randomUUID().toString();
            usuarioService.saveToken(emailDto.getCorreo(), token); // Almacena el token y la fecha en la base de datos

            // Enviar correo sin el token en la URL
            emailService.sendEmail(emailDto.getCorreo(), "Recuperación de contraseña",
                    "Para recuperar tu contraseña, haz clic en el siguiente enlace: " +
                            "https://localhost:8080/sibi/reset-password");

            return new ResponseEntity<>(new Message("Correo de recuperación enviado correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("Error al enviar el correo: " + e.getMessage(), TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveToken(String email, String token) {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setCodigo(token); // Asigna el token al atributo `codigo`
        usuario.setCodigoGeneradoEn(new Date(System.currentTimeMillis())); // Establece la fecha actual
        usuarioRepository.save(usuario); // Guarda el usuario actualizado
    }
}