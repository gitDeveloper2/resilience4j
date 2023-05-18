package git.developer.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Controller
public class MyController {
    @Autowired
    MyService service;
    @GetMapping("/data")
    public String data() throws InterruptedException {

        CircuitBreakerConfig config=CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(4)
                .slowCallRateThreshold(2.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(1))
                .build();
        CircuitBreakerRegistry registry=CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker=registry.circuitBreaker("cbk");
        Consumer<String> konsumer=(data)->{
            try {
                service.data("nothing");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        };
        Consumer<String> cKonsumer=circuitBreaker.decorateConsumer(konsumer);
        for (int i=0;i<10;i++) {
            cKonsumer.accept("nothing");
        }
        return "Done";
    }

}
