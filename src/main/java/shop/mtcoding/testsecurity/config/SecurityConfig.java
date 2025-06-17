package shop.mtcoding.testsecurity.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import shop.mtcoding.testsecurity.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity // 이 클래스가 스프링 시큐리티 하에 관리 되도록 하는 어노테이션
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 경로의 특정 접근 권한 설정
        http
                .authorizeHttpRequests((auth) ->auth
                .requestMatchers("/","/login","/loginProc","/join","/joinProc").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated()
        );

        // admin 페이지와 같이 로그인이 필요한 페이지면 오류를 뱉지 않고 자동으로 로그인 페이지로 리다이렉트 해준다.
        // loginProcessingUrl은 login 페이지에서 받은 데이터를 Post 방식으로 넘길 주소를 나타낸다
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc").permitAll());


//        http
//                .csrf((auth) -> auth.disable());

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                );

        http
                .logout((auth) -> auth.logoutUrl("/logout").logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("1234"))
                .roles("USER")
                .build();



        return new InMemoryUserDetailsManager(user1,user2);
    }
}
