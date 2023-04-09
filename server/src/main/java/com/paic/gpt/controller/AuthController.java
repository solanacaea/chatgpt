package com.paic.gpt.controller;

import com.paic.gpt.model.Membership;
import com.paic.gpt.model.RoleCode;
import com.paic.gpt.model.User;
import com.paic.gpt.model.UserRole;
import com.paic.gpt.payload.*;
import com.paic.gpt.repository.RoleRepository;
import com.paic.gpt.repository.UserDao;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.repository.UserRoleRepository;
import com.paic.gpt.security.CurrentUser;
import com.paic.gpt.security.CustomUserDetailsService;
import com.paic.gpt.security.JwtTokenProvider;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuditService auditService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@CurrentUser UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("permission denied");
        } else if (currentUser.getUsername().equalsIgnoreCase("moss")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("permission denied");
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        if ("moss".equalsIgnoreCase(loginRequest.getUsername())) {
//            throw new Exception("Moss账号已停用");
            return ResponseEntity.ok(new JwtAuthenticationResponse(
                    null, "moss_expired", "failed"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        String jwt = tokenProvider.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(loginRequest.getUsername()).get();
        String startStr = "2023-04-07 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startStr);
        if (startDate.after(user.getUpdatedAt())) {
            logger.info(loginRequest.getUsername() + " login successfully, required change password.");
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,"expired"));
        }
        logger.info(loginRequest.getUsername() + " login successfully.");

        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            auditService.auditLogin(attr.getRequest(), loginRequest.getUsername(), "login");
        } catch (Exception e) {
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
//                    HttpStatus.BAD_REQUEST);
//        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(), 1);

        user.setPwd(passwordEncoder.encode(user.getPwd()));

        UserRole userRole = new UserRole(signUpRequest.getUsername(), RoleCode.User.name());
        userRoleRepository.save(userRole);

//        Role userRole = roleRepository.findByCode(RoleCode.User)
//                .orElseThrow(() -> new AppException("User Role not set."));
//        user.setRoles(Collections.singleton(userRole));

        Membership ms = new Membership();
        ms.setId(2);
        user.setMember(ms);
        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/change2")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> change2(@CurrentUser UserPrincipal currentUser,
                                    @Valid @RequestBody LoginRequest req) {
        String encodedNew = passwordEncoder.encode(req.getPassword());
        userDao.updatePwd(currentUser.getUsername(), encodedNew);
        customUserDetailsService.cacheEvict(currentUser.getId());
        customUserDetailsService.cacheEvict(currentUser.getUsername());
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            auditService.auditLogin(attr.getRequest(), req.getUsername(), "change2 pwd");
        } catch (Exception e) {
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/change")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> change(@CurrentUser UserPrincipal currentUser,
                                    @Valid @RequestBody ChangeRequest req) {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            auditService.auditLogin(attr.getRequest(), req.getUsername(), "change pwd");
        } catch (Exception e) {
        }

        String encodedPre = passwordEncoder.encode(req.getPrePassword());
        boolean valid = true;
        if (valid) {
            String encodedNew = passwordEncoder.encode(req.getNewPassword());
            userDao.updatePwd(currentUser.getUsername(), encodedNew);
            customUserDetailsService.cacheEvict(currentUser.getId());
            customUserDetailsService.cacheEvict(currentUser.getUsername());
        } else {
            return ResponseEntity.ok(new MsgResponse("failed", "pwdWrong"));
        }
        return ResponseEntity.ok(new MsgResponse("success", null));
    }

}
