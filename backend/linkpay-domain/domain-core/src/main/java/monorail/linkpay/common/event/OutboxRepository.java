package monorail.linkpay.common.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Modifying
    @Query("update Outbox o set o.eventStatus = :eventStatus where o.id = :id")
    void updateOutboxStatusById(@Param("id") Long id, @Param("eventStatus") EventStatus eventStatus);
}
