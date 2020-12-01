package nl.p4c.infra;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.apigateway.EndpointConfiguration;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaRestApi.Builder;
import software.amazon.awscdk.services.apigateway.StageOptions;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableEncryption;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

public class FeedbackStack extends Stack {
    public FeedbackStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public FeedbackStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Table feedbackTable = Table.Builder.create(this, "FeedbackTable")
            .tableName("Questionnaires")
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .encryption(TableEncryption.AWS_MANAGED)
            .partitionKey(Attribute.builder().name("PK").type(AttributeType.STRING).build())
            .sortKey(Attribute.builder().name("SK").type(AttributeType.STRING).build())
            .build();

        Map<String, String> environment = new HashMap<>();
        environment.put("DISABLE_SIGNAL_HANDLERS", "true");

        Function apiFunction = Function.Builder.create(this, "ApiFunction")
            .runtime(Runtime.PROVIDED)
            .timeout(Duration.seconds(15))
            .memorySize(512)
            .code(Code.fromAsset("../api/target/function.zip"))
            .handler("not.used.in.provided.runtime")
            .environment(environment)
            .build();

        LambdaRestApi lambdaRestApi = LambdaRestApi.Builder.create(this, "Api")
            .handler(apiFunction)
            .binaryMediaTypes(Collections.singletonList("*/*"))
            .deployOptions(StageOptions.builder()
                .throttlingBurstLimit(100.0)
                .throttlingRateLimit(100)
                .build())
            .build();

        feedbackTable.grantReadWriteData(apiFunction);
    }
}
