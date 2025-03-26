package monorail.linkpay.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import monorail.linkpay.auth.service.AuthService;
import monorail.linkpay.linkcard.service.LinkCardService;
import monorail.linkpay.member.service.MemberService;
import monorail.linkpay.payment.service.PaymentService;
import monorail.linkpay.security.WithCustomUser;
import monorail.linkpay.wallet.service.LinkedMemberService;
import monorail.linkpay.wallet.service.LinkedWalletService;
import monorail.linkpay.wallet.service.MyWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest({
        AuthController.class,
        MemberController.class,
        MyWalletController.class,
        LinkCardController.class,
        LinkedMemberController.class,
        LinkedWalletController.class,
        PaymentController.class,
})
@WithCustomUser
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

    protected MockMvcRequestSpecification docsGiven;
    @MockitoBean
    protected AuthService authService;
    @MockitoBean
    protected MyWalletService myWalletService;
    @MockitoBean
    protected LinkCardService linkCardService;
    @MockitoBean
    protected MemberService memberService;
    @MockitoBean
    protected LinkedMemberService linkedMemberService;
    @MockitoBean
    protected LinkedWalletService linkedWalletService;
    @MockitoBean
    protected PaymentService paymentService;

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
