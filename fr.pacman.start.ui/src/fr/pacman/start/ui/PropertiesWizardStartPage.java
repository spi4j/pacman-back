package fr.pacman.start.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import fr.pacman.start.ui.activator.Activator;
import fr.pacman.start.ui.util.FormUtil;

/**
 * 
 * @author @MINARM
 */
public class PropertiesWizardStartPage extends PropertiesWizardPage<Control> {

	/**
	 * Liste des paramètres.
	 */
	private String _projectName = "";

	/**
	 * Constructeur avec la définition de l'en-tête pour le panneau global de
	 * saisie.
	 */
	protected PropertiesWizardStartPage() {
		super("Propriétés du projet Cali");
		setTitle("Nouveau projet Cali (BackEnd)");
		setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.c_pluginId, Activator.c_pluginLogo));
		setDescription("Saisir les différents paramètres pour la création du projet.");
	}

	void resize(final boolean p_defaultSize) {
		if (p_defaultSize)
			getShell().setSize(getShell().computeSize(720, 300));
	}

	/**
	 * Initialisation et positionnement de l'ensemble des éléments de saisie sur la
	 * page.
	 */
	@Override
	public void createControl(final Composite p_parent) {
		final Composite container = new Composite(p_parent, SWT.NONE);
		final Map<String, Composite> containers = addTabFolder(container);

		Group project1 = addGroup(containers.get("project"), "Identification",
				"Informations de base permettant l'identification du projet.");

		Group project2 = addGroup(containers.get("project"), "Options",
				"Informations structurantes pour la création du projet.");

		Group database2 = addGroup(containers.get("database"), "Options",
				"Informations structurantes pour la création et la gestion de la(ou des) base(s) de données.");

		Group database3 = addGroup(containers.get("database"), "Champs automatiques",
				"Définition des champs transverses à rajouter automatiquement pour toutes les entités persistantes.",
				new GridLayout(1, false));

		Group options1 = addGroup(containers.get("options"), "Règles de gestion", "tooltip");
		Group options2 = addGroup(containers.get("options"), "Autre", "tooltip");

		registerWidget("w_dba", containers.get("database"));
		registerWidget("w_appl", addTextApplication(project1));
		registerWidget("w_pack", addTextPackage(project1));
		registerWidget("w_auth", addTextAuthorName(project1));
		registerWidget("w4", addComboProjectType(project2));
		registerWidget("w4b", addComboFramework(project2));
		registerWidget("w5", addComboJavaVersion(project2));

		addDatabases(project2);

		registerWidget("w10", addTextDbTablePrefix(database2));
		registerWidget("w11", addTextDbTableSpace(database2));
		registerWidget("w12", addTextDbTableSchema(database2));

		registerWidget("w13", addTextReqPrefix(options1));
		registerWidget("w14", addTextReqLevel(options1));
		registerWidget("w15", addComboReqInitVersion(options1));

		registerWidget("w16", addCheckBoxCdi(options2));
		registerWidget("w17", addCheckBoxSpi4jConfig(options2));
		registerWidget("w18", addCheckBoxFetchStrategy(options2));
		registerWidget("w19", addCheckBoxSecurity(options2));
		registerWidget("w20", addCheckBoxCrud(options2));
		registerWidget("w21", addCheckBoxBatch(options2));

		registerWidget("w22", project1);
		registerWidget("w23", project2);
		registerWidget("w25", database2);
		registerWidget("w26", database3);
		registerWidget("w27", options1);
		registerWidget("w28", options2);

		registerWidget("w29", containers.get("project"));
		registerWidget("w30", containers.get("database"));
		registerWidget("w31", containers.get("various"));

		addTable(database3);

		setControl(container);
		setPageComplete(false);
		initWithDefault();
		resize(true);

		setPageComplete(true);
	}

	/**
	 * 
	 */
	private void initWithDefault() {
		// setEnabled(getWidget("w7"), false);
		// setEnabled(getWidget("w8"), false);
		// ((Button) getWidget("w6")).setSelection(true);
	}

	/**
	 * Création d'un conteneur avec les différents onglets. (On ne fonctionne pour
	 * l'instant qu'avec des {@link GridLayout} car plus flexible si besoin de
	 * changer rapidement d'organisation des composants).
	 * 
	 * @param p_parent le conteneur parent.
	 * @return une liste d'onglets prêts à recevoir les différents composants pour
	 *         la saisie utilisateur.
	 */
	private Map<String, Composite> addTabFolder(final Composite p_parent) {
		final Map<String, Composite> containers = new HashMap<String, Composite>();
		final TabFolder tabFolder = new TabFolder(p_parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		containers.put("project", addTabItem(tabFolder, "Projet"));
		containers.put("database", addTabItem(tabFolder, "Base de données"));
		containers.put("options", addTabItem(tabFolder, "Autre"));
		p_parent.setLayout(new GridLayout());
		return containers;
	}

	/**
	 * Champ de saisie pour le nom de l'application.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private Text addTextApplication(final Composite p_parent) {
		final Text txt = addText(p_parent, "Nom", "",
				"Nom du projet avec lequel la structure de l'application sera générée.");
		txt.setFocus();
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_projectName = txt.getText();
				// FormUtil.completePackageName(_packageName, applicationName);
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForProjectName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * Champ de saisie pour le package racine de l'application (tous les packages
	 * générés commenceront avec le contenu de cette saisie).
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private Text addTextPackage(final Composite p_parent) {
		Text txt = addText(p_parent, "Package racine     ", "fr.", "Package racine du projet sous lequel"
				+ " positionner l'ensemble des classes et des autres sous-packages.");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPackageName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * Champ de saisie pour le nom par défaut du ou des auteur(s) de l'application.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private Text addTextAuthorName(final Composite p_parent) {
		final Text txt = addText(p_parent, "Auteur(s)", "",
				"Auteur(s) ou organisme(s) à afficher dans l'ensemble des commentaires "
						+ "(uniquement pour la partie infrastrucuture).");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				// RAS.
			}
		});
		return txt;
	}

	/**
	 * Champ de saisie pour la sélection de la (ou des) base(s) de données à
	 * utiliser pour la sauvegarde des données de l'application.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private void addDatabases(final Composite p_parent) {
		MultiSelectionContainer container = addMultiSelection(p_parent, "Base(s) de données",
				"La (ou les) base(s) de données à utiliser pour la persistence des données de l'application"
						+ "\nLa base de données H2 est automatiquement embarquée dans le projet.");
		container.populate(new ArrayList<String>(
				Arrays.asList("MySql", "Postgresql", "MariaDb", "Oracle (< 12.2)", "Oracle (> 12.1)")));

		container.get_selected().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setEnabled(getWidget("w_dba"), container.get_selected().getItemCount() > 0);
			}
		});
	}

	/**
	 * 
	 * @param p_parent p_parent le composite parent sur lequel accrocher le
	 *                 composant.
	 * @return
	 */
	private Button addCheckBoxHttpServer(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Utilise un serveur HTTP embarqué", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent p_parent le composite parent sur lequel accrocher le
	 *                 composant.
	 * @return
	 */
	private Button addCheckBoxH2(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Utilise une base H2 embarquée", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Combo addComboJavaVersion(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Version Java", "La version LTS pour la compilation des classes du projet.",
				new String[] { "Java 11", "Java 17", "Java 21" }, 0);
		return cbx;
	}

	/**
	 * Champ de saisie pour le type de projet.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Combo addComboProjectType(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Type ",
				"Le type du (ou des) service(s) à exposer pour le projet de type FrontEnd.",
				new String[] { "Librairie de services internes", "Exposition de services externes",
						"Exposition de micro-services externes", "Librairie client services externes",
						"POC (base H2 embedded)" },
				0);
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Combo addComboFramework(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Framework",
				"Le framework à utiliser pour la génération des classes issues de la modélisation.",
				new String[] { "Spring", "Spi4j" }, 0);
		return cbx;
	}

	private Text addTextDbTablePrefix(final Composite p_parent) {
		final Text txt = addText(p_parent, "Préfixe pour l'ensemble des tables", "", "");
		return txt;
	}

	private Text addTextDbTableSpace(final Composite p_parent) {
		final Text txt = addText(p_parent, "Tablespace pour les indexs (Oracle)", "", "");
		return txt;
	}

	private Text addTextDbTableSchema(final Composite p_parent) {
		final Text txt = addText(p_parent, "Schema pour l'ensemble des tables", "", "");
		return txt;
	}

	private Text addTextReqPrefix(final Composite p_parent) {
		final Text txt = addText(p_parent, "Préfixe", "", "");
		return txt;
	}

	private Text addTextReqLevel(final Composite p_parent) {
		final Text txt = addText(p_parent, "Niveaux", "", "");
		return txt;
	}

	private Combo addComboReqInitVersion(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Init. Version", "", new String[] { "None", "Current" }, 0);
		return cbx;
	}

	private Button addCheckBoxCdi(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Api REST - Utilisation de l'injection CDI", "");
		return cbx;
	}

	private Button addCheckBoxSpi4jConfig(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Fichiers de configuration gérés par SPI4J", "");
		return cbx;
	}

	private Button addCheckBoxFetchStrategy(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Fetching Strategy", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent
	 * @return
	 */
	private Button addCheckBoxSecurity(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Implémentation de la sécurité",
				"Activation de la sécurité (permissions) utilisateur pour l'utilisation"
						+ " du (ou des) service(s) de l'application.");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Button addCheckBoxCrud(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Implémentation du CRUD",
				"Activation de la génération automatique des services de type CRUD.");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Button addCheckBoxBatch(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Implémentation des batchs", "Activation d...");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private void addTable(final Composite p_parent) {
		Table table = addTable(p_parent, 200);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(110);
		column.setText("Type");
		column.setToolTipText("Le type de colonne pour le champ automatique.");

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setWidth(150);
		column1.setText("Nom");
		column1.setToolTipText("Le nom de la colonne pour le champ automatique.");

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(50);
		column2.setText("Taille");
		column2.setToolTipText("La taille maximale pour la colonne (optionnel).");

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(80);
		column3.setText("Défaut");
		column3.setToolTipText("La valeur par défaut pour la colonne si aucune valeur de définie (optionnel).");

		TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setWidth(45);
		column4.setText("Null");
		column4.setToolTipText("Accepte la valeur 'null'.");

		TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setText("Commentaire");
		column5.setWidth(220);
		column5.setToolTipText("Le commentaire pour la colonne (optionnel).");

		for (int i = 0; i < 10; i++)
			addTableItem(table);
	}

	/**
	 * 
	 * @param p_parent le composite parent, en loccurence, la table.
	 */
	private void addTableItem(final Table p_parent) {
		TableItem p_item = new TableItem(p_parent, SWT.NONE);

		TableEditor editor = new TableEditor(p_parent);
		CCombo combo = new CCombo(p_parent, SWT.NONE);
		combo.add("XtopSup", 0);
		combo.add("XdMaj", 1);
		combo.add("Char", 2);
		combo.add("String", 3);
		combo.add("Integer", 4);
		combo.add("Long", 5);
		combo.add("Double", 6);
		combo.add("Float", 7);
		combo.add("Date", 8);
		combo.add("Timestamp", 9);
		combo.add("Time", 10);
		combo.add("Boolean", 11);
		editor.grabHorizontal = true;
		editor.setEditor(combo, p_item, 0);

		Text txtName = addTableText(p_parent, p_item, 1);
		Text txtLength = addTableText(p_parent, p_item, 2);
		Text txtDefault = addTableText(p_parent, p_item, 3);
		addTableCheckBox(p_parent, p_item, 4);
		addTableText(p_parent, p_item, 5);

		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><");

			}
		});

		txtName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForAlphaNumericValue(p_e.character)) {
					p_e.doit = false;
				}
			}
		});

		txtLength.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
					p_e.doit = false;
				}
			}
		});

		txtDefault.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (combo.getSelectionIndex() == 4 || combo.getSelectionIndex() == 5 || combo.getSelectionIndex() == 6
						|| combo.getSelectionIndex() == 7) {
					if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
						p_e.doit = false;
					}
				}
			}
		});
	}

	/**
	 * Vérification globale de la saisie.
	 */
	private void computeValidity() {

	}

	/**
	 * Retourne le nom pour le projet qui sera concaténé avec les différents
	 * suffixes en fonction des sous-projets à créer.
	 * 
	 * @return le nom du projet.
	 */
	public String getProjectName() {
		return _projectName;
	}

	/**
	 * Retourne la liste des fichiers de modélisation à créer directement dans le
	 * projet de modélisation.
	 * 
	 * @return la liste des fichiers de modélisation à créer.
	 */
	public List<String> getModelCodes() {
		List<String> models = new ArrayList<>();
		models.add("entity");
		models.add("requirement");
		models.add("soa");
		return models;
	}
}
