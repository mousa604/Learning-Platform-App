
package com.example.LearningPlatformApp.config;

import com.example.LearningPlatformApp.security.JwtAuthenticationFilter;
import com.example.LearningPlatformApp.security.RestAccessDeniedHandler;
import com.example.LearningPlatformApp.security.RestAuthenticationEntryPoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint,
            RestAccessDeniedHandler restAccessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                restAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                restAccessDeniedHandler
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Swagger: Public
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Authentication: Public
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/auth/register",
                                "/api/v1/auth/login"
                        ).permitAll()

                        // Public: Browse courses and their reviews/lessons
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/courses",
                                "/api/v1/courses/search",
                                "/api/v1/courses/*",
                                "/api/v1/courses/*/reviews",
                                "/api/v1/courses/*/lessons"
                        ).permitAll()

                        // Instructor: Create and submit courses
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/courses",
                                "/api/v1/courses/*/submit-for-review"
                        ).hasRole("INSTRUCTOR")

                        // Instructor: Update and delete courses
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/courses/*"
                        ).hasRole("INSTRUCTOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/courses/*"
                        ).hasRole("INSTRUCTOR")

                        // Instructor: Manage lessons
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/courses/*/lessons"
                        ).hasRole("INSTRUCTOR")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/courses/*/lessons/*"
                        ).hasRole("INSTRUCTOR")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/courses/*/lessons/*"
                        ).hasRole("INSTRUCTOR")

                        // Admin: Course moderation and dashboard
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/admin/courses/pending",
                                "/api/v1/admin/dashboard",
                                "/api/v1/courses/*/statistics"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/courses/*/approve",
                                "/api/v1/courses/*/reject"
                        ).hasRole("ADMIN")

                        // Admin: User management
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/users/*/roles"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/users/*/status"
                        ).hasRole("ADMIN")

                        // Student: Enrollments
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/courses/*/enrollments"
                        ).hasRole("STUDENT")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/students/me/enrollments"
                        ).hasRole("STUDENT")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/courses/*/enrollments/cancel"
                        ).hasRole("STUDENT")

                        // Student: Reviews
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/courses/*/reviews"
                        ).hasRole("STUDENT")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/reviews/*"
                        ).hasRole("STUDENT")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/reviews/*"
                        ).hasRole("STUDENT")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/tags",
                                "/api/v1/tags/*"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/tags"
                        ).hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/tags/*"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/tags/*"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/courses/*/tags"
                        ).hasRole("INSTRUCTOR")
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/lessons/*/progress/complete"
                        ).hasRole("STUDENT")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/lessons/courses/*/progress"
                        ).hasRole("STUDENT")
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/courses/*/certificates"
                        ).hasRole("STUDENT")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/certificates/my-certificates"
                        ).hasRole("STUDENT")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/certificates/verify/*"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/certificates/*/download"
                        ).hasRole("STUDENT")
                        .requestMatchers("/api/v1/notifications/**").hasAnyRole(
                                "STUDENT", "INSTRUCTOR", "ADMIN"
                        )




                        // Any other endpoint requires authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
