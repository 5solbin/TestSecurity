package shop.mtcoding.testsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shop.mtcoding.testsecurity.dto.JoinDTO;
import shop.mtcoding.testsecurity.entity.UserEntity;
import shop.mtcoding.testsecurity.repository.UserRepository;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(JoinDTO joinDTO) {

        // 유저 네임 중복 검사
        if(userRepository.existsByUsername(joinDTO.getUsername())){
            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(passwordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_ADMIN");

        userRepository.save(data);

    }
}
