package com.example.hotel.security.serivce;

import com.example.hotel.model.UserModel;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.security.dto.LoginDTO;
import com.example.hotel.security.dto.RegisterDTO;
import com.example.hotel.security.jwt.JwtHelper;
import com.example.hotel.utils.enumm.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwts;

    @Override
    public String login(LoginDTO dto) {
        // get user info
        Optional<UserModel> userOpt = repository.findByUserName(dto.getUsername());

        String encodedPassword = userOpt.get().getPassword();
        if (userOpt.get().getIsActive() == false) {
            return null;
        }
        if (passwordEncoder.matches(dto.getPassword(), encodedPassword)) {
            return jwts.generateJwtToken(dto.getUsername());
        }
        // check pass
        return null;
    }

    @Transactional
    @Override
    public Object register(RegisterDTO dto) {
        UserModel user = new UserModel();
        user.setUserName(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPosition(RoleUser.ROLE_EMPLOYEE);
        user.setIsActive(true);
        user.setFullName(dto.getFullname());

        return repository.save(user);
    }
}
