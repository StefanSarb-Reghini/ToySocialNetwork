package com.example.toysocialnetwork.repository.file;

import com.example.toysocialnetwork.domain.Entity;
import com.example.toysocialnetwork.domain.validators.ValidationException;
import com.example.toysocialnetwork.domain.validators.Validator;
import com.example.toysocialnetwork.exceptions.ArgumentException;
import com.example.toysocialnetwork.exceptions.RepositoryException;
import com.example.toysocialnetwork.repository.memory.InMemoryRepository;

public abstract class FileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String filename;

    public FileRepository(Validator<E> validator, String filename) {
        super(validator);
        this.filename = filename;
        loadFromFile();
    }

    protected abstract void loadFromFile();

    protected abstract void saveToFile();

    @Override
    public E save(E entity) throws ArgumentException, ValidationException, RepositoryException {
        super.save(entity);
        saveToFile();
        return entity;
    }

    @Override
    public E delete(ID id) throws ArgumentException, RepositoryException {
        E entity = super.delete(id);
        saveToFile();
        return entity;
    }

    @Override
    public E update(E entity) throws ArgumentException, RepositoryException {
        var previous = super.update(entity);
        saveToFile();
        return previous;
    }
}
