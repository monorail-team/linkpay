package monorail.linkpay.util.id;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UniqueIdGenerator implements IdGenerator {

    private static AtomicInteger sequence = new AtomicInteger();

    @Override
    public long generate() {
        // TODO: 서버 Id 포함해서 guid 생성하도록 고치기 & Instant/SystemTime 비교
        // https://github.com/f4b6a3/tsid-creator 이거 알아보고 적용하기
        long time = System.currentTimeMillis(); // 밀리초 단위 시간
        long seq = sequence.getAndIncrement();
        sequence.compareAndExchange(0xFFFF, 0);// 16비트 시퀀스 제한, overflow 방지
        return (time << 16) | seq;
    }
}
