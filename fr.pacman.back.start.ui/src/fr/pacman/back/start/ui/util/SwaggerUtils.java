package fr.pacman.back.start.ui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import fr.pacman.back.start.plugin.Activator;

/**
 * Utilitaire pour copier les fichiers nécessaires à l'interface Swagger UI
 * depuis les ressources embarquées du plugin vers un répertoire cible du
 * système de fichiers.
 * <p>
 * Les fichiers copiés incluent le HTML, les fichiers CSS/JS, les icônes et les
 * fichiers de configuration nécessaires au bon fonctionnement de Swagger UI.
 *
 * Ce composant est utile dans un contexte où l'on souhaite déployer ou rendre
 * accessible Swagger UI à partir d'un plugin Eclipse ou d'une application
 * packagée.
 *
 * Exemple d'utilisation :
 * 
 * <pre>{@code
 * SwaggerUtils utils = new SwaggerUtils();
 * utils.copyFiles("mon/dossier/de/destination");
 * }</pre>
 */
public class SwaggerUtils {

	private final ILog logger;

	/**
	 * Liste des fichiers Swagger UI à copier. Les chemins sont relatifs au
	 * répertoire {@code /resources/src/main/webapp/swagger/} contenu dans le
	 * classpath du plugin.
	 */
	private static final List<String> c_SWAGGER_FILES = Arrays.asList("src/main/webapp/swagger/favicon-16x16.png",
			"src/main/webapp/swagger/favicon-32x32.png", "src/main/webapp/swagger/index.html",
			"src/main/webapp/swagger/index.css", "src/main/webapp/swagger/oauth2-redirect.html",
			"src/main/webapp/swagger/swagger-initializer.js", "src/main/webapp/swagger/swagger-ui.css",
			"src/main/webapp/swagger/swagger-ui.css.map", "src/main/webapp/swagger/swagger-ui.js",
			"src/main/webapp/swagger/swagger-ui.js.map", "src/main/webapp/swagger/swagger-ui-bundle.js",
			"src/main/webapp/swagger/swagger-ui-bundle.js.map", "src/main/webapp/swagger/swagger-ui-es-bundle.js",
			"src/main/webapp/swagger/swagger-ui-es-bundle.js.map",
			"src/main/webapp/swagger/swagger-ui-es-bundle-core.js",
			"src/main/webapp/swagger/swagger-ui-es-bundle-core.js.map",
			"src/main/webapp/swagger/swagger-ui-standalone-preset.js",
			"src/main/webapp/swagger/swagger-ui-standalone-preset.js.map");

	/**
	 * Constructeur avec injection du logger Eclipse.
	 *
	 * @param logger instance de {@link ILog}, typiquement obtenue via
	 *               Activator.getDefault().getLog()
	 */
	public SwaggerUtils(ILog logger) {
		this.logger = logger;
	}

	/**
	 * Copie l'ensemble des fichiers Swagger UI embarqués dans les ressources du
	 * plugin vers le répertoire cible spécifié.
	 *
	 * @param p_target chemin du dossier de destination où les fichiers Swagger
	 *                 seront copiés. Ce chemin peut être relatif ou absolu.
	 * @throws IOException en cas d'erreur d'accès aux fichiers (lecture/écriture).
	 */
	public void copyFiles(final String p_target) throws IOException {
		for (final String v_file : c_SWAGGER_FILES) {
			String fileName = Path.of(v_file).getFileName().toString();
			Path destination = Path.of(p_target + File.separator + fileName);
			String in = "/resources/" + v_file; // Pourquoi le File.separator ne fonctionne pas ?
			InputStream source = getClass().getResourceAsStream(in);
			if (source == null) {
				logger.log(new Status(IStatus.ERROR, Activator.c_pluginId, "Fichier non trouvé : " + fileName));
				return;
			}
			Files.createDirectories(destination.getParent());
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
		}
	}
}