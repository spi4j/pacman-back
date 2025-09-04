package fr.pacman.back.core.generator;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * 
 * @author MINARM
 */
public abstract class PacmanGeneratorStart extends PacmanGenerator {

	/**
	 * 
	 */
	private boolean fromStarter;

	/**
	 * 
	 * @param p_targetFolder
	 * @throws IOException
	 */
	public void setModelFile(final File p_targetFolder) throws IOException {
		setResources(Collections.emptyList());
		setRootPath(p_targetFolder.getAbsolutePath());
		setFromStarter(true);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFromStarter() {
		return fromStarter;
	}

	/**
	 * 
	 * @param fromStarter
	 */
	public void setFromStarter(boolean fromStarter) {
		this.fromStarter = fromStarter;
	}
}
