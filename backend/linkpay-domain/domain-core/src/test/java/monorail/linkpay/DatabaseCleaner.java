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

    private static final String SET_REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY %s";

    private final EntityManager em;

    @Transactional
    public void truncateAllTables() {
        em.createNativeQuery(SET_REFERENTIAL_INTEGRITY.formatted("FALSE")).executeUpdate();

        List<String> existingTables = em.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'"
        ).getResultList();

        em.getMetamodel().getEntities().stream()
                .map(EntityType::getJavaType)
                .map(clazz -> {
                    Table table = clazz.getAnnotation(Table.class);
                    return (table != null) ? table.name() : clazz.getSimpleName();
                })
                .map(String::toUpperCase)
                .filter(existingTables::contains)
                .forEach(tableName -> em.createNativeQuery("TRUNCATE TABLE " + tableName)
                        .executeUpdate());

        em.createNativeQuery(SET_REFERENTIAL_INTEGRITY.formatted("TRUE")).executeUpdate();
    }
}
