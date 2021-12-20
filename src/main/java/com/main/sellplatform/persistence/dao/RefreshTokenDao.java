package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.persistence.entity.RefreshToken;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;

@Component
public class RefreshTokenDao {
    private final EntityManager entityManager;
    @Autowired
    public RefreshTokenDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RefreshToken getTokenById(Long id){
        return entityManager.getObjectById(RefreshToken.class, id, null, null);
    }


    public RefreshToken getTokenByToken(String token){
        return entityManager.getObjectByWhere(RefreshToken.class, "(OBJ_12ATTR_62 = ?)", Collections.singletonList(token));
    }

    public RefreshToken saveToken(RefreshToken token){
        return entityManager.merge(token);
    }
}
