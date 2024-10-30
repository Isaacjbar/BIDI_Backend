package com.sibi.GestionDeBibliotecas.Usuario.Controller;

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

@Service
@Transactional
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.usuarioRepository = usuarioRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
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
}