package fr.pacman.back.core.ui.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.part.ViewPart;

import fr.pacman.back.core.ui.generator.PacmanUIGeneratorHelper;
import fr.pacman.back.core.validation.PacmanValidationRow;

/**
 * Vue Eclipse affichant un rapport de validation sous forme de tableau.
 * 
 * Cette vue liste les erreurs détectées lors de la validation des données, avec
 * 4 colonnes principales : Règle, Objet, Erreur rencontrée, Solution
 * potentielle. Elle permet de filtrer les résultats, de trier chaque colonne,
 * et de naviguer vers l’élément métier correspondant dans la représentation
 * Sirius associée.
 * 
 * Le chemin relatif vers le fichier des représentations (fichier .aird) doit
 * être fourni via {@link #setRepresentations(String)} pour permettre la
 * navigation.
 * 
 * La navigation vers un élément valide automatiquement l’ouverture de la
 * représentation contenant cet élément, puis surligne la partie graphique
 * correspondante.
 * 
 * <b>Remarques techniques :</b>
 * 
 * <ul>
 * <li>Utilise Sirius pour gérer les sessions et représentations
 * graphiques.</li>
 * <li>Gère la résolution des proxys EMF pour la navigation.</li>
 * <li>Utilise un TableViewer SWT avec filtres et tri.</li>
 * </ul>
 * 
 * @author MINARM
 */
public class PacmanUIValidationView extends ViewPart {

	/**
	 * Le TableViewer principal affichant le tableau des résultats de validation.
	 */
	private TableViewer _viewer;

	/**
	 * L'identifiant de la vue pour afficher la liste des erreurs rencontrées au
	 * niveau du diagramme de modélisation.
	 */
	public final static String c_view_validation_id = "fr.pacman.validation.ui.core.ValidationView";

	/**
	 * Identifiant de la vue pour le model explorer.
	 */
	private static final String c_view_explorer_id = "org.eclipse.sirius.ui.tools.views.model.explorer";

	/**
	 * Pourcentage de largeur de chaque colonne par rapport à la largeur totale du
	 * tableau. Les valeurs doivent totaliser 100.
	 */
	private int[] _COLUMNS_PERCENTS = { 10, 30, 20, 39 };

	/**
	 * Liste des lignes de validation affichées dans le tableau. Chaque
	 * ValidationRow représente une erreur ou avertissement détecté.
	 */
	private List<PacmanValidationRow> _rows;

	/**
	 * Titre de la vue affiché dans l'onglet Eclipse, avec le nombre de lignes.
	 */
	private static final String TITLE = "Rapport de validation";

	/**
	 * Tableau des zones de texte utilisées pour filtrer dynamiquement le contenu de
	 * chaque colonne. Une case par colonne (Règle, Objet, Erreur, Solution).
	 */
	private Text[] _filterTexts = new Text[4];

	/**
	 * Lec chemin relatif utilisé pour créer le chemin de chargement du fichier
	 * contenenant l'ensemble des représentations (représentations.aird).
	 */
	private String _representations;

	/**
	 * Constructeur par défaut.
	 */
	public PacmanUIValidationView() {
		super();
	}

	/**
	 * Initialise la vue et ses composants graphiques (tableau, colonnes, filtres).
	 * 
	 * @param p_parent Composite parent pour le layout de la vue.
	 */
	@Override
	public void createPartControl(Composite p_parent) {
		Composite container = new Composite(p_parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		_viewer = new TableViewer(container, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		_viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createColumns();
		Table table = _viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// Supprime la sélection native (évite le conflit visuel avec la surbrillance)
		_viewer.getTable().deselectAll();
		_viewer.setContentProvider(ArrayContentProvider.getInstance());

		_viewer.addSelectionChangedListener(event -> {
			IStructuredSelection selection = _viewer.getStructuredSelection();
			PacmanValidationRow row = (PacmanValidationRow) selection.getFirstElement();

			if (row != null) {
				clearHighlights();
				highlightSelectedRow(row);
				navigateToElement(row);
			}
		});

		container.addListener(SWT.Resize, e -> {
			int totalWidth = container.getClientArea().width;
			TableColumn[] columns = _viewer.getTable().getColumns();
			for (int i = 0; i < columns.length; i++) {
				int colWidth = totalWidth * _COLUMNS_PERCENTS[i] / 100;
				columns[i].setWidth(colWidth);
			}
		});

		createFilterRow(container);
		refreshViewTitle();
	}

	/**
	 * Réinitialise la couleur de fond de toutes les lignes du tableau, supprimant
	 * ainsi toute surbrillance précédente.
	 */
	private void clearHighlights() {
		for (TableItem item : _viewer.getTable().getItems()) {
			item.setBackground(null);
			item.setForeground(null);
		}
	}

	/**
	 * Met en surbrillance la ligne correspondant à l'objet
	 * {@link PacmanValidationRow} spécifié dans le tableau affiché par le
	 * {@link TableViewer}.
	 * 
	 * Cette méthode parcourt tous les éléments du tableau et applique une couleur
	 * de fond bleue à la ligne correspondant à l'objet donné. Elle force ensuite un
	 * redessin du tableau pour garantir l'affichage visuel de la surbrillance.
	 *
	 * @param p_row la ligne de validation à mettre en surbrillance ; si aucune
	 *              ligne ne correspond, aucune action n'est effectuée.
	 */
	private void highlightSelectedRow(final PacmanValidationRow p_row) {
		for (TableItem item : _viewer.getTable().getItems()) {
			if (item.getData() == p_row) {
				item.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				// item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				break;
			}
		}
	}

	/**
	 * Met à jour le titre de la vue pour afficher le nombre de lignes affichées.
	 */
	private void refreshViewTitle() {
		if (_rows != null)
			setPartName(TITLE + " (" + _rows.size() + " ligne(s))");
	}

	/**
	 * Initialise les colonnes du {@link TableViewer} avec les titres, les
	 * fournisseurs d'étiquettes (label providers) et les comportements de tri.
	 * <p>
	 * Chaque colonne correspond à un aspect d'une ligne de validation :
	 * <ul>
	 * <li>Règle</li>
	 * <li>Objet</li>
	 * <li>Erreur rencontrée</li>
	 * <li>Solution potentielle</li>
	 * </ul>
	 * 
	 * Les colonnes sont triables en cliquant sur leur en-tête. Le tri alterne entre
	 * ordre croissant et décroissant.
	 * 
	 * Le tri est réalisé via un {@link ViewerComparator} qui utilise des
	 * {@link Comparator} spécifiques à chaque colonne, en traitant proprement les
	 * valeurs nulles.
	 */
	private void createColumns() {
		String[] titles = { "Règle", "Objet", "Erreur rencontrée", "Solution potentielle" };

		@SuppressWarnings("unchecked")
		Comparator<PacmanValidationRow>[] comparators = (Comparator<PacmanValidationRow>[]) new Comparator[] {
				Comparator.comparing((PacmanValidationRow r) -> r._rule == null ? "" : r._rule),
				Comparator.comparing((PacmanValidationRow r) -> r._objectName == null ? "" : r._objectName),
				Comparator.comparing((PacmanValidationRow r) -> r._error == null ? "" : r._error),
				Comparator.comparing((PacmanValidationRow r) -> r._solution == null ? "" : r._solution) };

		for (int i = 0; i < titles.length; i++) {
			final int columnIndex = i;
			TableViewerColumn col = new TableViewerColumn(_viewer, SWT.NONE);
			TableColumn tableCol = col.getColumn();
			tableCol.setText(titles[columnIndex]);
			tableCol.setWidth(100);
			col.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					PacmanValidationRow row = (PacmanValidationRow) element;
					return switch (columnIndex) {
					case 0 -> row._rule;
					case 1 -> row._objectName;
					case 2 -> row._error;
					case 3 -> row._solution;
					default -> "";
					};
				}
			});
			final int[] dir = { SWT.UP };
			tableCol.addListener(SWT.Selection, e -> {
				Table table = _viewer.getTable();
				TableColumn currentSortColumn = table.getSortColumn();
				if (currentSortColumn == tableCol) {
					dir[0] = table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					dir[0] = SWT.UP;
				}
				table.setSortColumn(tableCol);
				table.setSortDirection(dir[0]);
				_viewer.setComparator(new ViewerComparator() {
					@Override
					public int compare(Viewer viewer, Object e1, Object e2) {
						int result = comparators[columnIndex].compare((PacmanValidationRow) e1,
								(PacmanValidationRow) e2);
						return dir[0] == SWT.UP ? result : -result;
					}
				});
			});
		}
	}

	/**
	 * Crée une rangée de filtres (zones de texte) permettant de filtrer les lignes
	 * selon le contenu des colonnes (règle, objet, erreur, solution). Le filtre
	 * agit dynamiquement à chaque modification du texte.
	 * 
	 * @param p_parent Composite parent dans lequel ajouter la rangée de filtres.
	 */
	@SuppressWarnings("unused")
	private void createFilterRow(Composite p_parent) {
		Composite filterComposite = new Composite(p_parent, SWT.NONE);
		filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		filterComposite.setLayout(new GridLayout(4, false));

		String[] hints = { "Filtrer par règle", "Filtrer par objet", "Filtrer par erreur", "Filtrer par solution" };

		for (int i = 0; i < 4; i++) {
			final int index = i;
			Text text = new Text(filterComposite, SWT.SEARCH | SWT.ICON_CANCEL);
			text.setMessage(hints[i]);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			text.addModifyListener(e -> _viewer.refresh());
			_filterTexts[i] = text;
		}

		_viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				PacmanValidationRow row = (PacmanValidationRow) element;
				String[] values = { row._rule == null ? "" : row._rule.toLowerCase(),
						row._objectName == null ? "" : row._objectName.toLowerCase(),
						row._error == null ? "" : row._error.toLowerCase(),
						row._solution == null ? "" : row._solution.toLowerCase() };
				for (int i = 0; i < _filterTexts.length; i++) {
					String filterText = _filterTexts[i].getText().toLowerCase().trim();
					if (!filterText.isEmpty() && !values[i].contains(filterText)) {
						return false;
					}
				}
				return true;
			}
		});
	}

	/**
	 * Tente de naviguer automatiquement vers l’élément {@link EObject} associé à
	 * une ligne de validation, en ouvrant la représentation graphique Sirius qui le
	 * contient (le cas échéant), puis en le sélectionnant visuellement.
	 * 
	 * Par souci de simplification (et quelques autres problèmes) la session sirius
	 * est chargée uniquement à ce niveau ce qui signifie que les ressources sont
	 * chargées dans deux resourcesets distincts -> il est impossible de faire des
	 * égalités (==) entre les objets puisque la référence mémoire entre aussi en
	 * jeu. Dans notre cas cela n'est pas grave on se contente d'une égalité entre
	 * les identifiants techniques des eObjects (ces identifiants sont uniques).
	 * 
	 * Cette méthode :
	 * <ul>
	 * <li>Vide la sélection courante dans la vue (TableViewer)</li>
	 * <li>Récupère la session Sirius liée au fichier de représentations
	 * (.aird)</li>
	 * <li>Recherche la {@link DRepresentation} (ex: diagramme) qui contient
	 * l'élément cible</li>
	 * <li>Ouvre l'éditeur Sirius correspondant si trouvé</li>
	 * <li>Sélectionne l’élément dans le diagramme via
	 * {@code selectAndRevealInDiagram}</li>
	 * </ul>
	 * Si aucune session ou représentation n’est trouvée, un message d’erreur est
	 * affiché via une pop-up.
	 * </p>
	 *
	 * @param p_row La ligne de validation contenant l’élément EMF cible à localiser
	 *              dans une représentation.
	 *
	 * @see org.eclipse.sirius.viewpoint.DRepresentation
	 * @see org.eclipse.sirius.ui.business.api.dialect.DialectUIManager#openEditor
	 * @see #selectAndRevealInDiagram(EObject, DialectEditor)
	 * @see org.eclipse.emf.ecore.resource.ResourceSet
	 */
	private void navigateToElement(PacmanValidationRow p_row) {

		// Vide la précédente sélection.
		_viewer.setSelection(new StructuredSelection());

		EObject target = p_row._emfElement;
		if (target == null) {
			PacmanUIGeneratorHelper.displayPopUpAlert("Aucun eObject associé pour la ligne.");
			return;
		}

		URI sessionURI = URI.createPlatformResourceURI(_representations, true);
		Session session = SessionManager.INSTANCE.getSession(sessionURI, new NullProgressMonitor());

		if (session == null) {
			try {
				session = SessionManager.INSTANCE.openSession(sessionURI, new NullProgressMonitor(), null);
			} catch (Exception e) {
				PacmanUIGeneratorHelper
						.displayPopUpAlert("Erreur lors de l'ouverture de la session: " + e.getMessage());
				return;
			}
		}

		if (session == null) {
			PacmanUIGeneratorHelper.displayPopUpAlert("Session introuvable pour cet élément.");
			return;
		}

		// Collection<DRepresentation> allRepresentations =
		// DialectManager.INSTANCE.getAllRepresentations(session);
		Collection<DRepresentation> allRepresentations = getAllRepresentations(session);
		DRepresentation containingRepresentation = null;

		for (DRepresentation rep : allRepresentations) {
			if (representationContainsTarget(rep, target)) {
				containingRepresentation = rep;
				break;
			}
		}

		if (containingRepresentation != null) {
			IEditorPart editorPart = DialectUIManager.INSTANCE.openEditor(session, containingRepresentation,
					new NullProgressMonitor());

			Display.getDefault().asyncExec(() -> {
				if (editorPart instanceof DialectEditor dialectEditor) {
					selectAndRevealInDiagram(target, dialectEditor);
				}
			});
		} else {
			PacmanUIGeneratorHelper
					.displayPopUpAlert("Impossible de retrouver automatiquement la représentation pour : "
							+ (String) target.eGet(target.eClass().getEStructuralFeature("name"))
							+ "\n\n1) Vérifiez que la représentation a bien été créée."
							+ "\n2) Essayez d'ouvrir manuellement la représentation associée.");
		}
	}

	/**
	 * Récupère toutes les représentations ({@link DRepresentation}) disponibles
	 * dans une session Sirius donnée, en parcourant récursivement tous les objets
	 * du modèle sémantique (y compris les namespaces imbriqués).
	 * <p>
	 * Cette méthode parcourt :
	 * <ul>
	 * <li>Toutes les ressources sémantiques de la session,</li>
	 * <li>Tous les objets racines et leurs contenus,</li>
	 * <li>Et agrège toutes les représentations associées via
	 * {@link DialectManager#getRepresentations(EObject, Session)}.</li>
	 * </ul>
	 *
	 * @param p_session la session Sirius active
	 * @return la collection de toutes les représentations trouvées
	 */
	public static Collection<DRepresentation> getAllRepresentations(Session p_session) {
		List<DRepresentation> reps = new ArrayList<>();
		for (Resource res : p_session.getSemanticResources()) {
			for (EObject root : res.getContents()) {
				reps.addAll(getRepresentationsFromEObject(root, p_session));
			}
		}
		return reps;
	}

	/**
	 * Récupère récursivement toutes les représentations ({@link DRepresentation})
	 * associées à un objet sémantique et à ses enfants.
	 * <p>
	 * Cette méthode appelle le {@link DialectManager} pour collecter les
	 * représentations attachées directement à l'objet courant, puis descend dans
	 * son contenu pour récupérer celles des sous-objets.
	 *
	 * @param p_eObj    l'objet sémantique à analyser
	 * @param p_session la session Sirius active
	 * @return la collection de représentations trouvées pour l'objet et ses
	 *         descendants
	 */
	private static Collection<DRepresentation> getRepresentationsFromEObject(EObject p_eObj, Session p_session) {
		List<DRepresentation> reps = new ArrayList<>();
		reps.addAll(DialectManager.INSTANCE.getRepresentations(p_eObj, p_session));
		for (EObject child : p_eObj.eContents()) {
			reps.addAll(getRepresentationsFromEObject(child, p_session));
		}
		return reps;
	}

	/**
	 * Sélectionne et révèle dans un éditeur Sirius tous les éléments de
	 * représentation ({@link DRepresentationElement}) qui pointent vers un objet
	 * métier donné.
	 * 
	 * Cette méthode parcourt tous les éléments de la représentation actuelle
	 * affichée dans l'éditeur Sirius (typiquement un {@link DDiagram}), et
	 * sélectionne ceux dont la cible ({@code getTarget()}) correspond à
	 * l'{@link EObject} spécifié. La correspondance est effectuée via l'identifiant
	 * EMF unique ({@link EcoreUtil#getID(EObject)}).
	 *
	 * @param p_target        L'objet métier à localiser dans le diagramme (ex : une
	 *                        entité, un attribut, etc.).
	 * @param p_dialectEditor L'éditeur Sirius affichant une représentation
	 *                        graphique (diagramme, table, etc.) dans laquelle
	 *                        rechercher l'objet à sélectionner.
	 *
	 * @see DRepresentation
	 * @see DRepresentationElement
	 * @see DialectUIManager#selectAndReveal(IEditorPart, java.util.Collection)
	 * @see EcoreUtil#getID(EObject)
	 */
	private void selectAndRevealInDiagram(EObject p_target, DialectEditor p_dialectEditor) {
		DRepresentation representation = p_dialectEditor.getRepresentation();
		List<DRepresentationElement> matches = new ArrayList<>();

		TreeIterator<EObject> it = representation.eAllContents();
		while (it.hasNext()) {
			EObject obj = it.next();
			if (obj instanceof DRepresentationElement) {
				DRepresentationElement dElement = (DRepresentationElement) obj;
				String elementId = EcoreUtil.getID(dElement.getTarget());
				String targetId = EcoreUtil.getID(p_target);
				if (elementId.equals(targetId)) {
					matches.add(dElement);
				}
			}
		}
		if (!matches.isEmpty()) {
			DialectUIManager.INSTANCE.selectAndReveal(p_dialectEditor, matches);
		}
	}

	/**
	 * Vérifie si la représentation donnée contient l'élément métier cible.
	 * Recherche parmi tous les décorateurs sémantiques la correspondance par ID EMF
	 * ou instance.
	 * 
	 * @param rep    Représentation Sirius dans laquelle chercher
	 * @param target Élément métier EMF recherché
	 * @return true si la représentation contient l'élément, false sinon
	 */
	private boolean representationContainsTarget(DRepresentation rep, EObject target) {
		if (target == null) {
			return false;
		}

		String targetId = EcoreUtil.getID(target);
		if (targetId == null) {
			// Pas d'ID défini, fallback à comparaison par instance
			return eAllContentsContainsEObject(rep, target);
		}

		Iterator<EObject> it = rep.eAllContents();
		while (it.hasNext()) {
			EObject obj = it.next();
			if (obj instanceof DSemanticDecorator decorator) {
				EObject semantic = decorator.getTarget();
				if (semantic != null) {
					String semanticId = EcoreUtil.getID(semantic);
					if (targetId.equals(semanticId)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Parcourt le contenu de la représentation pour vérifier la présence de
	 * l'élément métier par comparaison directe (utilisé en fallback quand aucun ID
	 * EMF n'est défini).
	 * 
	 * @param rep    Représentation Sirius à parcourir
	 * @param target Élément métier EMF recherché
	 * @return true si trouvé, false sinon
	 */
	private boolean eAllContentsContainsEObject(DRepresentation rep, EObject target) {
		Iterator<EObject> it = rep.eAllContents();
		while (it.hasNext()) {
			EObject obj = it.next();
			if (obj instanceof DSemanticDecorator decorator) {
				EObject semantic = decorator.getTarget();
				if (semantic != null && semantic.equals(target)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Prépare l’environnement de la vue de validation en activant la
	 * synchronisation entre l’éditeur et l’explorateur de modèles.
	 * 
	 * Cette méthode tente d’ouvrir la vue de l’explorateur de modèles Sirius
	 * (identifiée par {@code EXPLORER_VIEW_ID}) si elle n’est pas déjà ouverte,
	 * puis active l’option "Link with Editor" pour que la sélection dans l’éditeur
	 * principal soit reflétée automatiquement dans l’explorateur.
	 *
	 * @param p_page La page de travail Eclipse active dans laquelle ouvrir et
	 *               configurer la vue.
	 */
	public void setLinkingEnabled(final IWorkbenchPage p_page) {
		Display.getDefault().asyncExec(() -> {
			IViewPart view = p_page.findView(c_view_explorer_id);
			if (view == null) {
				try {
					view = p_page.showView(c_view_explorer_id);
				} catch (Exception e) {
					PacmanUIGeneratorHelper
							.displayPopUpAlert("Impossible de trouver la vue pour l'explorateur de modèles.");
					return;
				}
			}
			if (view instanceof CommonNavigator navigator)
				navigator.setLinkingEnabled(true);
		});
	}

	/**
	 * Donne le focus au contrôle principal de la vue (tableau).
	 */
	@Override
	public void setFocus() {
		_viewer.getControl().setFocus();
	}

	/**
	 * Définit le chemin relatif vers le fichier des représentations Sirius (.aird).
	 * Ce chemin est utilisé pour ouvrir la session et retrouver les
	 * représentations.
	 * 
	 * @param p_representations chemin relatif vers le fichier .aird
	 */
	public void setRepresentations(String p_representations) {
		this._representations = p_representations;
	}

	/**
	 * Met à jour la liste des lignes à afficher dans la vue. Chaque ligne
	 * représente une erreur ou un avertissement de validation.
	 * 
	 * @param p_rows Liste des ValidationRow à afficher
	 */
	public void setRows(final List<PacmanValidationRow> p_rows) {
		this._rows = p_rows;
		if (_viewer != null && !_viewer.getTable().isDisposed()) {
			_viewer.setInput(p_rows);
			refreshViewTitle();
		}
	}
}
