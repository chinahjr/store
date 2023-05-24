package com.cy.store;

import com.cy.store.entity.TUser;
import com.cy.store.mapper.TUserMapper;
import com.cy.store.service.TUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class StoreApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TUserMapper tUserMapper;

    @Autowired
    private TUserService tUserService;

    @Test
    void contextLoads() {
    }

    @Test
    void getConnerction() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void login()
    {
        TUser user = tUserService.login("admin1", "admin");
        System.out.println(user);
    }

    @Test
    public void reg()
    {
        TUser user=new TUser();
        user.setUsername("admin1");
        user.setPassword("admin");

        tUserService.reg(user);
    }
}
