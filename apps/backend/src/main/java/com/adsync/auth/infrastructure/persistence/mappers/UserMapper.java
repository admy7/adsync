package com.adsync.auth.infrastructure.persistence.mappers;

import com.adsync.auth.domain.models.User;
import com.adsync.auth.infrastructure.persistence.models.SqlUser;

public class UserMapper {

    public static User toDomain(SqlUser sqlUser) {
        return new User(sqlUser.id(), sqlUser.email(), sqlUser.password());
    }

    public static SqlUser toSql(User user) {
        return new SqlUser(user.id(), user.email(), user.password());
    }
}
