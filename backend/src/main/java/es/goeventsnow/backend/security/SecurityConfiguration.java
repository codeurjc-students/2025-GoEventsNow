package es.goeventsnow.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.goeventsnow.backend.security.jwt.JwtRequestFilter;
import es.goeventsnow.backend.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String EVENTS = "/api/v1/events/**";
    private static final String PARTICIPANTS = "/api/v1/participants/**";
    private static final String EVENT_IMAGES = "/api/v1/events/*/image";
    private static final String PARTICIPANT_IMAGES = "/api/v1/participants/*/image";
    private static final String USERS = "/api/v1/users/**";
    private static final String USER_IMAGE = "/api/v1/users/me/image";
    private static final String USER_ME = "/api/v1/users/me";
    private static final String REVIEWS = "/api/v1/reviews/**";
    private static final String TICKETS = "/api/v1/tickets/**";
    private static final String GRAPHICS = "/api/v1/graphics/**";

    @Autowired
    RepositoryUserDetailsService userDetailService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/v1/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http.authorizeHttpRequests(authorize -> authorize

                .requestMatchers(HttpMethod.POST, EVENTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, EVENTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, EVENTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.POST, EVENT_IMAGES).hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, EVENT_IMAGES).hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, EVENT_IMAGES).hasRole(ADMIN)

                .requestMatchers(HttpMethod.POST, PARTICIPANTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, PARTICIPANTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, PARTICIPANTS).hasRole(ADMIN)
                .requestMatchers(HttpMethod.POST, PARTICIPANT_IMAGES).hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT,PARTICIPANT_IMAGES).hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, PARTICIPANT_IMAGES).hasRole(ADMIN)

                .requestMatchers(HttpMethod.POST, USER_IMAGE).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.PUT, USER_IMAGE).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.GET, USER_IMAGE).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.GET, USER_ME).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.PUT, USERS).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.POST, USERS).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.DELETE, USERS).hasAnyRole(USER, ADMIN)

                .requestMatchers(HttpMethod.POST, REVIEWS).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.PUT, REVIEWS).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.DELETE, REVIEWS).hasAnyRole(USER, ADMIN)

                .requestMatchers(HttpMethod.GET, GRAPHICS).hasRole(ADMIN)

                .requestMatchers(HttpMethod.POST, TICKETS).hasAnyRole(USER, ADMIN)
                .requestMatchers(HttpMethod.GET, TICKETS).hasAnyRole(USER, ADMIN)

                .anyRequest().permitAll());

        http.formLogin(formLogin -> formLogin.disable());

        http.csrf(csrf -> csrf.disable());

        http.httpBasic(httpBasic -> httpBasic.disable());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
