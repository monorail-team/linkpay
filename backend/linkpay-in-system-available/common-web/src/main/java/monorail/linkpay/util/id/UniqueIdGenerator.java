package monorail.linkpay.util.id;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class UniqueIdGenerator implements IdGenerator {

    private static AtomicInteger sequence = new AtomicInteger();

    @Override
    public long generate() {
        long time = System.currentTimeMillis(); // 밀리초 단위 시간
        long seq = sequence.getAndIncrement() & 0xFFFF; // 16비트 시퀀스 제한
        return (time << 16) | seq;
    }
}
