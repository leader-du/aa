package com.ssvet.approval;

import com.alibaba.druid.pool.DruidDataSource;
import com.ssvet.approval.utils.upload.GetPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
public class ApprovalApplicationTests {
    @Autowired
    private DataSource datasource;
    @Test
   public  void contextLoads() {

        //System.out.println(GetPath.getUploadPath("/static/upload"));

//        System.out.println(datasource);
//        System.out.println(datasource instanceof DruidDataSource);
    }
   /* @Test
    public void testBCryptPasswordEncoder(){
        String str = "a12345";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode(str);
        System.out.println(encode1);
        System.out.println(passwordEncoder.matches(str,encode1));
    }
*/
}
