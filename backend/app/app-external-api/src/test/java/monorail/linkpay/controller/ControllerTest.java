package monorail.linkpay.controller;

import monorail.linkpay.auth.service.AuthService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import monorail.linkpay.security.WithCustomUser;
import monorail.linkpay.wallet.service.WalletHistoryService;
import monorail.linkpay.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest({
    AuthController.class,
    WalletController.class
})
@WithCustomUser
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

    protected MockMvcRequestSpecification docsGiven;
    @MockitoBean
    protected AuthService authService;
    @MockitoBean
    protected WalletService walletService;
    @MockitoBean
    protected WalletHistoryService walletHistoryService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        docsGiven = RestAssuredMockMvc.given()
            .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()))
                .build()).log().all();
    }
}
