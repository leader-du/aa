package com.ssvet.approval.config.sercurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssvet.approval.entity.User;
import com.ssvet.approval.service.IUserService;
import com.ssvet.approval.utils.jwt.JwtTokenUtil;
import com.ssvet.approval.utils.resp.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * @author Programmer_Liu.
 * @since 2020/8/29 8:40
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IUserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/approval/user/login").permitAll().anyRequest().authenticated()
                .and().formLogin().loginProcessingUrl("/approval/user/login")
                .successHandler((req, resp, authentication) -> {
                    UserDetails principal = (UserDetails) authentication.getPrincipal();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwtTokenUtil.generateToken(principal);
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(CommonResult.success(token, "登陆成功")));
                    out.flush();
                    out.close();
                })
                .failureHandler((req, resp, e) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    CommonResult respBean = CommonResult.failed(e.getMessage());
                    if (e instanceof LockedException) {
                        respBean = CommonResult.failed("账户被锁定，请联系管理员!");
                    } else if (e instanceof CredentialsExpiredException) {
                        respBean = CommonResult.failed("密码过期，请联系管理员!");
                    } else if (e instanceof AccountExpiredException) {
                        respBean = CommonResult.failed("账户过期，请联系管理员!");
                    } else if (e instanceof DisabledException) {
                        respBean = CommonResult.failed("账户被禁用，请联系管理员!");
                    } else if (e instanceof BadCredentialsException) {
                        respBean = CommonResult.failed("用户名或者密码输入错误，请重新输入!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .and().csrf().disable().exceptionHandling()
                .authenticationEntryPoint((req, resp, authException) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("尚未登录，请先登录");
                    out.flush();
                    out.close();
                })
                .and().logout()
                .logoutUrl("/approval/user/logout")
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .permitAll().and();
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                User user = (User) authentication.getPrincipal();
                SecurityContextHolder.getContext().setAuthentication(authentication);
               /* jwtTokenUtil.setExpiration(1800000L);
                jwtTokenUtil.setSecret("approval");*/
                String token = jwtTokenUtil.generateToken(user);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString(CommonResult.success(new HashMap<String,Object>(){{
                    put("user", user);
                    put("accessToken", token);

                }}, "登陆成功")));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                CommonResult respBean = CommonResult.failed(exception.getMessage());
                if (exception instanceof LockedException) {
                    respBean = CommonResult.failed("账户被锁定，请联系管理员!");
                } else if (exception instanceof CredentialsExpiredException) {
                    respBean = CommonResult.failed("密码过期，请联系管理员!");
                } else if (exception instanceof AccountExpiredException) {
                    respBean = CommonResult.failed("账户过期，请联系管理员!");
                } else if (exception instanceof DisabledException) {
                    respBean = CommonResult.failed("账户被禁用，请联系管理员!");
                } else if (exception instanceof BadCredentialsException) {
                    respBean = CommonResult.failed("用户名或者密码输入错误，请重新输入!");
                }
                out.write(new ObjectMapper().writeValueAsString(respBean));
                out.flush();
                out.close();
            }
        });
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setFilterProcessesUrl("/approval/user/login");
        return loginFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);

        web.ignoring().antMatchers("/static/**");
    }
}
