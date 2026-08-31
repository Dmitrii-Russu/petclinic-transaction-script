package dev.dmitriirussu.petclinic.infrastructure.command.support;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;

public final class ConstraintViolationTranslator<T> {

    public interface ExceptionFactory<T> {
        RuntimeException create(ServerErrorMessage error, T entity, DataIntegrityViolationException cause);
    }

    private final Map<String, ExceptionFactory<T>> handlers;

    public ConstraintViolationTranslator(Map<String, ExceptionFactory<T>> handlers) {
        this.handlers = handlers;
    }

    public RuntimeException translate(DataIntegrityViolationException e, T entity) {
        if (!(e.getMostSpecificCause() instanceof PSQLException pgEx)) {
            return generic(e);
        }
        var error = pgEx.getServerErrorMessage();
        if (error == null) {
            return generic(e);
        }
        if ("23502".equals(pgEx.getSQLState())) {
            return new IllegalArgumentException("Field cannot be null: " + error.getColumn(), e);
        }

        var handler = handlers.get(error.getConstraint());
        if (handler != null) {
            return handler.create(error, entity, e);
        }
        if ("23503".equals(pgEx.getSQLState())) {
            return new IllegalArgumentException("Referenced entity does not exist: " + error.getConstraint(), e);
        }
        return generic(e);
    }

    private static IllegalStateException generic(DataIntegrityViolationException e) {
        return new IllegalStateException("Data integrity violation", e);
    }
}