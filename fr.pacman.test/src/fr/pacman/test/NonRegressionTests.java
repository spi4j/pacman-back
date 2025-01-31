package fr.pacman.test;

import java.io.File;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import fr.pacman.core.generator.PacmanGenerator;

public class NonRegressionTests extends AbstractNonRegressionTests {

	public NonRegressionTests(Class<? extends PacmanGenerator> p_cls, File p_modelFolder, File p_model) {
		super(p_cls, p_modelFolder, p_model);
	}

	@Parameters(name = "{0}-{1}")
	public static Collection<Object[]> retrieveTests() {
		return retrieveTests("resources");
	}
}
