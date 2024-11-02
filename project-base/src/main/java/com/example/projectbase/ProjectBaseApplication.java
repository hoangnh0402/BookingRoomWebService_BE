package com.example.projectbase;

import com.example.projectbase.config.UserInfoProperties;
import com.example.projectbase.constant.RoleConstant;
import com.example.projectbase.domain.entity.Role;
import com.example.projectbase.domain.entity.User;
import com.example.projectbase.repository.RoleRepository;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.CustomUserDetailsService;
import com.example.projectbase.util.FileUtil;
import com.example.projectbase.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({UserInfoProperties.class})
@SpringBootApplication
public class ProjectBaseApplication {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final CustomUserDetailsService customUserDetailsService;

  private final UploadFileUtil uploadFile;

  public static void main(String[] args) {
    Environment env = SpringApplication.run(ProjectBaseApplication.class, args).getEnvironment();
    String appName = env.getProperty("spring.application.name");
    if (appName != null) {
      appName = appName.toUpperCase();
    }
    String port = env.getProperty("server.port");
    log.info("-------------------------START " + appName
        + " Application------------------------------");
    log.info("   Application         : " + appName);
    log.info("   Url swagger-ui      : http://localhost:" + port + "/swagger-ui.html");
    log.info("-------------------------START SUCCESS " + appName
        + " Application------------------------------");
  }

  @Bean
  CommandLineRunner init(UserInfoProperties userInfo) {
    return args -> {
      PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      //init role
      if (roleRepository.count() == 0) {
        roleRepository.save(new Role(null, RoleConstant.ADMIN, null));
        roleRepository.save(new Role(null, RoleConstant.USER, null));
      }
      //init admin
      if (userRepository.count() == 0) {
        String url = uploadFile.uploadImage(FileUtil.getBytesFileByPath(userInfo.getAvatar()));
        User admin = new User(userInfo.getEmail(), userInfo.getPhone(), passwordEncoder.encode(userInfo.getPassword()),
                userInfo.getFirstName(), userInfo.getLastName(), userInfo.getGender(), LocalDate.parse(userInfo.getBirthday()),
                userInfo.getAddress(), Boolean.TRUE, url, roleRepository.findByRoleName(RoleConstant.ADMIN));
        userRepository.save(admin);
      }

      User admin = userRepository.findByEmail(userInfo.getEmail()).get();
      UserDetails userDetails = customUserDetailsService.loadUserById(admin.getId());
      UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    };
  }
}
