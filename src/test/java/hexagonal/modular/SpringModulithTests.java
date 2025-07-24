package hexagonal.modular;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SpringModulithTests {
    ApplicationModules modules = ApplicationModules.of(StartApplication.class);

    @Test
    void shouldBeCompliant() {
        modules.verify();
    }

    @Test
    void writeDocumentationSnippets() {

        // This test is used to generate the documentation snippets for the modules.
        assertDoesNotThrow(() -> {
            Documenter documenter = new Documenter(modules);
            documenter.writeModulesAsPlantUml().writeIndividualModulesAsPlantUml().writeModuleCanvases().writeAggregatingDocument();
        });
    }
}