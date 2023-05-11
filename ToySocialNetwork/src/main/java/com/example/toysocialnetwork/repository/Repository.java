package com.example.toysocialnetwork.repository;

import com.example.toysocialnetwork.domain.Entity;
import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;


public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id) throws ArgumentException;
    Iterable<E> findAll();
    E save(E entity) throws ArgumentException, ValidationException, RepositoryException;
    E delete(ID id) throws ArgumentException, RepositoryException;
    E update(E entity) throws ArgumentException, RepositoryException;
}
