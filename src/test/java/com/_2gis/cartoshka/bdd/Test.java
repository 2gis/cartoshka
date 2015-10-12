package com._2gis.cartoshka.bdd;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

public class Test extends JUnitStories {
    @Override
    public final Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .usePendingStepStrategy(new FailingUponPendingStep())
                .useStoryReporterBuilder(new StoryReporterBuilder().withDefaultFormats()
                        .withFormats(Format.CONSOLE));
    }

    @Override
    public final List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "test.story", "");
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new Steps());
    }
}