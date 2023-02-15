package com.elmiraantipina.spring.security.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

import javax.sql.DataSource;

@EnableWebSecurity//конфигурация для Spring Security
public class MySecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    DataSource dataSource;

    //alt+insert переопределяем метод configure
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);//теперь приложение знает, что нужно брать инфу о user в БД
        /*
        //дефолтное шифрование паролей, в будущем будем исп-ть БД
        UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser(userBuilder.username("zaur").password("zaur").roles("EMPLOYEE"))
                .withUser(userBuilder.username("elena").password("elena").roles("HR"))
                .withUser(userBuilder.username("ivan").password("ivan").roles("MANAGER", "HR"));
        //будут сравниваться вводимые пароли с паролями выше
        */
    }

    //alt+insert переопределяем метод configure
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //запрашивается авторизация для определенных url-ов
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER") /*страницу "/" видят роли "EMPLOYEE","HR", "MANAGER"*/
                .antMatchers("/hr_info").hasRole("HR")
                .antMatchers("/manager_info/**").hasRole("MANAGER")/*страница с началом /manager_info видят только MANAGERS*/
                .and().formLogin().permitAll();//форма логина и пароля будет запрашиваться у всех
    }
}
