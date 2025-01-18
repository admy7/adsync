package com.objective_platform.auth.infrastructure.persistence;

import com.objective_platform.auth.application.ports.UserRepository;
import com.objective_platform.auth.domain.models.User;
import com.objective_platform.auth.infrastructure.persistence.mappers.UserMapper;
import com.objective_platform.auth.infrastructure.persistence.models.SqlUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
public class SqlUserRepository implements UserRepository {
    private EntityManager entityManager;

    public SqlUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var query = entityManager.createQuery(
                "SELECT u FROM SqlUser u WHERE u.email = :email", SqlUser.class);

        query.setParameter("email", email);

        return query.getResultStream().findFirst().map(UserMapper::toDomain);
    }

    @Override
    public void save(User user) {
        SqlUser sqlUser = UserMapper.toSql(user);
        entityManager.merge(sqlUser);
    }

    @Override
    public void clear() {
        entityManager.createQuery("DELETE FROM SqlUser").executeUpdate();
    }
}
