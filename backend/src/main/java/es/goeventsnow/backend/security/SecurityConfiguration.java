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

         http.authorizeHttpRequests( authorize -> authorize

            .requestMatchers(HttpMethod.POST,"/api/v1/events/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/events/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/events/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/v1/events/*/image").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/events/*/image").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/events/*/image").hasRole("ADMIN")

            .requestMatchers(HttpMethod.POST,"/api/v1/participants/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/participants/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/participants/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/v1/participants/*/image").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/participants/*/image").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/participants/*/image").hasRole("ADMIN")

            .requestMatchers(HttpMethod.POST,"/api/v1/users/me/image").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/users/me/image").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/v1/users/me/image").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/v1/users/me").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/v1/users/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/users/**").hasAnyRole("USER","ADMIN")

            .requestMatchers(HttpMethod.POST,"/api/v1/reviews/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/v1/reviews/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.DELETE,"/api/v1/reviews/**").hasAnyRole("USER","ADMIN")

            .requestMatchers(HttpMethod.GET,"/api/v1/graphics/**").hasRole("ADMIN")

            .requestMatchers(HttpMethod.POST,"/api/v1/tickets/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/v1/tickets/**").hasAnyRole("USER","ADMIN")

            .anyRequest().permitAll()
        );

        http.formLogin(formLogin -> formLogin.disable());

        http.csrf(csrf -> csrf.disable());

        http.httpBasic(httpBasic -> httpBasic.disable());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
        
    }
}
