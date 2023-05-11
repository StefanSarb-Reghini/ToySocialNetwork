package com.example.toysocialnetwork.repository.memory;

import com.example.toysocialnetwork.domain.Entity;
import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.domain.validators.Validator;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.repository.Repository;


import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private Validator<E> validator;
    protected Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public E findOne(ID id) throws ArgumentException {
        if (id == null) {
            throw new ArgumentException("The ID that has to be found cannot be null!\n");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ArgumentException, ValidationException, RepositoryException {
        if (entity == null) {
            throw new ArgumentException("Entity to be saved cannot be null!\n");
        }
        validator.validate(entity);
        if (entities.get(entity.getId()) != null) {
            throw new RepositoryException("Entity already exists!\n");
        }

        for (var entry : entities.entrySet()) {
            if (entity.equals(entry.getValue())) {
                throw new RepositoryException("Entity already exists!\n");
            }
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E delete(ID id) throws ArgumentException, RepositoryException {
        if (id == null) {
            throw new ArgumentException("The ID to be deleted cannot be null");
        }

        if (entities.get(id) == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        return entities.remove(id);
    }

    @Override
    public E update(E entity) throws ArgumentException, RepositoryException {
        if (entity == null) {
            throw new ArgumentException("Entity to be updated cannot be null!\n");
        }

        if (entities.get(entity.getId()) == null) {
            throw new RepositoryException("Entity doesn't exist!");
        }

        validator.validate(entity);

        return entities.replace(entity.getId(), entity);
    }
}
