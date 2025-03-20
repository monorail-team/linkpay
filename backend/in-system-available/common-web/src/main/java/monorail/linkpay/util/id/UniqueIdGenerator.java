package monorail.linkpay.util.id;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UniqueIdGenerator implements IdGenerator {

    private static AtomicInteger sequence = new AtomicInteger();

    @Override
    public long generate(){
        long id = (((long) Instant.now().getNano())) << 32 | sequence.getAndIncrement();
        sequence.compareAndSet(Integer.MAX_VALUE, 0);
        return id; //todo timestamp:serverId:seq 형태의 유일 아이디 생성
    }
}
