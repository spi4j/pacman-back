package fr.pacman.start.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Cette classe représente une vue personnalisée dans Eclipse qui permet
 * d'afficher une page HTML dans un composant `Browser` intégré à Eclipse. Elle
 * hérite de `ViewPart` et est conçue pour afficher une page HTML à partir d'une
 * URL spécifiée. L'URL de la page à afficher peut être définie statiquement via
 * la méthode `setHtmlUrlToLoad()`.
 * 
 * La vue est rendue via un composant `Browser` d'Eclipse qui peut afficher des
 * pages HTML locales ou distantes. Si le composant `Browser` n'est pas
 * disponible sur la plateforme, un fallback est utilisé pour éviter les erreurs
 * fatales.
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class HtmlViewerView extends ViewPart {

	/**
	 * L'ID unique de la vue, utilisé pour l'enregistrement et la référence dans
	 * Eclipse.
	 */
	public static final String c_id = "fr.pacman.start.ui.views.HtmlViewerView";
	/**
	 * L'URL de la page HTML à charger dans le composant `Browser`. Cette URL est
	 * définie via la méthode statique {@link #setHtmlUrlToLoad(String)}.
	 */
	private static String _htmlUrlToLoad;

	/**
	 * Le composant `Browser` utilisé pour afficher le contenu HTML dans cette vue.
	 */
	private Browser _browser;

	/**
	 * Crée le composant `Browser` et charge l'URL spécifiée dans `htmlUrlToLoad`.
	 * Si l'URL est définie, la page HTML correspondante est affichée dans le
	 * `Browser`. Si le `Browser` n'est pas disponible (par exemple, sur certaines
	 * plateformes), un fallback est utilisé et le `browser` est défini sur `null`.
	 *
	 * @param p_parent Le composant parent auquel ce composant `Browser` est
	 *                 attaché.
	 */
	@Override
	public void createPartControl(Composite p_parent) {
		try {
			_browser = new Browser(p_parent, SWT.NONE);
			if (_htmlUrlToLoad != null) {
				_browser.setUrl(_htmlUrlToLoad);
			}
		} catch (Throwable e) {
			_browser = null;
		}
	}

	/**
	 * Définit le focus sur le composant `Browser` si celui-ci n'est pas déjà
	 * disposé. Cela permet de garantir que la vue est interactive lorsque
	 * l'utilisateur clique dessus.
	 */
	@Override
	public void setFocus() {
		if (_browser != null && !_browser.isDisposed()) {
			_browser.setFocus();
		}
	}

	/**
	 * Définit l'URL de la page HTML à afficher dans la vue. Cette URL peut être une
	 * adresse locale ou distante, et elle sera chargée dans le composant `Browser`
	 * lors de la création de la vue.
	 *
	 * @param url L'URL de la page HTML à charger dans la vue.
	 */
	public static void setHtmlUrlToLoad(String p_url) {
		_htmlUrlToLoad = p_url;
	}
}
