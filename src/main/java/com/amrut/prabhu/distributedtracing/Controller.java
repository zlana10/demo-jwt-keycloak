package com.amrut.prabhu.distributedtracing;

import com.amrut.prabhu.entity.LoginRequest;
import com.amrut.prabhu.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@ComponentScan(basePackages="com.amrut.prabhu.security")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private RestTemplate restTemplate;
    private JwtTokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;

    @Value("${spring.application.name}")
    private String applicationName;

    public Controller(RestTemplate restTemplate, JwtTokenProvider tokenProvider) {
        this.restTemplate = restTemplate;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok("Login OK :" +token);
    }

    @GetMapping("/logged")
    public ResponseEntity login(@RequestParam("username") String userName) {
//        String token = tokenProvider.generateToken(userName);
        return ResponseEntity.ok("Login from Keycloak OK ");
    }

    @GetMapping("/path1")
    public ResponseEntity path1() {
        RemoteCallCommand command = new RemoteCallCommand("http://localhost:1080/service/path2");
        String responseRemote = command.execute();
        logger.info("Incoming request at {} for request /path1 ", applicationName);
        String response = restTemplate.getForObject("http://localhost:1080/service/path2", String.class);
        return ResponseEntity.ok("response from /path1 + " + response + responseRemote);
    }

    @GetMapping("/path2")
    public ResponseEntity path2(@RequestParam("token") String tokenFromClient) {
        logger.info("Incoming request at {} at /path2", applicationName);
        Claims validateToken = tokenProvider.validateToken(tokenFromClient);
        System.out.println(validateToken);
        return ResponseEntity.ok("response from /path2 token: " + tokenFromClient);
    }
}
