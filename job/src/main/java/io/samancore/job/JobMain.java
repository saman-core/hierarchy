package io.samancore.job;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.samancore.job.process.JsonProcess;
import jakarta.inject.Inject;

@QuarkusMain
public class JobMain implements QuarkusApplication {

    @Inject
    JsonProcess jsonProcess;

    @Override
    public int run(String... args) {
        try {
            jsonProcess.run();
            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}
