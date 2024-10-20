package br.ufpb.dsc.expense_tracker_api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufpb.dsc.expense_tracker_api.Constants;
import br.ufpb.dsc.expense_tracker_api.entity.User;
import br.ufpb.dsc.expense_tracker_api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "1. Usuários")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "Retorna os dados de um usuário específico", position = 1)
    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        // Pega o userId do token JWT
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se o usuário autenticado está tentando acessar seus próprios dados
        if (userId != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso.");
        }

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @ApiOperation(value = "Registra um novo usuário no sistema", position = 2)
    @PostMapping("auth/register")
    public Map<String, String> registerUser(@RequestBody User user) {
        return generateJWTToken(userService.registerUser(user));
    }

    @ApiOperation(value = "Autentica um usuário e gera um token JWT", position = 3)
    @PostMapping("auth/login")
    public Map<String, String> loginUser(@RequestBody User user) {
        return generateJWTToken(userService.validateUser(user.getEmail(), user.getPassword()));
    }

    @ApiOperation(value = "Atualiza os dados de um usuário específico", position = 4)
    @PutMapping("users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User userDetails, HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se o usuário autenticado está tentando atualizar seus próprios dados
        if (userId != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para atualizar este recurso.");
        }

        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @ApiOperation(value = "Exclui um usuário do sistema", position = 5)
    @DeleteMapping("users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("userId");

        // Verifica se o usuário autenticado está tentando excluir seus próprios dados
        if (userId != id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir este recurso.");
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para gerar token JWT
    private Map<String, String> generateJWTToken(User user) {
        long timeStamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timeStamp))
                .setExpiration(new Date(timeStamp + Constants.TOKEN_VALIDITY))
                .claim("userId", user.getId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .compact();

        Map<String, String> map = new HashMap<>();
        map.put("token", token);

        return map;
    }
}
