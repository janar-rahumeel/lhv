package ee.lhv.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public abstract class AbstractRestControllerIntegrationTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
