package com.genetions.lojadegames.service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.genetions.lojadegames.model.Usuario;
import com.genetions.lojadegames.model.UsuarioLogin;
import com.genetions.lojadegames.repository.UsuarioRepository;
import com.genetions.lojadegames.security.JwtService;

import jakarta.validation.Valid;
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> cadastrarUsuario(@Valid Usuario usuario) {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return Optional.empty();
        }
        if (!isMaiorDeIdade(usuario.getDataNascimento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário deve ter 18 anos ou mais para se cadastrar.");
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        return Optional.ofNullable(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> atualizarUsuario(@Valid Usuario usuario) {
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
            if (buscaUsuario.isPresent() && !buscaUsuario.get().getId().equals(usuario.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
            }
            usuario.setSenha(criptografarSenha(usuario.getSenha()));
            return Optional.ofNullable(usuarioRepository.save(usuario));
        }
        return Optional.empty();
    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {
        if (usuarioLogin.isEmpty()) {
            return Optional.empty();
        }
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
                usuarioLogin.get().getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);
        if (authentication.isAuthenticated()) {
            Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
            if (usuario.isPresent()) {
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setSenha("");
                usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
                return usuarioLogin;
            }
        }
        return Optional.empty();
    }

    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }

    private String criptografarSenha(String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }

    private boolean isMaiorDeIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }
}