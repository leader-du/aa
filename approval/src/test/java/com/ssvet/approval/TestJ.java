package com.ssvet.approval;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Programmer_Liu.
 * @since 2020/8/29 10:02
 */
public class TestJ {
    @Test
    public void bCryptPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
//        System.out.println(passwordEncoder.encode("123"));
    }
}
