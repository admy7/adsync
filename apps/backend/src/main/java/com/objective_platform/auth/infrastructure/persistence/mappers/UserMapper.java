package com.objective_platform.auth.infrastructure.persistence.mappers;

import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.infrastructure.persistence.models.SqlUser;

public class UserMapper {

    public static User toDomain(SqlUser sqlUser) {
        return new User(sqlUser.id(), sqlUser.email(), sqlUser.password());
    }

    public static SqlUser toSql(User user) {
        return new SqlUser(user.id(), user.email(), user.password());
    }
}
