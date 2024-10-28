package com.sirkaue.demojunit5.config;

import com.sirkaue.demojunit5.mapper.UserMapper;
import com.sirkaue.demojunit5.mapper.impl.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }
}
