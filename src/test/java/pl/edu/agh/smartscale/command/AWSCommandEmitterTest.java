package pl.edu.agh.smartscale.command;


import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AWSCommandEmitterTest {

    @Test
    public void shouldEmitPositiveValueOnScaleUp() throws Exception {
        // given
        AmazonCloudWatch clientMock = mock(AmazonCloudWatch.class);
        AWSCommandEmitter emitter = new AWSCommandEmitter(clientMock);

        // when
        emitter.emit(Command.SCALE_UP);

        // then
        verify(clientMock).putMetricData(Matchers.any(PutMetricDataRequest.class));
    }
}