package com.main.sellplatform.service;

import com.main.sellplatform.exception.userexception.TokenRefreshException;
import com.main.sellplatform.persistence.dao.RefreshTokenDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenDao refreshTokenDao;
    private final UserDao userDao;


    @Autowired
    public RefreshTokenService(RefreshTokenDao refreshTokenDao, UserDao userDao) {
        this.refreshTokenDao = refreshTokenDao;
        this.userDao = userDao;

    }

    public RefreshToken findByToken(String token) {

        return refreshTokenDao.getTokenByToken(token);
    }

    public RefreshToken createRefreshToken(Long id) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userDao.getUser(id));
        refreshToken.setExpiryDate(calendar.getTime());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenDao.saveToken(refreshToken);
        return refreshToken;
    }

    public boolean verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(new Date()) < 0) {
            throw new TokenRefreshException(refreshToken.getToken(), "!!!");
        }
        return true;
    }
}
