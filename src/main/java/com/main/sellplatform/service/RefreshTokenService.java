package com.main.sellplatform.service;

import com.main.sellplatform.entitymanager.testdao.UserDao2;
import com.main.sellplatform.exception.userexception.TokenRefreshException;
import com.main.sellplatform.persistence.dao.RefreshTokenDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private RefreshTokenDao refreshTokenDao;
    private UserDao userDao;
    private UserDao2 userDao2;

    @Autowired
    public RefreshTokenService(RefreshTokenDao refreshTokenDao, UserDao userDao, UserDao2 userDao2) {
        this.refreshTokenDao = refreshTokenDao;
        this.userDao = userDao;
        this.userDao2 = userDao2;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenDao.getTokenByToken(token);
    }

    public RefreshToken createRefreshToken(Long id) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userDao2.getUser(id));
        refreshToken.setExpiryDate(LocalDate.now().plusDays(1));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenDao.saveToken(refreshToken);
        return refreshToken;
    }

    public boolean verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(LocalDate.now()) < 0) {
            throw new TokenRefreshException(refreshToken.getToken(), "!!!");
        }
        return true;
    }
}
