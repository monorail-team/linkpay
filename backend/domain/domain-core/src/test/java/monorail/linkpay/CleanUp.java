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
public class CleanUp {

    private final EntityManager entityManager;

    @Transactional
    public void cleanAll() {
        // 1. 외래 키 제약 해제
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        // 2. 현재 존재하는 테이블 목록 조회
        List<String> existingTables = entityManager.createNativeQuery(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'"
        ).getResultList();

        // 3. 모든 테이블 삭제 (존재하는 경우만 TRUNCATE)
        entityManager.getMetamodel().getEntities().stream()
                .map(EntityType::getJavaType)
                .map(clazz -> {
                    Table table = clazz.getAnnotation(Table.class);
                    return (table != null) ? table.name() : clazz.getSimpleName(); // @Table이 없으면 클래스명 사용
                })
                .map(String::toUpperCase) // H2에서는 테이블명이 대문자로 저장될 가능성이 높음
                .filter(existingTables::contains) // 존재하는 테이블만 처리
                .forEach(tableName -> entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());

        // 4. 외래 키 제약 복구
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
