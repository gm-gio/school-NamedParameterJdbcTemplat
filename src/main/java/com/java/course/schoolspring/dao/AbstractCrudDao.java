package com.java.course.schoolspring.dao;

import com.java.course.schoolspring.model.HasId;

public abstract class AbstractCrudDao<T extends HasId<K>, K> implements CrudDAO<T, K> {
    protected abstract T create(T entity);

    protected abstract T update(T entity);

    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }
}



