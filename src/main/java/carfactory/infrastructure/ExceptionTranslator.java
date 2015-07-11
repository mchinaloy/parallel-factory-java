package carfactory.infrastructure;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class ExceptionTranslator implements PersistenceExceptionTranslator{

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return null;
    }

}
