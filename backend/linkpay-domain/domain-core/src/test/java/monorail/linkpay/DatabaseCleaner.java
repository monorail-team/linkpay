package monorail.linkpay;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseCleaner {

    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE %s";
    private static final String SET_REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY %s";
    private static final String INFORMATION_SCHEMA_TABLES = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'";

    private final EntityManager em;

    @Transactional
    public void truncateAllTables() {
        List<String> existingTables = em.createNativeQuery(INFORMATION_SCHEMA_TABLES)
                .getResultList().stream()
                .map(tuple -> tuple.toString().toLowerCase())
                .toList();

        em.createNativeQuery(SET_REFERENTIAL_INTEGRITY.formatted("FALSE")).executeUpdate();

        em.getMetamodel().getEntities().stream()
                .map(EntityType::getJavaType)
                .map(this::extractTableName)
                .map(String::toLowerCase)
                .filter(existingTables::contains)
                .forEach(tableName -> em.createNativeQuery(TRUNCATE_TABLE.formatted(tableName))
                        .executeUpdate());

        em.createNativeQuery(SET_REFERENTIAL_INTEGRITY.formatted("TRUE")).executeUpdate();
    }

    private String extractTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return table != null ? table.name() : clazz.getSimpleName();
    }
}
