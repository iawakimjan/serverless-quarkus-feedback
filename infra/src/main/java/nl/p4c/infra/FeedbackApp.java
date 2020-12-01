package nl.p4c.infra;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class FeedbackApp {
    public static void main(final String[] args) {
        App app = new App();

        new FeedbackStack(app, "FeedbackStack");

        app.synth();
    }
}
