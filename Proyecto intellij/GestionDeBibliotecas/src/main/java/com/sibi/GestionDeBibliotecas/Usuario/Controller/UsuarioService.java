package com.sibi.GestionDeBibliotecas.Usuario.Controller;

import com.sibi.GestionDeBibliotecas.Prestamo.Model.PrestamoRepository;
import com.sibi.GestionDeBibliotecas.Security.UserDetailsServiceImpl;
import com.sibi.GestionDeBibliotecas.Usuario.Model.Usuario;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioDTO;
import com.sibi.GestionDeBibliotecas.Usuario.Model.UsuarioRepository;
import com.sibi.GestionDeBibliotecas.Util.Estado;
import com.sibi.GestionDeBibliotecas.Util.Message;
import com.sibi.GestionDeBibliotecas.Util.Rol;
import com.sibi.GestionDeBibliotecas.Util.TypesResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PrestamoRepository prestamoRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UserDetailsServiceImpl userDetailsServiceImpl, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.prestamoRepository = prestamoRepository;
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

        Usuario usuario = new Usuario(usuarioDTO.getNombre(), usuarioDTO.getCorreo(), hashPassword, Rol.CLIENTE, Estado.ACTIVO, usuarioDTO.getNumeroTelefono());
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
    public ResponseEntity<Message> changeStatus(Long id, Estado nuevoEstado) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (!usuarioOptional.isPresent()) {
            return new ResponseEntity<>(new Message("El usuario no existe", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        if (nuevoEstado == Estado.INACTIVO) {
            Long prestamosCount = usuarioRepository.countPrestamosByUsuarioId(id);
            if (prestamosCount > 0) {
                return new ResponseEntity<>(new Message("El usuario tiene préstamos activos y no se puede desactivar", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
            }
        }

        usuario.setEstado(nuevoEstado);
        usuario = usuarioRepository.saveAndFlush(usuario);

        if (usuario == null) {
            return new ResponseEntity<>(new Message("El estado del usuario no se actualizó", TypesResponse.ERROR), HttpStatus.BAD_REQUEST);
        }

        logger.info("La actualización ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(usuario, "El estado del usuario se actualizó correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}