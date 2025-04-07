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

import fr.pacman.core.property.project.ProjectProperties;
import fr.pacman.start.ui.util.FormUtil;
import fr.pacman.start.ui.util.ValidatorUtil;

/**
 * 
 * @author @MINARM
 */
public class PropertiesWizardStartPage extends PropertiesWizardPage<Control> {

	/**
	 * Liste des paramètres.
	 */
	private String _projectName = "";
	private String _packageName = "";
	private String _authorName = "";
	private String _javaVersion = "";
	private String _typeProject = "";
	private String _typeFramework = "";
	private String _sqlTablePrefix = "";
	private String _sqlTableSchema = "";
	private String _sqlTableSpace = "";
	private String _requirementPrefix = "";
	private String _requirementLevel = "";
	private String _requirementInitVersion = "";
	private String _spi4jRsCdi = "";
	private String _spi4jfetchingStrategy = "";
	private String _spi4jSecurity = "";
	private String _projectCrud = "";
	private String _databases = "";

	private List<SqlAutoField> _sqlAutoFields = new ArrayList<>();

	/**
	 * Constructeur avec la définition de l'en-tête pour le panneau global de
	 * saisie.
	 */
	protected PropertiesWizardStartPage() {
		super("Propriétés du projet Cali");
		setTitle("Nouveau projet Cali (BackEnd)");
		//setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.c_pluginId, Activator.c_pluginLogo));
		setDescription("Saisir les différents paramètres pour la création du projet.");
	}

	/**
	 * 
	 * @param p_defaultSize
	 */
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
				"Ensemble des informations de base permettant l'identification du projet.");

		Group project2 = addGroup(containers.get("project"), "Options",
				"Ensemble des informations structurantes pour la création du projet.");

		Group database2 = addGroup(containers.get("database"), "Options",
				"Ensemble des informations structurantes pour la création et la gestion de la(ou des) base(s) de données.");

		Group database3 = addGroup(containers.get("database"), "Champs automatiques",
				"Définition des champs transverses à rajouter automatiquement pour toutes les entités persistantes.",
				new GridLayout(1, false));

		Group options1 = addGroup(containers.get("options"), "Règles de gestion", "tooltip");
		Group options2 = addGroup(containers.get("options"), "Autre", "tooltip");

		registerWidget("cb_database", containers.get("database"));
		registerWidget("txt_project", addTextApplication(project1));
		registerWidget("txt_package", addTextPackage(project1));
		registerWidget("txt_author", addTextAuthorName(project1));
		registerWidget("cb_projectType", addComboProjectType(project2));
		registerWidget("cb_framework", addComboFramework(project2));
		registerWidget("cb_javaVersion", addComboJavaVersion(project2));

		addDatabases(project2);

		registerWidget("txt_sqlTPrefix", addTextDbTablePrefix(database2));
		// registerWidget("txt_sqlTSpace", addTextDbTableSpace(database2));
		registerWidget("txt_sqlTSpace", addTextDbTableSchema(database2));
		registerWidget("txt_reqPrefix", addTextReqPrefix(options1));
		registerWidget("txt_reqLevel", addTextReqLevel(options1));
		registerWidget("cb_reqInitVerion", addComboReqInitVersion(options1));
		registerWidget("ck_jerseyCdi", addCheckBoxCdi(options2));
		registerWidget("ck_fileConfig", addCheckBoxSpi4jConfig(options2));
		registerWidget("ck_fetchStrategy", addCheckBoxFetchStrategy(options2));
		registerWidget("ck_security", addCheckBoxSecurity(options2));
		registerWidget("ck_crud", addCheckBoxCrud(options2));
		registerWidget("ck_batch", addCheckBoxBatch(options2));
		registerWidget("grp_project1", project1);
		registerWidget("grp_project2", project2);
		registerWidget("grp_database2", database2);
		registerWidget("grp_database3", database3);
		registerWidget("grp_options1", options1);
		registerWidget("grp_options2", options2);
		registerWidget("tab_project", containers.get("project"));
		registerWidget("tab_database", containers.get("database"));
		registerWidget("tab_various", containers.get("various"));
		registerWidget("tbl_addSqlColumns", addTable(database3));

		setControl(container);
		setPageComplete(false);
		initWithDefault();
		resize(true);
	}

	/**
	 * Initialisation des données par défaut pour l'ensemble des valeurs de retour.
	 * Désactivation de certains composants pour l'arrivée sur le formulaire de
	 * création.
	 */
	private void initWithDefault() {
		_javaVersion = "17";
		_typeProject = "xx";
		_typeFramework = "springboot";
		_spi4jRsCdi = "false";
		_spi4jfetchingStrategy = "false";
		_spi4jSecurity = "false";
		_projectCrud = "false";

		getWidget("ck_jerseyCdi").setEnabled(false);
		getWidget("ck_fileConfig").setEnabled(false);
		getWidget("ck_fetchStrategy").setEnabled(false);
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
				completePackageName((Text) getWidget("txt_package"), txt);
				_packageName = ((Text) getWidget("txt_package")).getText();
				computeValidity();
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
				_packageName = txt.getText();
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
				"Auteur(s) ou organisme(s) à afficher dans l'ensemble des commentaires."
						+ "\nCes informations ne seront visibles que dans la partie infrastrucuture.");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_authorName = txt.getText();
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
						+ "\nLa base de données H2 est toujours automatiquement embarquée dans le projet.");
		container.populate(new ArrayList<String>(
				Arrays.asList("MySql", "Postgresql", "MariaDb", "Oracle (< 12.2)", "Oracle (> 12.1)")));

		container.get_selected().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				_databases = "";
				for (String item : container.get_selected().getItems()) {
					_databases += "," + item.toLowerCase();
				}
				computeValidity();
			}
		});
	}

	/**
	 * Ajout de la boite de sélection pour le choix de la version Java à appliquer
	 * sur l'ensemble du projet.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return la version Java.
	 */
	private Combo addComboJavaVersion(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Version Java", "La version LTS pour la compilation des classes du projet.",
				new String[] { "Java 17", "Java 21" }, 0);

		cbx.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent p_e) {
				_javaVersion = String.valueOf(cbx.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
		return cbx;
	}

	/**
	 * Champ de saisie pour le type de projet.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return le type de projet.
	 */
	private Combo addComboProjectType(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Type ",
				"Le type du (ou des) service(s) à exposer pour le projet de type FrontEnd.",
				new String[] { "Exposition de services externes de type rest" }, 0);

		cbx.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent p_e) {
				_typeProject = String.valueOf(cbx.getSelectionIndex());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
		return cbx;
	}

	/**
	 * Champ de saisie pour le choix du type de framework à utiliser.
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return le choix du type de framework.
	 */
	private Combo addComboFramework(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Framework",
				"Le framework à utiliser pour la génération des classes issues de la modélisation.",
				new String[] { "Spring Boot"}, 0);

		cbx.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent p_e) {
				_typeFramework = (cbx.getItem(cbx.getSelectionIndex()).replaceAll(" +", "")).toLowerCase();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Text addTextDbTablePrefix(final Composite p_parent) {
		final Text txt = addText(p_parent, "Préfixe pour l'ensemble des tables", "", "");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTablePrefix = txt.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	@SuppressWarnings("unused")
	private Text addTextDbTableSpace(final Composite p_parent) {
		final Text txt = addText(p_parent, "Tablespace pour les indexs (Oracle)", "", "");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTableSpace = txt.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForSqlTableSpace(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Text addTextDbTableSchema(final Composite p_parent) {
		final Text txt = addText(p_parent, "Schema pour l'ensemble des tables", "", "");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTableSchema = txt.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForSchema(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Text addTextReqPrefix(final Composite p_parent) {
		final Text txt = addText(p_parent, "Préfixe", "", "");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_requirementPrefix = txt.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Text addTextReqLevel(final Composite p_parent) {
		final Text txt = addText(p_parent, "Niveaux", "", "");
		txt.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_requirementLevel = txt.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
		return txt;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Combo addComboReqInitVersion(final Composite p_parent) {
		Combo cbx = addComboBox(p_parent, "Init. Version", "", new String[] { "None", "Current" }, 0);
		cbx.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent p_e) {
				_requirementInitVersion = (cbx.getItem(cbx.getSelectionIndex()).trim()).toLowerCase();
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Button addCheckBoxCdi(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Api REST - Utilisation de l'injection CDI", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Button addCheckBoxSpi4jConfig(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Fichiers de configuration gérés par SPI4J", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 * @return
	 */
	private Button addCheckBoxFetchStrategy(final Composite p_parent) {
		Button cbx = addCheckBox(p_parent, "Fetching Strategy", "");
		return cbx;
	}

	/**
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
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
	 * Création de la table pour l'ajout de colonnes supplémentaires au niveau de la
	 * base de données. ces champs sont à rajouter automatiquement sur chasue
	 * entité. Pour l'instant impossible d'arriver à lire les données insérées dans
	 * le tableau, on a donc recours à un hack en enregistrant l'ensemble des
	 * widgets non seulement au niveau de la table mais aussi dans une liste de
	 * {@link sqlAutoField}. Il est ainsi possible de récupérer les entrées lors du
	 * click utilisateur sur le bouton "finish".
	 * 
	 * @param p_parent le composite parent sur lequel accrocher le composant.
	 */
	private Table addTable(final Composite p_parent) {
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

		return table;
	}

	/**
	 * Enregistrement des lignes pour la table.
	 * 
	 * @param p_parent le composite parent, en loccurence, la table.
	 */
	private void addTableItem(final Table p_parent) {
		TableItem p_item = new TableItem(p_parent, SWT.NONE);

		TableEditor editor = new TableEditor(p_parent);
		CCombo cbx = new CCombo(p_parent, SWT.NONE);
		cbx.add("", 0);
		cbx.add("XtopSup", 1);
		cbx.add("XdMaj", 2);
		cbx.add("Char", 3);
		cbx.add("String", 4);
		cbx.add("Integer", 5);
		cbx.add("Long", 6);
		cbx.add("Double", 7);
		cbx.add("Float", 8);
		cbx.add("Date", 9);
		cbx.add("Timestamp", 10);
		cbx.add("Time", 11);
		cbx.add("Boolean", 12);
		editor.grabHorizontal = true;
		editor.setEditor(cbx, p_item, 0);

		Text txtName = addTableText(p_parent, p_item, 1);
		Text txtLength = addTableText(p_parent, p_item, 2);
		Text txtDefault = addTableText(p_parent, p_item, 3);
		Button cbxNull = addTableCheckBox(p_parent, p_item, 4);
		Text txtDescription = addTableText(p_parent, p_item, 5);

		// hack pour récupération des données.
		SqlAutoField row = new SqlAutoField();
		row._name = txtName;
		row._comment = txtDescription;
		row._default = txtDefault;
		row._type = cbx;
		row._size = txtLength;
		row._null = cbxNull;
		_sqlAutoFields.add(row);

		cbx.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cbx.getSelectionIndex() == 1) {
					txtName.setText("XTOPSUP");
					txtName.setEnabled(false);
					txtLength.setText("1");
					txtLength.setEnabled(false);
					txtDefault.setText("false");
					txtDefault.setEnabled(false);
					cbxNull.setEnabled(false);
					txtDescription.setText("Date de mise à jour de la ligne");
					txtDescription.setEnabled(false);
					txtName.getParent();
				} else if (cbx.getSelectionIndex() == 2) {
					txtName.setText("XDMAJ");
					txtName.setEnabled(false);
					txtLength.setText("");
					txtLength.setEnabled(false);
					txtDefault.setText("");
					txtDefault.setEnabled(false);
					cbxNull.setEnabled(false);
					txtDescription.setText("Indicateur de suppression logique");
					txtDescription.setEnabled(false);
				} else {
					txtName.setText("");
					txtLength.setText("");
					txtDefault.setText("");
					cbxNull.setEnabled(true);
					cbxNull.setSelection(false);
					txtDescription.setText("");
					txtLength.setEnabled(true);
					txtName.setEnabled(true);
					txtDefault.setEnabled(true);
					txtDescription.setEnabled(true);
				}
				computeValidity();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		txtName.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				computeValidity();
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
				if (cbx.getSelectionIndex() == 5 || cbx.getSelectionIndex() == 6 || cbx.getSelectionIndex() == 7
						|| cbx.getSelectionIndex() == 8) {
					if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
						p_e.doit = false;
					}
				}
			}
		});
	}

	/**
	 * Construction automatique d'un nom de package a partir du nom de projet.
	 * 
	 * @param p_packageName
	 * @param p_projectName
	 */
	private void completePackageName(final Text p_packageName, final Text p_projectName) {
		p_packageName.setText(p_packageName.getText().substring(0, p_packageName.getText().indexOf(".") + 1)
				.concat(p_projectName.getText().toLowerCase().replace("-", "_")));
	}

	/**
	 * Vérification globale de la saisie.
	 */
	private void computeValidity() {

		ValidatorUtil.INSTANCE.setPackageOK(!_packageName.isEmpty());
		ValidatorUtil.INSTANCE.setApplicationNewOk(FormUtil.checkForNewProject(_projectName));
		ValidatorUtil.INSTANCE.setApplicationOK(null != _projectName && !_projectName.isEmpty());
		ValidatorUtil.INSTANCE.setAuthorOK(null != _authorName && !_authorName.isEmpty());
		ValidatorUtil.INSTANCE.setSqlTablePrefixOk(FormUtil.checkValueForPrefix(_sqlTablePrefix));
		ValidatorUtil.INSTANCE.setSqlTableSchema(FormUtil.checkValueForSchema(_sqlTableSchema));
		ValidatorUtil.INSTANCE.setRequirementPrefixOK(FormUtil.checkValueForPrefix(_requirementPrefix));
		ValidatorUtil.INSTANCE.setDatabaseOK(FormUtil.checkForDatabase(_databases));
		ValidatorUtil.INSTANCE.setAdditionalFieldsOK(computeAdditionalFieldsValidity());
		setMessage(ValidatorUtil.INSTANCE.getMessage(), ValidatorUtil.INSTANCE.getMessageType());
		setPageComplete(ValidatorUtil.INSTANCE.isValid());
	}

	/**
	 * Vérifie la validité des champs additionnels pour les tables sql.
	 */
	private boolean computeAdditionalFieldsValidity() {
		int xtopsup = 0;
		int xdmaj = 0;
		for (SqlAutoField sqlAutoField : _sqlAutoFields) {
			if ("xtopsup".equalsIgnoreCase(sqlAutoField._name.getText().trim()))
				xtopsup++;
			if ("xdmaj".equalsIgnoreCase(sqlAutoField._name.getText().trim()))
				xdmaj++;
			if (!sqlAutoField._type.getText().isEmpty() && sqlAutoField._name.getText().isEmpty())
				return false;
			if (!sqlAutoField._name.getText().isEmpty() && sqlAutoField._type.getText().isEmpty())
				return false;
		}
		if (xtopsup > 1 || xdmaj > 1)
			return false;
		return true;
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
	 * Retourne le libellé du package racine pour le projet.
	 * 
	 * @return le libellé du package.
	 */
	public String getPackageName() {
		return _packageName;
	}

	/**
	 * Retourne le nom de l'auteur pour le projet.
	 * 
	 * @return le nom de l'auteur pour le projet.
	 */
	public String getAuthorName() {
		return _authorName;
	}

	/**
	 * Retourne la version Java pour la compilation du projet.
	 * 
	 * @return la version Java.
	 */
	public String getJavaVersion() {
		return _javaVersion;
	}

	/**
	 * Retourne le type de projet à générer.
	 * 
	 * @return le type du projet.
	 */
	public String getTypeProject() {
		return _typeProject;
	}

	/**
	 * Retourne le type de framework à utiliser pour le projet.
	 * 
	 * @return le type de framework.
	 */
	public String getTypeFramework() {
		return _typeFramework;
	}

	/**
	 * Retourne le préfixe à utiliser pour l'ensemble des tables sql. On supprimme
	 * l'underscore qui ne servait que pour la validation.
	 * 
	 * @return le préfixe pour les tables sql.
	 */
	public String getSqlTablePrefix() {
		return _sqlTablePrefix.replace("_", "");
	}

	/**
	 * Retourne le schéma à utiliser pour l'ensemble des tables sql.
	 * 
	 * @return le schéma à utiliser pour les tables sql.
	 */
	public String getSqlTableSchema() {
		return _sqlTableSchema;
	}

	/**
	 * Retourne (dans le cas d'une base Oracle) le tablespace à utiliser.
	 * 
	 * @return le tablespace à utiliser.
	 */
	public String getSqlTableSpace() {
		return _sqlTableSpace;
	}

	/**
	 * Retourne le préfixe à utiliser pour les règles de gestion (requirement).
	 * 
	 * @return le préfixe à utiliser pour les règles de gestions.
	 */
	public String getRequirementPrefix() {
		return _requirementPrefix;
	}

	/**
	 * 
	 * @return
	 */
	public String getRequirementLevel() {
		if (_requirementLevel.isEmpty() || _requirementLevel.isBlank())
			_requirementLevel = "0";
		return _requirementLevel;
	}

	/**
	 * 
	 * @return
	 */
	public String getRequirementInitVersion() {
		return _requirementInitVersion;
	}

	/**
	 * 
	 * @return
	 */
	public String getSpi4jRsCdi() {
		return _spi4jRsCdi;
	}

	/**
	 * 
	 * @return
	 */
	public String getSpi4jfetchingStrategy() {
		return _spi4jfetchingStrategy;
	}

	/**
	 * 
	 * @return
	 */
	public String getSpi4jSecurity() {
		return _spi4jSecurity;
	}

	/**
	 * Retourne la liste des base de données. On en profite pour supprimer les
	 * versions oracle et suffixer simplement les dernières versions par "+" et les
	 * premières versions par "-" . Le procédé est un peu cavalier, mais faut le
	 * job.
	 * 
	 * @return la liste des bases de données pour l'application cible.
	 */
	public String getDatabases() {
		return "h2" + _databases.replace(" (< 12.2)", "-").replace(" (> 12.1)", "+");
	}

	/**
	 * 
	 * @return
	 */
	public String getProjectCrud() {
		return _projectCrud;
	}

	/**
	 * On transfert l'ensemble des données du tableau dans une classe interne afin
	 * de rendre les données plus lisibles pour la classe chargée de la création du
	 * projet. Pour xdmaj et xtopsup il n'y a même pas besoin d'aller lire les
	 * informations car on les connait déjà.
	 * 
	 * @return une liste contenant les données pour l'ensemble des champs
	 *         supplémentaires (colonnes) pour la base de donénes
	 */
	public Map<String, String> getsqlAutoFields() {
		Map<String, String> sqlAutoFields = new HashMap<>();
		String sqlFields = "";

		for (SqlAutoField sqlAutoField : _sqlAutoFields) {
			if ("xdmaj".equalsIgnoreCase(sqlAutoField._name.getText().trim())) {
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajName, "XDMAJ");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajComment, "Date de mise à jour de la ligne");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajDefault, "");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajNull, "false");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajSize, "");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXdmajType, "Timestamp");
				sqlFields += ("," + ProjectProperties.c_sql_tableXdmaj);
			} else if ("xtopsup".equalsIgnoreCase(sqlAutoField._name.getText().trim())) {
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupName, "XTOPSUP");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupComment, "Indicateur de suppression logique");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupDefault, "false");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupNull, "false");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupSize, "1");
				sqlAutoFields.put(ProjectProperties.c_sql_tableXtopsupType, "Boolean");
				sqlFields += ("," + ProjectProperties.c_sql_tableXtopsup);
			} else if (!sqlAutoField._name.getText().isEmpty() && !sqlAutoField._name.getText().isBlank()) {
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".name", sqlAutoField._name.getText().trim());
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".comment", sqlAutoField._comment.getText().trim());
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".default", sqlAutoField._default.getText().trim());
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".size", sqlAutoField._size.getText().trim());
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".type", sqlAutoField._type.getText());
				sqlFields += ("," + ProjectProperties.c_sql_tableXField + "." + sqlAutoField._name.getText().trim());
				sqlAutoFields.put(sqlAutoField.getAssociatedKey() + ".null", sqlAutoField.isNull());

			}
		}
		sqlAutoFields.put(ProjectProperties.c_sql_fields, sqlFields.replaceFirst(",", ""));
		return sqlAutoFields;
	}

	/**
	 * Classe interne pour le stockage des champs supplémentaires dans les entités
	 * de la base de données.
	 */
	class SqlAutoField {
		Text _name;
		Text _size;
		Text _comment;
		Text _default;
		Button _null;
		CCombo _type;

		String getAssociatedKey() {
			return ProjectProperties.c_sql_tableXField + "." + _name.getText().trim();
		}

		String isNull() {
			return String.valueOf(_null.getSelection());
		}

		String getXtopSupType() {
			return _type.getText();
		}
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
