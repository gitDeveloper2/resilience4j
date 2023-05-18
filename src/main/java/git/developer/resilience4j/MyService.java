package git.developer.resilience4j;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public void data(String data) throws InterruptedException {
        System.out.println("Starting");
        Thread.sleep(10000);
        System.out.println("Exiting");

    }
}
