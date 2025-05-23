package com.genetions.lojadegames.controller;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.genetions.lojadegames.model.Usuario;
import com.genetions.lojadegames.model.UsuarioLogin;
import com.genetions.lojadegames.repository.UsuarioRepository;
import com.genetions.lojadegames.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }
        if (usuario.getDataNascimento() == null || !isMaiorDeIdade(usuario.getDataNascimento())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário deve ter 18 anos ou mais para se cadastrar.");
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@Valid @RequestBody Usuario usuario) {
        if (!usuarioRepository.findById(usuario.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }
        Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
        if (buscaUsuario.isPresent() && !buscaUsuario.get().getId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PostMapping("/logar")
    public ResponseEntity<?> autenticarUsuario(@Valid @RequestBody UsuarioLogin usuarioLogin) {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.getUsuario());
        if (usuario.isEmpty() || !verificarSenha(usuarioLogin.getSenha(), usuario.get().getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
        }
        var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.getUsuario(), usuarioLogin.getSenha());
        Authentication authentication = authenticationManager.authenticate(credenciais);
        if (authentication.isAuthenticated()) {
            usuarioLogin.setId(usuario.get().getId());
            usuarioLogin.setNome(usuario.get().getNome());
            usuarioLogin.setFoto(usuario.get().getFoto());
            usuarioLogin.setSenha("");
            usuarioLogin.setToken(gerarToken(usuarioLogin.getUsuario()));
            return ResponseEntity.ok(usuarioLogin);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticação falhou.");
    }

    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }

    private String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    private boolean verificarSenha(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }

    private boolean isMaiorDeIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }
}