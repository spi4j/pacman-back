package fr.pacman.start.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * Classe abstraite déstinée à faciliter la création du/des formulaire(s) pour
 * la création d'un nouveau projet Pacman. Définit des composants SWT plus
 * facilement pilotables pour le dévelopeur.
 * 
 * @author MINARM
 *
 * @param <T>
 */
abstract class PropertiesWizardPage<T extends Control> extends WizardPage {

	private static final String c_labelOffset = "     ";

	/**
	 * La liste de l'ensemble des composants de saisie à gérer.
	 */
	private Map<String, T> _lstWidgets = new HashMap<>();

	/**
	 * La liste des composants spécifiques à 'disposer' expressement (sinon message
	 * d'erreur dans la console, voir plus tard si solution plus élégante).
	 */
	private List<Resource> _lstResourcesToDispose = new ArrayList<>();

	/**
	 * Constructeur.
	 * 
	 * @param pageName le nom à afficher dans l'en-tête de la page.
	 */
	PropertiesWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * 
	 * @param p_resource
	 */
	void registerResourceToDispose(final Resource p_resource) {
		_lstResourcesToDispose.add(p_resource);
	}

	/**
	 * Demande d'enregistrement du composant de saisie.
	 * 
	 * @param p_key    la clé unique, identifiant du composant
	 * @param p_widget le composant
	 */
	void registerWidget(final String p_key, T p_widget) {
		_lstWidgets.put(p_key, p_widget);
	}

	/**
	 * Retourne le composant de saisie à partir de son identifiant unique.
	 * 
	 * @param p_key la clé unique pour le composant de saisie.
	 * @return le composant de saisie.
	 */
	T getWidget(final String p_key) {
		return _lstWidgets.get(p_key);
	}

	/**
	 * Active ou désactive un {@link Widget} ( ou un groupe de {@link Widget}). On
	 * récupère le label associé au {@link Widget} grâce à la méthode 'getData()'.
	 * 
	 * @param p_widget l'élément ou le groupe d'éléments à activer ou désactiver.
	 * @param p_enable 'true' pour une demande d'activation, sinon 'false'.
	 */
	@SuppressWarnings("unchecked")
	private void enable(final T p_widget, final boolean p_enable) {
		if (p_widget instanceof Composite) {
			for (Control control : ((Composite) p_widget).getChildren()) {
				if (control instanceof Composite)
					enable((T) control, p_enable);
				if (!(control instanceof Label))
					control.setEnabled(p_enable);
				if (control instanceof Button)
					((Button) control).setSelection(false);
			}
			return;
		}
		p_widget.setEnabled(p_enable);
		// ((Label) p_widget.getData()).setEnabled(p_enable);
		if (p_widget instanceof Button)
			((Button) p_widget).setSelection(false);
	}

	/**
	 * Demande l'activation pour un élément.
	 * 
	 * @param p_widget l'élément ou le groupe d'éléments à activer.
	 */
	void enable(final T p_widget) {
		enable(p_widget, true);
	}

	/**
	 * Demande la désactivation pour un élément.
	 * 
	 * @param p_widget l'élément ou le groupe d'éléments à désactiver.
	 */
	void disable(final T p_widget) {
		enable(p_widget, false);
	}

	void setVisible(final T p_widget, final boolean p_enable) {
		p_widget.setVisible(p_enable);
	}

	/**
	 * Création d'un conteneur de type 'onglet'.
	 * 
	 * @param p_parent  le conteneur parent.
	 * @param p_tabName le titre à afficher pour l'onglet.
	 * @param p_layout  le layout spécifique pour l'onglet.
	 * @return le conteneur de type 'onglet'.
	 */
	Composite addTabItem(final TabFolder p_parent, final String p_tabItemName) {
		final TabItem tabItem = new TabItem(p_parent, SWT.NONE);
		final Composite pageItem = new Composite(p_parent, SWT.NONE);
		tabItem.setText(p_tabItemName);
		tabItem.setControl(pageItem);
		pageItem.setLayout(new GridLayout(1, false));
		// pageItem.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		return pageItem;
	}

	/**
	 * Création d'un conteneur de type 'onglet' avec un panneau de type ''.
	 * 
	 * @param p_parent   le conteneur parent.
	 * @param p_tabName  le titre à afficher pour l'onglet.
	 * @param p_layout   le layout spécifique pour l'onglet.
	 * @param p_minHeigh la taille minimum du panneau avant que les barres de
	 *                   défilement apparaissent.
	 * @return
	 */
	Composite addScrollTabItem(final TabFolder p_parent, final String p_tabItemName, final int p_minHeigh) {
		final TabItem tabItem = new TabItem(p_parent, SWT.NONE);
		final ScrolledComposite scroller = new ScrolledComposite(p_parent, SWT.V_SCROLL);
		scroller.setLayout(new FillLayout());
		Composite pageItem = new Composite(scroller, SWT.NONE);
		pageItem.setLayout(new GridLayout(1, false));
		tabItem.setText(p_tabItemName);
		scroller.setContent(pageItem);
		scroller.setOrigin(pageItem.getSize());
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setAlwaysShowScrollBars(true);
		tabItem.setControl(scroller);
		return pageItem;
	}

	/**
	 * Création d'un conteneur de type 'group' avec une légende.
	 * 
	 * @param p_parent  le conteneur parent.
	 * @param p_title   le titre à afficher pour le groupement.
	 * @param p_tooltip la description (si fournie).
	 * @return le conteneur pour le groupe.
	 */
	Group addGroup(final Composite p_parent, final String p_title, final String p_tooltip) {
		return addGroup(p_parent, p_title, p_tooltip, new GridLayout(2, false));
	}

	/**
	 * Création d'un conteneur de type 'group' avec une légende.
	 * 
	 * @param p_parent  le conteneur parent.
	 * @param p_title   le titre à afficher pour le groupement.
	 * @param p_tooltip la description (si fournie).
	 * @return le conteneur pour le groupe.
	 */
	Group addGroup(final Composite p_parent, final String p_title, final String p_tooltip, final GridLayout p_layout) {
		Group group = new Group(p_parent, SWT.NONE);
		group.setText(" " + p_title + " ");
		group.setFont(new Font(group.getDisplay(), new FontData("Arial", 8, SWT.BOLD)));
		group.setToolTipText(p_tooltip);
		group.setLayout(p_layout);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		registerResourceToDispose(group.getFont());
		return group;
	}

	/**
	 * Création d'un conteneur de type 'table'.
	 * 
	 * @param p_parent le conteneur parent.
	 * @param p_height la taille maximale pour la table avant apparition des
	 *                 scrollbars.
	 * @return le conteneur pour la table.
	 */
	Table addTable(final Composite p_parent, final int p_height) {
		Table table = new Table(p_parent, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = p_height;
		table.setLayoutData(data);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setHeaderBackground(new Color(81, 118, 205));
		table.setHeaderForeground(new Color(255, 255, 255));
		return table;
	}

	/**
	 * Création d'une liste de valeurs sélectionnables dans une ligne de tableau.
	 * 
	 * @param p_parent le conteneur parent.
	 * @param p_item   la ligne à laquelle rattacher le composant.
	 * @param p_column l'index dela colonne à laquelle rattacher le composant.
	 * @param p_values la liste des valeurs à injecter dans le composant.
	 * @return le composant de type {@link CCombo}.
	 */
	CCombo addTableCCombo(final Table p_parent, final TableItem p_item, final int p_column, List<String> p_values) {
		TableEditor editor = new TableEditor(p_parent);
		CCombo combo = new CCombo(p_parent, SWT.NONE);
		combo.add("XtopSup", 0);
		combo.add("XdMaj", 1);
		combo.add("String", 2);
		combo.add("Integer", 2);
		combo.add("Long", 2);
		combo.add("Float", 2);
		editor.grabHorizontal = true;
		editor.setEditor(combo, p_item, p_column);
		return combo;
	}

	/**
	 * Création d'une zone de texte dans une ligne de tableau.
	 * 
	 * @param p_parent le conteneur parent.
	 * @param p_item   la ligne à laquelle rattacher le composant.
	 * @param p_column l'index dela colonne à laquelle rattacher le composant.
	 * @return le composant de type {@link Text}.
	 */
	Text addTableText(final Table p_parent, final TableItem p_item, final int p_column) {
		TableEditor editor = new TableEditor(p_parent);
		Text txt = new Text(p_parent, SWT.NONE);
		editor.grabHorizontal = true;
		editor.setEditor(txt, p_item, p_column);
		return txt;
	}

	/**
	 * Création d'une case à cocher dans une ligne de tableau.
	 * 
	 * @param p_parent le conteneur parent.
	 * @param p_item   la ligne à laquelle rattacher le composant.
	 * @param p_column l'index dela colonne à laquelle rattacher le composant.
	 * @return le composant de type {@link Button}.
	 */
	Button addTableCheckBox(final Table p_parent, final TableItem p_item, final int p_column) {
		TableEditor editor = new TableEditor(p_parent);
		Button chk = new Button(p_parent, SWT.CHECK);
		chk.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		chk.pack();
		editor.minimumWidth = chk.getSize().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.setEditor(chk, p_item, p_column);
		registerResourceToDispose(chk.getCursor());
		return chk;
	}

	/**
	 * Création d'un champ de saisie de type {@link Text} avec son libellé.
	 * 
	 * @param p_labelValue   le libellé pour le champ de type 'texte.
	 * @param p_defaultValue la valeur par défaut pour le champ (optionnel).
	 * @param p_tooltip      une explication (description) pour le champ
	 *                       (optionnel).
	 * @return le composite comprenant un champ de saisie et son libellé.
	 */
	Text addText(final Composite p_parent, final String p_labelValue, final String p_defaultValue,
			final String p_tooltip) {
		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue + c_labelOffset);
		final Text element = new Text(p_parent, SWT.BORDER | SWT.SINGLE);
		element.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		element.setText(p_defaultValue);
		element.setData(label);
		return element;
	}

	/**
	 * Creating a borderless container for radio buttons.
	 * 
	 * @param p_parent
	 * @return
	 */
	widgetContainer addGroupRadio(final Composite p_parent, final String p_labelValue, final String p_tooltip) {

		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue);
		Composite group = new Composite(p_parent, SWT.BORDER);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.marginWidth = 5;
		fillLayout.marginHeight = 3;
		group.setLayout(fillLayout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		Button yesButton = new Button(group, SWT.RADIO);
		yesButton.setText("OUI");
		yesButton.setSelection(true);
		Button noButton = new Button(group, SWT.RADIO);
		noButton.setText("NON");
		widgetContainer container = new widgetContainer(yesButton, noButton);
		registerResourceToDispose(group.getCursor());
		return container;
	}

	/**
	 * Add an input field (with more than one line) and a label to the container.
	 * 
	 * @param p_parent
	 * @param p_labelValue
	 * @param p_defaultValue
	 * @param nbLines
	 * @param p_tooltip      An explanation for the input field (optional).
	 * @return
	 */
	Text addTextArea(final Composite p_parent, final String p_labelValue, final String p_defaultValue,
			final int p_nbLines, final String p_tooltip) {
		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue + c_labelOffset);
		final Text element = new Text(p_parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		element.setLayoutData(new GridData(GridData.FILL_BOTH));
		element.setText(p_defaultValue);
		return element;
	}

	/**
	 * Adds a Select box with a label to the container.
	 * 
	 * @param p_labelValue Label for the select box
	 * @param p_elements   List of elements to add to the list.
	 * @param p_tooltip    An explanation for the input field (optional).
	 * @return Combo
	 */
	Combo addComboBox(final Composite p_parent, final String p_labelValue, final String p_tooltip,
			final String[] p_elements, final int p_defaultSelection) {
		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue + c_labelOffset);
		Combo combo = new Combo(p_parent, SWT.BORDER);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo.setItems(p_elements);
		combo.select(p_defaultSelection);
		return combo;
	}

	/**
	 * Creation of a borderless container for the checkboxes.
	 * 
	 * @param p_parent
	 * @return
	 */
	Composite addGroupCheckBox(final Composite p_parent) {
		Composite group = new Composite(p_parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		group.setLayout(gridLayout);
		gridLayout.marginTop = 8;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		group.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		registerResourceToDispose(group.getCursor());
		return group;
	}

	/**
	 * 
	 * @param p_parent
	 * @param p_labelValue
	 * @param p_tooltip
	 * @return
	 */
	Button addCheckBox(final Composite p_parent, final String p_labelValue, final String p_tooltip) {
		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue + c_labelOffset);
		Composite group = new Composite(p_parent, SWT.NONE);
		FillLayout fillLayout = new FillLayout(SWT.HORIZONTAL);
		fillLayout.marginHeight = 3;
		group.setLayout(fillLayout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		registerResourceToDispose(group.getCursor());
		Button chbox = new Button(group, SWT.CHECK);
		chbox.setData(label);
		return chbox;
	}

	/**
	 * 
	 * @param p_parent
	 * @param p_labelValue
	 * @param p_tooltip
	 * @return
	 */
	MultiSelectionContainer addMultiSelection(final Composite p_parent, final String p_labelValue,
			final String p_tooltip) {
		Label label = new Label(p_parent, SWT.NONE);
		label.setToolTipText(p_tooltip);
		label.setText(p_labelValue + c_labelOffset);
		Composite group = new Composite(p_parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		GridLayout grid = new GridLayout(4, false);
		grid.marginWidth = 0;
		grid.marginHeight = 0;
		group.setLayout(grid);
		group.setLayoutData(gridData);
		Combo combo1 = new Combo(group, SWT.BORDER);
		combo1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button bt1 = new Button(group, SWT.FLAT);
		Button bt2 = new Button(group, SWT.FLAT);
		Combo combo2 = new Combo(group, SWT.BORDER);
		combo2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Image imageBt1 = new Image(p_parent.getDisplay(), this.getClass().getResourceAsStream("left.png"));
		Image imageBt2 = new Image(p_parent.getDisplay(), this.getClass().getResourceAsStream("right.png"));
		bt1.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		bt2.setCursor(new Cursor(p_parent.getDisplay(), SWT.CURSOR_HAND));
		bt1.setBackground(p_parent.getBackground());
		bt2.setBackground(p_parent.getBackground());
		registerResourceToDispose(imageBt1);
		registerResourceToDispose(imageBt2);
		registerResourceToDispose(bt1.getCursor());
		registerResourceToDispose(bt2.getCursor());
		bt1.setImage(imageBt1);
		bt2.setImage(imageBt2);

		bt1.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				transfertSelectectedItem(combo1, combo2);
			}
		});

		bt2.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				transfertSelectectedItem(combo2, combo1);
			}
		});
		return new MultiSelectionContainer(combo1, combo2);
	}

	/**
	 * 
	 * @param p_source
	 * @param p_target
	 */
	private void transfertSelectectedItem(final Combo p_source, final Combo p_target) {
		if (p_source.getItemCount() > 0 && p_source.getSelectionIndex() >= 0) {
			p_target.add(p_source.getItem(p_source.getSelectionIndex()), 0);
			p_source.remove(p_source.getSelectionIndex());
			p_target.select(0);
			if (p_source.getItemCount() > 0)
				p_source.select(0);
		}
		p_target.notifyListeners(SWT.Modify, new Event());
	}

	/**
	 * Creation of a borderless container for a generic composite.
	 * 
	 * @param p_parent
	 * @return
	 */
	Composite addGroupGeneric(final Composite p_parent) {
		Composite group = new Composite(p_parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, true);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		group.setLayout(gridLayout);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		return group;
	}

	/**
	 * Conteneur pour le système de multi-sélection d'une liste déroulante.
	 * <p>
	 * Afin d'avoir exactement la même taille pour les deux listes, on rempli les
	 * deux listes avec les mêmes valeurs. Une fois que les listes sont affichées on
	 * supprime alors toutes les valeurs de la seconde liste (non visible pour
	 * l'utilisateur). La seconde liste a alors sa taille qui est identique à celle
	 * de la première. Cela évite de surcharger le composant {@link CCombo}.
	 */
	class MultiSelectionContainer {

		private Combo _selectable;
		private Combo _selected;
		private boolean _painted;

		MultiSelectionContainer(final Combo p_selectable, final Combo p_selected) {
			_selectable = p_selectable;
			_selected = p_selected;

			_selected.addListener(SWT.Paint, new Listener() {
				@Override
				public void handleEvent(Event event) {
					if (!_painted) {
						_selected.removeAll();
						_painted = true;
					}
				}
			});
		}

		void populate(final List<String> p_values) {
			for (String value : p_values) {
				_selectable.add(value);
				_selected.add(value);
			}
		}

		Combo get_selected() {
			return _selected;
		}
	}

	class widgetContainer {

		private Button _yesButton;
		private Button _noButton;

		widgetContainer(final Button p_yesButton, final Button p_noButton) {
			_yesButton = p_yesButton;
			_noButton = p_noButton;
		}

		public Button getYesButton() {
			return _yesButton;
		}

		public Button getNoButton() {
			return _noButton;
		}
	}

	/**
	 * Demande express de suppression de certaines ressources à la fermeture du
	 * formulaire, sinon apparition de messages d'erreur dans la console
	 */
	@Override
	public void dispose() {
		for (Resource resource : _lstResourcesToDispose) {
			resource.dispose();
		}
		super.dispose();
	}
}
