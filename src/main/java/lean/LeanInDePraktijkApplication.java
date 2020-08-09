package lean;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LeanInDePraktijkApplication extends Application<LeanInDePraktijkConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LeanInDePraktijkApplication().run(args);
    }

    @Override
    public String getName() {
        return "LeanInDePraktijk";
    }

    @Override
    public void initialize(final Bootstrap<LeanInDePraktijkConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final LeanInDePraktijkConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
