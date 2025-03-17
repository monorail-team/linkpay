package monorail.linkpay.util.json;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonTest {

    @Test
    public void 올바르지_않은_Json_형식인_경우_예외가_발생한다() throws Exception{
        //given
        String plainJson = "wrong";

        //when, then
        Assertions.assertThatThrownBy(() -> Json.of(plainJson))
                .isInstanceOf(IllegalArgumentException.class);
    }
}