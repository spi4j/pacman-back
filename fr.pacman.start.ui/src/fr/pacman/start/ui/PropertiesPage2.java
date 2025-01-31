package fr.pacman.start.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import fr.pacman.start.ui.activator.Activator;
import fr.pacman.start.ui.util.FormUtil;
import fr.pacman.start.ui.util.ValidatorUtil;

/**
 * Display a properties page.
 * 
 * @author @MINARM
 *
 */
public class PropertiesPage2 extends WizardPage {

	public static final String c_PROJECT_PACKAGE_DEFAULT = "fr.";
	public static final String c_CARRIAGE_RETURN_W = "WINDOWS";
	public static final String c_CARRIAGE_RETURN_L = "UNIX";
	public static final String c_BOOL_STR_YES = "true";
	public static final String c_BOOL_STR_NO = "false";
	public static final String c_BOOL_STR_1 = "1";
	public static final String c_BOOL_STR_0 = "0";
	public static final String c_DEFAULT_SQL_DATE = "current_date";
	public static final String c_VERSION_INIT_CURRENT = "current";
	public static final String c_VERSION_INIT_NONE = "none";
	public static final String c_IHM_TYPE_NO_DBDD = "JSF";
	public static final String c_ORACLE = "Oracle";
	public static final String c_ORACLE_32 = "Oracle_32";
	public static final String c_POSTGRESQL = "PostgreSQL";
	public static final String c_MYSQL = "MySQL";
	public static final String c_H2 = "H2";
	public static final String c_MARIA_DB = "MariaDB";
	public static final String c_AUTHOR = "safr@n";
	public static final String c_REQUIREMENT_PREFIX = "REQ_";
	public static final String c_REQUIREMENT_LEVEL = "0";
	public static final String c_XTOPSUP = "xtopsup";
	public static final String c_XDMAJ = "xdmaj";

	// Initialisation des listes deroulantes.
	public static final String[] c_USE_BDD_LIST = new String[] { "OUI", "NON" };
	public static final String[] c_SQL_TYPE_COLUMN_LIST = new String[] { "Binary", "Boolean", "Date", "Double", "Float",
			"Integer", "Long", "String", "Time", "Timestamp" };

	public static final String[] c_SQL_NB_COLUMNS_LIST = new String[] {

			"", "Un champ", "Deux champs", "Trois champs", "Quatre champs" };

	public static final String[] c_SQL_NB_LIBRARIES_LIST = new String[] {

			"", "Une librairie", "Deux librairies", "Trois librairies", "Quatre librairies" };

	// Assemblage des versions pour Java
	public static final Map<String, String> c_JAVA_VERSION;

	// Initialisation de la map des versions Java.
	static {

		c_JAVA_VERSION = new HashMap<String, String>();
		c_JAVA_VERSION.put("version 11", "11");
		c_JAVA_VERSION.put("version 17", "17");
		c_JAVA_VERSION.put("version 21", "21");
	}

	public static final String c_LBL_YES = "OUI";
	public static final String c_LBL_NO = "NON";
	public static final String c_LBL_NONE = "none";
	public static final String c_LBL_CURRENT = "current";

	// List of constants specifics to the wizard.
	public static final String c_PROJECT = "project";
	public static final String c_GENERATE = "generate";
	public static final String c_LIBRARY = "library";
	public static final String c_ODATABASE = "odatabase";
	public static final String c_MODELING = "modeling";
	public static final String c_VERSION = "version";
	public static final String c_VARIOUS = "divers";
	public static final String c_TABIDX = "tabIdx";

	// Beware for shell.disposed !
	private Text _packageName;

	// All options are under string format (wrong or not ?)
	private String _ihm = "";
	private String _namingCode = "";
	private String _javaVersion = "";
	private String _spi4jVersion = "";
	private String _sqlTablePrefix = "";
	private String _sqlTableSpace = "";
	private String _sqlTableSchema = "";
	private String _applicationName = "";
	private String _httpEmbeddedServer = "";
	private String _authorName = c_AUTHOR;
	private String _log4J = c_BOOL_STR_YES;
	private String _srvEjb = c_BOOL_STR_NO;
	private String _srvReq = c_BOOL_STR_YES;
	private String _srvWS = c_BOOL_STR_NO;
	private String _srvWMS = c_BOOL_STR_NO;
	private String _appliCrud = c_BOOL_STR_NO;
	private String _genBDD = c_BOOL_STR_NO;
	private String _useConfig = c_BOOL_STR_NO;
	private String _isLibrary = c_BOOL_STR_NO;
	private String _isLibraryRs = c_BOOL_STR_NO;
	private String _useSecurity = c_BOOL_STR_NO;
	private String _modeDebug = c_BOOL_STR_NO;
	private String _typeRC = c_CARRIAGE_RETURN_W;
	private String _unitTests = c_BOOL_STR_YES;
	private String _generateMatching = c_BOOL_STR_YES;
	private String _fetchingStrategy = c_BOOL_STR_NO;
	private String _requirementVersion = c_VERSION_INIT_NONE;
	private String _requirementPrefix = c_REQUIREMENT_PREFIX;
	private String _requirementLevel = c_REQUIREMENT_LEVEL;
	private String _crud = c_BOOL_STR_NO;
	private String _hk2 = c_BOOL_STR_YES;
	private String _eh2 = c_BOOL_STR_NO;

	private Label _labelBDD;

	private final List<String> _bdNames = new ArrayList<>();
	private final List<String> _mdCodes = new ArrayList<>();
	private final List<AddLibrary> _useLibraries = new ArrayList<>();
	private final List<AddColumn> _sqlAddColumns = new ArrayList<>();
	private final List<AddColumn> _sqlReservedAddColumns = new ArrayList<>();
	private Map<Id, Composite> _lstCompositesToManage = new Hashtable<>();

	/**
	 * Properties for the page.
	 */
	protected PropertiesPage2() {

		super("Propriétés du projet Safr@n");
		setTitle("Nouveau projet Safr@n");
		setImageDescriptor(getCaliLogoForWizard());
		setDescription("Entrez les paramètres pour la création du projet");
	}

	/**
	 * @TODO : Check for image loading exception.
	 * 
	 * @return ImageDescriptor
	 */
	private static ImageDescriptor getCaliLogoForWizard() {

		return Activator.imageDescriptorFromPlugin(Activator.c_pluginId, Activator.c_pluginLogo);
	}

	/**
	 * Main method for the page creation.
	 */
	@Override
	public void createControl(final Composite p_parent) {

		final Composite v_container = new Composite(p_parent, SWT.NONE);
		final Map<String, Composite> v_containers = addTabFolder(v_container);

		
		
		addSWTGroup(v_containers.get(c_PROJECT));
		
		
		addTextApplication(v_containers.get(c_PROJECT));
		addTextPackage(v_containers.get(c_PROJECT));
		addComboUseBDD(v_containers.get(c_PROJECT));
		addRadioTU(v_containers.get(c_GENERATE));
		addRadioSrvReq(v_containers.get(c_GENERATE));
		addRadioHttpServer(v_containers.get(c_VARIOUS));
		addRadioUseSecurity(v_containers.get(c_GENERATE));
		addRadioWS(v_containers.get(c_GENERATE));
		// addRadioWMS(v_containers.get(c_GENERATE));
		addRadioGenBDD(v_containers.get(c_GENERATE));
		addCheckBoxDataBase(v_containers.get(c_PROJECT));
		addRadioIsLibrary(v_containers.get(c_GENERATE));
		addRadioIsLibraryRs(v_containers.get(c_GENERATE));
		addComboUseLibraries(v_containers.get(c_LIBRARY));
		addTabFolderUseLibraries(v_containers.get(c_LIBRARY));
		addSPI4JVersion(v_containers.get(c_VERSION));
		//addComboJavaVersion(v_containers.get(c_VERSION));
		addTextSqlTablePrefix(v_containers.get(c_ODATABASE));
		addTextSqlTableSpace(v_containers.get(c_ODATABASE));
		addTextSqlTableSchema(v_containers.get(c_ODATABASE));
		addCheckBoxSqlXtopSupXdMaj(v_containers.get(c_ODATABASE));
		addComboSqlColumns(v_containers.get(c_ODATABASE));
		addTabFolderSqlColumns(v_containers.get(c_ODATABASE));
		addTextAuthorName(v_containers.get(c_VARIOUS));
		addRadioRC(v_containers.get(c_VARIOUS));
		addRadioModeDebug(v_containers.get(c_VARIOUS));
		addRadioFecthingStrategy(v_containers.get(c_VARIOUS));
		addRadioUseConfig(v_containers.get(c_VARIOUS));
		addRadioLog4j(v_containers.get(c_GENERATE));
		addRadioH2Embedded(v_containers.get(c_VARIOUS));
		addRadioHk2(v_containers.get(c_VARIOUS));
		addRadioCrud(v_containers.get(c_GENERATE));
		addTextRequirementPrefix(v_containers.get(c_VARIOUS));
		addTextRequirementLevel(v_containers.get(c_VARIOUS));
		addRadioRequirementVersion(v_containers.get(c_VARIOUS));

		manageInitialDeactivations();
		setControl(v_container);
		setPageComplete(false);
		resize(true);
	}
	
	
	
	
	
	
	
	
	

		public void addSWTGroup(Composite parent) {
		
			
			Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
			group.setLocation(50, 50);
			
			group.setText("Group SHADOW_IN");
			
			Label label = new Label(group, SWT.NONE);
			label.setText("Label in Group");
			label.setLocation(20,20);
			label.pack();
			
			Button button = new Button(group, SWT.PUSH);
			button.setText("Push button in Group");
			button.setLocation(20,45);
			button.pack();
			
			group.pack();
}
			

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * Set the default size for the wizard. To be completed...
	 */
	private void resize(final boolean p_defaultSize) {

		if (p_defaultSize)
			getShell().setSize(getShell().computeSize(700, 770));
	}

	/**
	 * Get the default generic GridLayout for the TabItem.
	 * 
	 * @return
	 */
	private Layout getDefaultLayout() {

		return new GridLayout(2, true);
	}

	/**
	 * Get the specific GridLayout for the Database TabItem.
	 * 
	 * @return
	 */
	private Layout getLayoutForODatabase() {

		return new GridLayout(1, false);
	}

	/**
	 * Get the specific GridLayout for the Libraries TabItem.
	 * 
	 * @return
	 */
	private Layout getLayoutForLibraries() {

		return new GridLayout(1, false);
	}

	/**
	 * Creation of the container for the different tabs.
	 * 
	 * @param p_container
	 * @return
	 */
	private Map<String, Composite> addTabFolder(final Composite p_container) {

		final Map<String, Composite> v_containers = new HashMap<String, Composite>();
		final TabFolder v_tabFolder = new TabFolder(p_container, SWT.NONE);
		v_tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		v_containers.put(c_PROJECT, addTabItem(v_tabFolder, "Configuration", getDefaultLayout()));
		v_containers.put(c_GENERATE, addTabItem(v_tabFolder, "Génération", getDefaultLayout()));
		v_containers.put(c_MODELING, addTabItem(v_tabFolder, "Modélisation", getDefaultLayout()));
		v_containers.put(c_LIBRARY, addTabItem(v_tabFolder, "Librairies", getLayoutForLibraries()));
		v_containers.put(c_ODATABASE, addTabItem(v_tabFolder, "Options BDD", getLayoutForODatabase()));
		v_containers.put(c_VARIOUS, addTabItem(v_tabFolder, "Divers", getDefaultLayout()));
		v_containers.put(c_VERSION, addTabItem(v_tabFolder, "Versions", getDefaultLayout()));
		v_tabFolder.setBackground(p_container.getParent().getBackground());
		p_container.setLayout(new GridLayout());
		return v_containers;
	}

	/**
	 * Creation of an item tab with its containers.
	 * 
	 * @param p_container
	 * @param p_tabName
	 * @param p_laout
	 * @return
	 */
	private Composite addTabItem(final TabFolder p_container, final String p_tabItemName, final Layout p_layout) {

		final TabItem v_tabItem = new TabItem(p_container, SWT.BORDER | SWT.SINGLE);
		final Composite v_pageItem = new Composite(p_container, SWT.BORDER | SWT.SINGLE);
		v_pageItem.setBackground(p_container.getParent().getBackground());
		v_tabItem.setText(p_tabItemName);
		v_tabItem.setControl(v_pageItem);
		v_pageItem.setLayout(p_layout);
		return v_pageItem;
	}

	/**
	 * Add an input field with a label to the container.
	 * 
	 * @param p_labelValue   Label for the input field
	 * @param p_defaultValue Default value for the element.
	 * @param p_tooltip      An explanation for the input field (optional).
	 * @return Text
	 */
	private Text addTextWithLabel(final Composite p_container, final String p_labelValue, final String p_defaultValue,
			final String p_tooltip) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setToolTipText(p_tooltip);
		v_label.setText(p_labelValue);

		final Text v_element = new Text(p_container, SWT.BORDER | SWT.SINGLE);
		v_element.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		v_element.setText(p_defaultValue);

		return v_element;
	}

	/**
	 * Add an input field (with more than one line) and a label to the container.
	 * 
	 * @param p_container
	 * @param p_labelValue
	 * @param p_defaultValue
	 * @param nbLines
	 * @param p_tooltip      An explanation for the input field (optional).
	 * @return
	 */
	@SuppressWarnings("unused")
	private Text addTextAreaWithLabel(final Composite p_container, final String p_labelValue,
			final String p_defaultValue, final int nbLines, final String p_tooltip) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setToolTipText(p_tooltip);
		v_label.setText(p_labelValue);

		final Text v_element = new Text(p_container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		v_element.setLayoutData(new GridData(GridData.FILL_BOTH));
		v_element.setText(p_defaultValue);

		return v_element;
	}

	/**
	 * Adds a Select box with a label to the container.
	 * 
	 * @param p_labelValue Label for the select box
	 * @param p_elements   List of elements to add to the list.
	 * @param p_tooltip    An explanation for the input field (optional).
	 * @return Combo
	 */
	private Combo addComboBoxWithLabel(final Composite p_container, final String p_labelValue,
			final String[] p_elements, final int p_defaultSelection, final String p_tooltip) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setToolTipText(p_tooltip);
		v_label.setText(p_labelValue);

		Combo v_combo = new Combo(p_container, SWT.BORDER);
		v_combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		v_combo.setItems(p_elements);
		v_combo.select(p_defaultSelection);

		return v_combo;
	}

	/**
	 * Creating a borderless container for radio buttons.
	 * 
	 * @param p_container
	 * @return
	 */
	private Composite addGroupRadio(final Composite p_container) {

		Composite v_group = new Composite(p_container, SWT.BORDER);
		FillLayout v_fillLayout = new FillLayout(SWT.HORIZONTAL);
		v_fillLayout.marginWidth = 5;
		v_fillLayout.marginHeight = 3;
		v_group.setLayout(v_fillLayout);
		v_group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// v_group.setCursor(new Cursor(p_container.getDisplay(), SWT.CURSOR_HAND));
		return v_group;
	}

	/**
	 * Creation of a borderless container for the checkboxes.
	 * 
	 * @param p_container
	 * @return
	 */
	private Composite addGroupCheckBox(final Composite p_container) {

		Composite v_group = new Composite(p_container, SWT.NONE);
		GridLayout v_gridLayout = new GridLayout();
		v_group.setLayout(v_gridLayout);
		v_gridLayout.marginTop = 8;
		GridData v_gridData = new GridData(GridData.FILL_HORIZONTAL);
		v_group.setLayoutData(v_gridData);
		// v_group.setCursor(new Cursor(p_container.getDisplay(), SWT.CURSOR_HAND));
		return v_group;
	}

	/**
	 * Creation of a borderless container for a generic composite.
	 * 
	 * @param p_container
	 * @return
	 */
	private Composite addGroupGeneric(final Composite p_container) {

		Composite v_group = new Composite(p_container, SWT.NONE);
		GridLayout v_gridLayout = new GridLayout(2, true);
		GridData v_gridData = new GridData(GridData.FILL_HORIZONTAL);
		v_group.setLayoutData(v_gridData);
		v_group.setLayout(v_gridLayout);
		v_gridLayout.marginHeight = 0;
		v_gridLayout.marginWidth = 0;
		return v_group;
	}

	/**
	 * Centralize all initial deactivations for components.
	 */
	private void manageInitialDeactivations() {

		manageActivationForWS(false);
		manageActivationForWMS(false);
		manageActivationForJbehaveTest(false);
		manageActivationForH2EmbeddedDatabase(false);
		manageActivationForCrudOperations(false);
	}

	/**
	 * Enable or disable the text field for SQL table prefix.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForSqlPrefix(final boolean p_enable) {

		final Composite v_groupGenSqlPrefix = _lstCompositesToManage.get(Id.ODB_SQL_PREFIX);

		Control[] v_lstControls = v_groupGenSqlPrefix.getChildren();
		for (Control v_control : v_lstControls) {
			if (v_control instanceof Text) {
				v_control.setEnabled(p_enable);
				if (!p_enable)
					((Text) v_control).setText("");
			}
		}
	}

	/**
	 * Enable or disable the text field for constraint tablespace.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForSqlTableSpace() {

		final Composite v_groupGenSqlTableSpace = _lstCompositesToManage.get(Id.ODB_SQL_TABLESPACE);

		Text v_txt = null;
		Control[] v_lstControls = v_groupGenSqlTableSpace.getChildren();
		for (Control v_control : v_lstControls) {
			if (v_control instanceof Text) {
				v_txt = (Text) v_control;
				break;
			}
		}

		if (null != v_txt) {
			if (_bdNames.contains(c_ORACLE) || _bdNames.contains(c_ORACLE_32)) {
				v_txt.setEnabled(true);
			} else {
				v_txt.setEnabled(false);
				v_txt.setText("");
			}
		}
	}

	/**
	 * Enable or disable the combo for optional sql columns.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForSqlNbColumns(final boolean p_enable) {

		final Composite v_groupNbColumns = _lstCompositesToManage.get(Id.ODB_SQL_NBCOLUMNS);
		Combo v_combo = (Combo) v_groupNbColumns;
		v_combo.setEnabled(p_enable);
		if (!p_enable) {
			v_combo.select(0);
			v_combo.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * Enable or disable the combo for optional libraries.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForLibraries(final boolean p_enable) {

		final Composite v_groupNbLibraries = _lstCompositesToManage.get(Id.LIB_USE_NBCOLUMNS);
		Combo v_combo = (Combo) v_groupNbLibraries;
		v_combo.setEnabled(p_enable);
		if (!p_enable) {
			v_combo.select(0);
			v_combo.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * Enable or disable the sql fields for an optional library.
	 * 
	 * @param p_enable
	 */
	@SuppressWarnings("unused")
	private void manageActivationForLibrarySqlFields(final boolean p_enable, int p_i) {

		// Non implemente pour l'instant...
	}

	/**
	 * Enable or disable the combo for schema name of sql tables.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForSqlTableSchema(final boolean p_enable) {

		final Composite v_groupSchema = _lstCompositesToManage.get(Id.ODB_SQL_SCHEMA);
		Control[] v_lstControls = v_groupSchema.getChildren();
		for (Control v_control : v_lstControls) {
			if (v_control instanceof Text) {
				v_control.setEnabled(p_enable);
				if (!p_enable)
					((Text) v_control).setText("");
			}
		}
	}

	/**
	 * Create the TabItem depending the number of optional columns.
	 * 
	 * @param p_nbColumns
	 */
	private void manageActivationForUseLibraries(final int p_nbColumns) {

		TabFolder v_useLibrariesTabFolder = (TabFolder) _lstCompositesToManage.get(Id.LIB_USE_LIBRARY);
		_useLibraries.clear();

		for (int v_idx = (v_useLibrariesTabFolder.getItemCount() - 1); v_idx > -1; v_idx--) {
			v_useLibrariesTabFolder.getItem(v_idx).dispose();
		}

		for (int v_idx = 0; v_idx < p_nbColumns; v_idx++) {
			Composite v_item = addTabItem(v_useLibrariesTabFolder, "Librairie n° " + (v_idx + 1), getDefaultLayout());
			addTextLibraryName(v_item, v_idx);
			addTextLibraryPackage(v_item, v_idx);
			addTextLibraryVersion(v_item, v_idx);
			addTextLibraryPrefix(v_item, v_idx);
			addTextLibrarySchema(v_item, v_idx);
			addRadioLibraryDatabase(v_item, v_idx);
			// else no composite on the first TabItem... why ?
			v_item.setSize(v_useLibrariesTabFolder.getSize());
			_useLibraries.add(new AddLibrary());
		}
		computeValidity();
	}

	/**
	 * Enable or disable the components depending of ws use.
	 */
	private void manageActivationForWS(final boolean p_enable) {

		manageActivationForCrudOperations(p_enable);
		manageActivationForHttpEmbeddedServer(p_enable);
		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_HK2), p_enable);
		computeValidity();
	}

	/**
	 * Enable or disable the components depending of ws use.
	 */
	private void manageActivationForWMS(final boolean p_enable) {

		if (p_enable)
			manageSelectionForRadio(_lstCompositesToManage.get(Id.RD_SOA_WS), Id.RDYES_WS);
		manageActivationForHttpEmbeddedServer(p_enable);
		computeValidity();
	}

	/**
	 * Enable or disable the combo for http server choice.
	 */
	private void manageActivationForHttpEmbeddedServer(final boolean p_enable) {

//		final Composite v_groupEmbdServer = _lstCompositesToManage.get(Id.CB_HTTP_SERVER);
//		Combo v_combo = (Combo) v_groupEmbdServer;
//		v_combo.setEnabled(p_enable);
//		if (!p_enable) {
//			v_combo.select(0);
//			v_combo.notifyListeners(SWT.Selection, new Event());
//		}
	}

	/**
	 * Enable or disable the radio for h2 embedded database.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForH2EmbeddedDatabase(final boolean p_enable) {
		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_EMBEDH2), p_enable);
		computeValidity();
	}

	/**
	 * Enable or disable components for the bdd option tab folfer and .
	 * 
	 * @param p_enable
	 */
	private void manageActivationForBddAndModelisation(final boolean p_enable) {

		manageActivationForCheckBox(_lstCompositesToManage.get(Id.CH_TYPE_BDD), p_enable);
		manageActivationForCheckBox(_lstCompositesToManage.get(Id.CH_MOD_LST_FILES), p_enable);
		manageActivationForText(_lstCompositesToManage.get(Id.ODB_SQL_PREFIX), p_enable);
		manageActivationForText(_lstCompositesToManage.get(Id.ODB_SQL_SCHEMA), p_enable);
		manageActivationForCheckBox(_lstCompositesToManage.get(Id.CH_CHXTOMAJ), p_enable);
		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_CRUD), p_enable);
		manageActivationForSqlNbColumns(p_enable);
	}

	/**
	 * Create the TabItem depending the number of optional columns.
	 * 
	 * @param p_nbColumns
	 */
	private void manageActivationForSqlTabFolderColumns(final int p_nbColumns) {

		TabFolder v_sqlTabFolder = (TabFolder) _lstCompositesToManage.get(Id.ODB_SQL_COLUMNS);
		_sqlAddColumns.clear();

		for (int v_idx = (v_sqlTabFolder.getItemCount() - 1); v_idx > -1; v_idx--) {
			v_sqlTabFolder.getItem(v_idx).dispose();
		}

		for (int v_idx = 0; v_idx < p_nbColumns; v_idx++) {
			Composite v_item = addTabItem(v_sqlTabFolder, "Champ n° " + (v_idx + 1), getDefaultLayout());
			addTextNameSqlColumn(v_item, v_idx);
			addComboTypeSqlColumn(v_item, v_idx);
			addTextSizeSqlColumn(v_item, v_idx);
			addTextDefaultValueSqlColumn(v_item, v_idx);
			addRadioForSqlColumnNullValue(v_item, v_idx);
			addTextCommentSqlColumn(v_item, v_idx);
			// else no composite on the first TabItem... why ?
			v_item.setSize(v_sqlTabFolder.getSize());
			_sqlAddColumns.add(new AddColumn());
		}
		computeValidity();
	}

	/**
	 * Enable or disable the specific generation "v_groupGenDBDD" group, depends of
	 * IHM type.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForJbehaveTest(final boolean p_enable) {

		if (p_enable && _ihm.isEmpty())
			return;

		final Composite v_groupGenDBDD = _lstCompositesToManage.get(Id.RD_DBDD);
		Control[] v_lstControls = v_groupGenDBDD.getChildren();
		for (Control v_control : v_lstControls) {
			Button v_button = (Button) v_control;
			int v_style = v_button.getStyle();
			v_button.setEnabled(p_enable);
			v_button.setSelection(false);
			if ((v_style & SWT.RADIO) != 0) {
				if (Id.RDNO_BDD == v_button.getData())
					v_button.setSelection(true);
			}
		}
	}

	/**
	 * Enable or disable the specific generation for the Ihm choice
	 * 
	 * @param p_enable
	 */
	private void manageActivationForIhm(final boolean p_enable) {

		final Composite v_groupIhm = _lstCompositesToManage.get(Id.CB_TYPE_IHM);
		Combo v_combo = (Combo) v_groupIhm;
		v_combo.setEnabled(p_enable);
		if (!p_enable) {
			v_combo.select(0);
			v_combo.notifyListeners(SWT.Selection, new Event());
		}
	}

	/**
	 * Enable or disable the specific simplified layer for DTO
	 * 
	 * @param p_enable
	 */
	private void manageActivationForSimplifiedLayer(final boolean p_enable) {

		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_MATCH), p_enable);
	}

//	/**
//	 * Enable or disable the use of a configuration files utility framework.
//	 * 
//	 * @param p_enable
//	 */
//	private void manageActivationForUseConfiguration(final boolean p_enable) {
//
//		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_USECONFIG), p_enable);
//	}

	/**
	 * Enable or disable the use of a configuration files utility framework.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForUseConfiguration(final boolean p_enable) {

		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_USECONFIG), p_enable);
	}

	/**
	 * Enable or disable the specific generation for the use bdd choice
	 * 
	 * @param p_enable
	 */
	private void manageActivationForUseBDD(final boolean p_enable) {

		final Composite v_groupUseBdd = _lstCompositesToManage.get(Id.CB_USE_BDD);
		Combo v_combo = (Combo) v_groupUseBdd;
		v_combo.setEnabled(p_enable);
		v_combo.select((p_enable) ? 0 : 1);
		v_combo.notifyListeners(SWT.Selection, new Event());
	}

	/**
	 * Enable or disable the specific generation for all radio.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForRadio(final Composite p_composite, final boolean p_enable) {

		Control[] v_lstControls = p_composite.getChildren();

		for (Control v_control : v_lstControls) {
			v_control.setEnabled(p_enable);
			if (v_control instanceof Button) {
				Button v_button = (Button) v_control;
				v_button.setSelection(false);
				int v_style = v_button.getStyle();
				if ((v_style & SWT.RADIO) != 0) {

					if (Id.isIdType_RD_NO((Id) v_button.getData()))
						v_button.setSelection(true);

					if (p_enable) {

						if (Id.RDYES_TU == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDNO_TU == v_button.getData())
							v_button.setSelection(false);

						if (Id.RDYES_REQ == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDNO_REQ == v_button.getData())
							v_button.setSelection(false);

						if (Id.RDYES_LOG4J == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDYES_HK2 == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDNO_EMBEDH2 == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDNO_LOG4J == v_button.getData())
							v_button.setSelection(false);

						if (Id.RDNO_HK2 == v_button.getData())
							v_button.setSelection(false);

						if (Id.RDYES_REQVERSION == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDNO_REQVERSION == v_button.getData())
							v_button.setSelection(false);

						if (Id.RDNO_CRUD == v_button.getData())
							v_button.setSelection(true);

						if (Id.RDYES_CRUD == v_button.getData())
							v_button.setSelection(false);
					}
				}
				v_button.notifyListeners(SWT.Selection, new Event());
			}
		}
	}

	/**
	 * Enable or disable the specific generation for all check boxs.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForCheckBox(final Composite p_composite, final boolean p_enable) {

		Control[] v_lstControls = p_composite.getChildren();

		for (Control v_control : v_lstControls) {
			Button v_button = (Button) v_control;
			int v_style = v_button.getStyle();
			v_button.setEnabled(p_enable);
			v_button.setSelection(false);
			if ((v_style & SWT.CHECK) != 0) {
				if (p_enable) {

					if (Id.CHBT_H2 == v_button.getData()) {
						v_button.setSelection(true);
						v_button.setEnabled(false);
					}

					if (Id.CHBT_SQLSERVER == v_button.getData())
						v_button.setEnabled(false);

					if (Id.CHBT_ENTITY == v_button.getData()) {
						v_button.setSelection(true);
						v_button.setEnabled(false);
					}

					if (Id.CHBT_DATABASE == v_button.getData())
						v_button.setEnabled(true);
				} else {

					if (Id.CHBT_SOAP == v_button.getData())
						v_button.setSelection(true);
				}
				v_button.notifyListeners(SWT.Selection, new Event());
			}
		}
	}

	/**
	 * Enable or disable the specific generation for all text fields.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForText(final Composite p_composite, final boolean p_enable) {
		Control[] v_lstControls = p_composite.getChildren();

		for (Control v_control : v_lstControls) {
			if (v_control instanceof Text) {
				Text v_fied = (Text) v_control;
				v_fied.setEnabled(p_enable);
				v_fied.setEditable(p_enable);
				// v_fied.setText("");
				v_fied.notifyListeners(SWT.KeyUp, new Event());
			}
		}
	}

	/**
	 * Enable or disable the specific generation for crud operations.
	 * 
	 * @param p_boolean p_enable
	 */
	private void manageActivationForCrudOperations(final boolean p_enable) {

		manageActivationForRadio(_lstCompositesToManage.get(Id.RD_CRUD), p_enable);
	}

	/**
	 * Enable or disable several groups, depends of Rest importation library.
	 * 
	 * @param p_enable
	 */
	private void manageActivationForRsLibrary(final boolean p_enable) {

		for (Entry<Id, Composite> v_entry : _lstCompositesToManage.entrySet()) {

			// Activation / Inactivation for the radio buttons.
			if (Id.isIdType_RD(v_entry.getKey())) {
				manageActivationForRadio(v_entry.getValue(), p_enable);
			}

			// Activation / Inactivation for the check boxes.
			if (Id.isIdType_CH(v_entry.getKey())) {
				manageActivationForCheckBox(v_entry.getValue(), p_enable);
			}

			// Activation / Inactivation for the text fields.
			if (Id.isIdType_TX(v_entry.getKey())) {
				manageActivationForText(v_entry.getValue(), p_enable);
			}
		}

		// Specific code.
		manageActivationForIhm(p_enable);
		manageActivationForJbehaveTest(p_enable);
		manageActivationForSqlPrefix(p_enable);
		manageActivationForSqlNbColumns(p_enable);
		manageActivationForSqlTableSchema(p_enable);
		manageActivationForLibraries(p_enable);
		manageActivationForSqlTableSpace();
		manageActivationForHttpEmbeddedServer(p_enable);
		manageActivationForUseBDD(p_enable);
		manageActivationForUseConfiguration(p_enable);
		manageActivationForH2EmbeddedDatabase(p_enable);

		// Inactive in all cases.
		manageActivationForCrudOperations(false);
		manageActivationForSimplifiedLayer(false);
		computeValidity();
	}

	/**
	 * Makes it possible to specifically target a radio button among the list of
	 * composites to manage and automatically select the "yes" option.
	 * 
	 * @param p_composite the id for the composite.
	 * @param p_controlId the id for the radio button.
	 */
	private void manageSelectionForRadio(final Composite p_composite, final PropertiesPage2.Id p_controlId) {

		Control[] v_lstControls = p_composite.getChildren();

		for (Control v_control : v_lstControls) {
			if (v_control instanceof Button) {
				Button v_button = (Button) v_control;
				int v_style = v_button.getStyle();
				if ((v_style & SWT.RADIO) != 0) {
					v_button.setSelection(false);
					if (p_controlId.equals((Id) v_button.getData())) {
						v_button.setSelection(true);
						v_button.notifyListeners(SWT.Selection, new Event());
					}
				}
			}
		}
	}

	/**
	 * Makes it possible to specifically target a check box among the list of
	 * composites to manage and automatically select.
	 * 
	 * @param p_composite the id for the composite.
	 * @param p_controlId the id for the check box.
	 */
	private void manageSelectionForCheckBox(final Composite p_composite, final PropertiesPage2.Id p_controlId) {

		Control[] v_lstControls = p_composite.getChildren();

		for (Control v_control : v_lstControls) {
			if (v_control instanceof Button) {
				Button v_button = (Button) v_control;
				int v_style = v_button.getStyle();
				if ((v_style & SWT.CHECK) != 0) {
					if (p_controlId.equals((Id) v_button.getData())) {
						v_button.setSelection(true);
						v_button.notifyListeners(SWT.Selection, new Event());
					}
				}
			}
		}
	}

	/**
	 * Choice of package name.
	 */
	private void addTextPackage(final Composite p_container) {

		_packageName = addTextWithLabel(p_container, "Package de l'application", c_PROJECT_PACKAGE_DEFAULT,
				"Le nom pour le package racine de l'application sous lequel"
						+ "\npositionner les classes et l'ensemble des autres packages.");

		_packageName.addKeyListener(new KeyListener() {

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
	}

	/**
	 * Choice of project name.
	 */
	private void addTextApplication(final Composite p_container) {

		final Text v_applicationName = addTextWithLabel(p_container, "Nom du projet", "",
				"Le nom du projet avec lequel le squelette de l'application sera généré.");
		v_applicationName.setFocus();

		v_applicationName.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_applicationName = v_applicationName.getText();
				FormUtil.completePackageName(_packageName, v_applicationName);
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForProjectName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioHttpServer(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupTU = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.CB_HTTP_SERVER, v_groupTU);

		v_label.setText("Serveur HTTP embarqué");

		Button v_ouiButton = new Button(v_groupTU, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_TU);

		Button v_nonButton = new Button(v_groupTU, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_TU);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_httpEmbeddedServer = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_httpEmbeddedServer = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Display of the current SPI4J version (just for info).
	 */
	private void addSPI4JVersion(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setText("Version de la librairie SPI4J");

		Label v_spi4J = new Label(p_container, SWT.NONE);
//		FontData v_fontData = v_spi4J.getFont().getFontData()[0];
//		Font font = new Font(p_container.getDisplay(),
//				new FontData(v_fontData.getName(), v_fontData.getHeight(), SWT.BOLD));
//		v_spi4J.setFont(font);
		v_spi4J.setText(_spi4jVersion);
	}

	/**
	 * 
	 * @param p_container
	 * @param p_tabIdx
	 */
	private void addComboUseBDD(final Composite p_container) {

		final Combo v_useBdd = addComboBoxWithLabel(p_container, "Utilisation de base(s) de données", c_USE_BDD_LIST, 0,
				"Inqique si le projet est en relation avec une base de données.");
		_lstCompositesToManage.put(Id.CB_USE_BDD, v_useBdd);

		v_useBdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int v_selectedIndex = v_useBdd.getSelectionIndex();
				if (v_selectedIndex == 1) {
					_bdNames.clear();
					manageActivationForBddAndModelisation(false);
				} else {
					// Si videe precedement on remet la base H2.
					if (_bdNames.isEmpty())
						_bdNames.add(c_H2);

					manageActivationForBddAndModelisation(true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// RAS.
			}
		});
	}

	/**
	 * Choice of implementation databases. As these are names of product, we don't
	 * put them in the TextUtil class
	 */
	private void addCheckBoxDataBase(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.PUSH);
		v_label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		v_label.setToolTipText("La (ou les) base(s) de données à utiliser dans le cadre du projet."
				+ "\nLa base H2 est toujours utilisée pour les tests unitaires");
		v_label.setText("Type(s) de base(s) de données");

		Composite v_groupDatabase = addGroupCheckBox(p_container);
		_lstCompositesToManage.put(Id.CH_TYPE_BDD, v_groupDatabase);

		Button v_btnOracle32 = new Button(v_groupDatabase, SWT.CHECK);
		v_btnOracle32.setText(c_ORACLE + " (Version < 12.2)");
		v_btnOracle32.setData(Id.CHBT_ORACLE_32);

		v_btnOracle32.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (_bdNames.indexOf(c_ORACLE_32) == -1 && v_selected) {
					_bdNames.add(c_ORACLE_32);
				} else {
					_bdNames.remove(c_ORACLE_32);
				}
				manageActivationForSqlTableSpace();
				computeValidity();
			}
		});

		Button v_btnOracle = new Button(v_groupDatabase, SWT.CHECK);
		v_btnOracle.setText(c_ORACLE + " (Version > 12.1)");
		v_btnOracle.setData(Id.CHBT_ORACLE);

		v_btnOracle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (_bdNames.indexOf(c_ORACLE) == -1 && v_selected) {
					_bdNames.add(c_ORACLE);
				} else {
					_bdNames.remove(c_ORACLE);
				}
				manageActivationForSqlTableSpace();
				computeValidity();
			}
		});

		Button v_btnPostgresql = new Button(v_groupDatabase, SWT.CHECK);
		v_btnPostgresql.setText(c_POSTGRESQL);
		v_btnPostgresql.setData(Id.CHBT_POSTGRES);

		v_btnPostgresql.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (_bdNames.indexOf(c_POSTGRESQL) == -1 && v_selected)
					_bdNames.add(c_POSTGRESQL);
				else
					_bdNames.remove(c_POSTGRESQL);
				computeValidity();
			}
		});

		Button v_btnMysql = new Button(v_groupDatabase, SWT.CHECK);
		v_btnMysql.setText(c_MYSQL);
		v_btnMysql.setData(Id.CHBT_MYSQL);

		v_btnMysql.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (_bdNames.indexOf(c_MYSQL) == -1 && v_selected)
					_bdNames.add(c_MYSQL);
				else
					_bdNames.remove(c_MYSQL);
				computeValidity();
			}
		});

		Button v_btnMariaDB = new Button(v_groupDatabase, SWT.CHECK);
		v_btnMariaDB.setText(c_MARIA_DB);
		v_btnMariaDB.setData(Id.CHBT_MARIADB);

		v_btnMariaDB.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (_bdNames.indexOf(c_MARIA_DB) == -1 && v_selected)
					_bdNames.add(c_MARIA_DB);
				else
					_bdNames.remove(c_MARIA_DB);
				computeValidity();
			}
		});

		// Le bouton est maintenant toujours desactive.
		Button v_btnH2 = new Button(v_groupDatabase, SWT.CHECK);
		v_btnH2.setText(c_H2);
		v_btnH2.setData(Id.CHBT_H2);

		v_btnH2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

//				// Check if button is really selected.
//				boolean v_selected = ((Button) p_e.getSource()).getSelection();
//
//				if (_bdNames.indexOf(c_H2) == -1 && v_selected)
//					_bdNames.add(c_H2);
//				else
//					_bdNames.remove(c_H2);
//				computeValidity();
			}
		});

		_bdNames.add(c_H2);
		v_btnH2.setSelection(true);
		v_btnH2.setEnabled(false);
	}

	/**
	 * Radio button for the choice of generation of unit tests classes for CRUD
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioTU(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupTU = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_TU, v_groupTU);

		v_label.setText("Tests unitaires pour les services CRUD");

		Button v_ouiButton = new Button(v_groupTU, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_TU);

		Button v_nonButton = new Button(v_groupTU, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_TU);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_unitTests = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_unitTests = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for the choice of generation of service classes implementation
	 * of requirements.
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioSrvReq(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupSrvReq = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_REQUIREMENT, v_groupSrvReq);
		v_label.setText("Implémentation des services des exigences");

		Button v_ouiButton = new Button(v_groupSrvReq, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_REQ);

		Button v_nonButton = new Button(v_groupSrvReq, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_REQ);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_srvReq = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_srvReq = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for the choice of WebServices generation.
	 * 
	 * @param p_container
	 */
	private void addRadioWS(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupWS = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_SOA_WS, v_groupWS);
		v_label.setText("Génération de Webservices (SOAP/REST)");

		Button v_ouiButton = new Button(v_groupWS, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_WS);

		Button v_nonButton = new Button(v_groupWS, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_WS);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_srvWS = c_BOOL_STR_YES;
					manageActivationForWS(true);
				}
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_srvWS = c_BOOL_STR_NO;
					manageActivationForWS(false);
				}
			}
		});
	}

//	/**
//	 * Radio button for the choice of MicroServices generation.
//	 * 
//	 * @param p_container
//	 */
//	private void addRadioWMS(final Composite p_container) {
//
//		Label v_label = new Label(p_container, SWT.NONE);
//		Composite v_groupWMS = addGroupRadio(p_container);
//		_lstCompositesToManage.put(Id.RD_SOA_WMS, v_groupWMS);
//		v_label.setText(c_LBL_SOA_WMS);
//
//		Button v_ouiButton = new Button(v_groupWMS, SWT.RADIO);
//		v_ouiButton.setText(c_LBL_YES);
//		v_ouiButton.setData(Id.RDYES_WMS);
//
//		Button v_nonButton = new Button(v_groupWMS, SWT.RADIO);
//		v_nonButton.setText(c_LBL_NO);
//		v_nonButton.setSelection(true);
//		v_nonButton.setData(Id.RDNO_WMS);
//
//		v_ouiButton.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(final SelectionEvent p_e) {
//
//				if (((Button) p_e.getSource()).getSelection()) {
//					_srvWMS = c_BOOL_STR_YES;
//					manageActivationForWMS(true);
//				}
//			}
//		});
//
//		v_nonButton.addSelectionListener(new SelectionAdapter() {
//
//			@Override
//			public void widgetSelected(final SelectionEvent p_e) {
//
//				if (((Button) p_e.getSource()).getSelection()) {
//					_srvWMS = c_BOOL_STR_NO;
//					manageActivationForWMS(false);
//				}
//			}
//		});
//	}

	/**
	 * Radio button for choosing the use of secure services (subject to
	 * authorization for the user).
	 * 
	 * @param p_container
	 */
	private void addRadioUseSecurity(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setToolTipText(
				"Utilisation de la couche spécifique spi4j pour la gestion " + "des rôles et des utilisateurs.");
		v_label.setText("Implémentation de la sécurité");
		Composite v_groupSecurity = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_SECURITY, v_groupSecurity);

		Button v_ouiButton = new Button(v_groupSecurity, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_SECU);

		Button v_nonButton = new Button(v_groupSecurity, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_SECU);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_useSecurity = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_useSecurity = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for the choice of generation of behavior tests.
	 * 
	 * @param p_container
	 */
	private void addRadioGenBDD(final Composite p_container) {

		_labelBDD = new Label(p_container, SWT.NONE);
		_labelBDD.setText("Génération de tests de comportement");
		_labelBDD.setToolTipText("Utilisation de scripts de type Jbehave pour les tests de comportement.");
		Composite v_groupGenDBDD = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_DBDD, v_groupGenDBDD);
		v_groupGenDBDD.setToolTipText("Utilisation de scripts de type Jbehave pour les tests de comportement.");

		Button v_ouiButton = new Button(v_groupGenDBDD, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_BDD);

		Button v_nonButton = new Button(v_groupGenDBDD, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_BDD);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_genBDD = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_genBDD = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for choosing the coding of carriage returns in the application
	 * generated.
	 * 
	 * @param p_container
	 */
	private void addRadioRC(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupRC = addGroupRadio(p_container);
		v_label.setText("Retour chariot souhaité");

		Button v_rnButton = new Button(v_groupRC, SWT.RADIO);
		v_rnButton.setText("Windows (\\r\\n)");
		v_rnButton.setSelection(true);

		Button v_nButton = new Button(v_groupRC, SWT.RADIO);
		v_nButton.setText("Linux (\\n)");

		v_rnButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_typeRC = "Windows (\\r\\n)";
			}
		});

		v_nButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_typeRC = c_CARRIAGE_RETURN_L;
			}
		});
	}

	/**
	 * Radio button for use of configuration files utility.
	 * 
	 * @param p_container
	 */
	private void addRadioUseConfig(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupRCG = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_USECONFIG, v_groupRCG);
		v_label.setToolTipText("Utilisation SPI4J pour la gestion des fichiers de configuration de l'application."
				+ "\nDifférents formats supportés (xml, json, yaml, etc...)");
		v_label.setText("Fichiers de config gérés par Spi4j");

		Button v_yesButton = new Button(v_groupRCG, SWT.RADIO);
		v_yesButton.setText(c_LBL_YES);
		v_yesButton.setData(Id.RDYES_USECONFIG);

		Button v_noButton = new Button(v_groupRCG, SWT.RADIO);
		v_noButton.setText(c_LBL_NO);
		v_noButton.setData(Id.RDNO_USECONFIG);
		v_noButton.setSelection(true);

		v_yesButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_useConfig = c_BOOL_STR_YES;
			}
		});

		v_noButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_useConfig = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button to find out if the project is a library.
	 * 
	 * @param p_container
	 */
	private void addRadioIsLibrary(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupIsLibrary = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_LIBRARY, v_groupIsLibrary);
		v_label.setText("Le projet est une librairie");
		v_label.setToolTipText("Le projet est un fichier jar réutilisable dans un autre projet.");

		Button v_ouiButton = new Button(v_groupIsLibrary, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_LIB);

		Button v_nonButton = new Button(v_groupIsLibrary, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_LIB);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_isLibrary = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_isLibrary = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button to know if the project is a Rest import library (Swagger).
	 * 
	 * @param p_container
	 */
	private void addRadioIsLibraryRs(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupIsLibraryRs = addGroupRadio(p_container);
		v_label.setText("Le projet est une librairie 'Import REST'");
		v_label.setToolTipText(
				"Le projet est un fichier jar dédié aux services REST, réutilisable dans un autre projet."
						+ "La modélisation est déduite d'un import de fichier au format swagger\n");

		Button v_ouiButton = new Button(v_groupIsLibraryRs, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);

		Button v_nonButton = new Button(v_groupIsLibraryRs, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_isLibraryRs = c_BOOL_STR_YES;
					manageActivationForRsLibrary(false);
				}
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_isLibraryRs = c_BOOL_STR_NO;
					manageActivationForRsLibrary(true);
				}
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTextSqlTablePrefix(final Composite p_container) {
		final Text v_tablePrefix = addTextWithLabel(addGroupGeneric(p_container), "Préfixe pour l'ensemble des tables",
				"", "Ajouter automatiquement un préfixe devant le nom de l'ensemble des tables."
						+ "\nLa syntaxe du préfixe est avec toujours un underscore : 'XX_'.");
		_lstCompositesToManage.put(Id.ODB_SQL_PREFIX, v_tablePrefix.getParent()); // Text is not a composite !
		v_tablePrefix.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTablePrefix = v_tablePrefix.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	private void addTextSqlTableSpace(final Composite p_container) {

		final Text v_tableSpace = addTextWithLabel(addGroupGeneric(p_container), "Tablespace pour les index (Oracle)",
				"", "");
		_lstCompositesToManage.put(Id.ODB_SQL_TABLESPACE, v_tableSpace.getParent());

		v_tableSpace.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTableSpace = v_tableSpace.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});

		v_tableSpace.setEnabled(false);
	}

	private void addTextSqlTableSchema(final Composite p_container) {

		final Text v_tableSchema = addTextWithLabel(addGroupGeneric(p_container), "Schéma pour l'ensemble des tables",
				"", "Ajouter automatiquement un schema devant le nom de l'ensemble des tables."
						+ "\nLa syntaxe du schéma est toujours avec un point : 'XX.'.");
		_lstCompositesToManage.put(Id.ODB_SQL_SCHEMA, v_tableSchema.getParent());

		v_tableSchema.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_sqlTableSchema = v_tableSchema.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForSchema(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addComboSqlColumns(final Composite p_container) {

		final Combo v_nbColumns = addComboBoxWithLabel(addGroupGeneric(p_container), "Champs supplémentaires libres",
				c_SQL_NB_COLUMNS_LIST, 0, "Ajouter automatiquement un champ pour l'ensemble des tables.");

		_lstCompositesToManage.put(Id.ODB_SQL_NBCOLUMNS, v_nbColumns);

		v_nbColumns.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				int v_sqlNbColumns = v_nbColumns.getSelectionIndex();
				if (-1 == v_sqlNbColumns)
					v_sqlNbColumns = 0;

				manageActivationForSqlTabFolderColumns(v_sqlNbColumns);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTabFolderSqlColumns(final Composite p_container) {

		final TabFolder v_sqlColumnsTabFolder = new TabFolder(p_container, SWT.NONE);
		v_sqlColumnsTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		v_sqlColumnsTabFolder.setBackground(p_container.getParent().getBackground());
		_lstCompositesToManage.put(Id.ODB_SQL_COLUMNS, v_sqlColumnsTabFolder);
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTabFolderUseLibraries(final Composite p_container) {

		final TabFolder v_useLibrariesTabFolder = new TabFolder(p_container, SWT.NONE);
		v_useLibrariesTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		v_useLibrariesTabFolder.setBackground(p_container.getParent().getBackground());
		_lstCompositesToManage.put(Id.LIB_USE_LIBRARY, v_useLibrariesTabFolder);
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTextNameSqlColumn(final Composite p_container, int p_tabIdx) {

		final Text v_columnName = addTextWithLabel(p_container, "Nom", "", "");
		v_columnName.setData(c_TABIDX, p_tabIdx);

		v_columnName.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddColumn) _sqlAddColumns.get((int) v_columnName.getData(c_TABIDX)))._name = v_columnName.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForSqlColumnName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addComboTypeSqlColumn(final Composite p_container, final int p_tabIdx) {

		final Combo v_columnType = addComboBoxWithLabel(p_container, "Type", c_SQL_TYPE_COLUMN_LIST, 0, "");
		v_columnType.setData(c_TABIDX, p_tabIdx);

		v_columnType.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {
				int v_selectedIndex = v_columnType.getSelectionIndex();
				((AddColumn) _sqlAddColumns.get((int) v_columnType.getData(c_TABIDX)))._type = v_columnType
						.getItem(v_selectedIndex);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});

		v_columnType.select(7);
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTextSizeSqlColumn(final Composite p_container, final int p_tabIdx) {

		final Text v_columnSize = addTextWithLabel(p_container, "Taille", "", "");
		v_columnSize.setData(c_TABIDX, p_tabIdx);

		v_columnSize.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddColumn) _sqlAddColumns.get((int) v_columnSize.getData(c_TABIDX)))._size = v_columnSize.getText();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTextDefaultValueSqlColumn(final Composite p_container, final int p_tabIdx) {

		final Text v_columnDefault = addTextWithLabel(p_container, "Valeur par défaut", "", "");
		v_columnDefault.setData(c_TABIDX, p_tabIdx);

		v_columnDefault.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddColumn) _sqlAddColumns.get((int) v_columnDefault.getData(c_TABIDX)))._default = v_columnDefault
						.getText();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				// RAS.
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addTextCommentSqlColumn(final Composite p_container, final int p_tabIdx) {

		final Text v_columnComment = addTextWithLabel(p_container, "Commentaire", "", "");
		v_columnComment.setData(c_TABIDX, p_tabIdx);

		v_columnComment.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddColumn) _sqlAddColumns.get((int) v_columnComment.getData(c_TABIDX)))._comment = v_columnComment
						.getText();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				// RAS.
			}
		});
	}

	/**
	 * 
	 * @param p_container
	 */
	private void addRadioForSqlColumnNullValue(final Composite p_container, final int p_tabIdx) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupNullValue = addGroupRadio(p_container);
		v_groupNullValue.setData(c_TABIDX, p_tabIdx);

		v_label.setText("Accepte la valeur nul");

		Button v_ouiButton = new Button(v_groupNullValue, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_TU);

		Button v_nonButton = new Button(v_groupNullValue, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_TU);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					((AddColumn) _sqlAddColumns.get((int) v_groupNullValue.getData(c_TABIDX)))._notNull = Boolean
							.toString(false);
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					((AddColumn) _sqlAddColumns.get((int) v_groupNullValue.getData(c_TABIDX)))._notNull = Boolean
							.toString(true);
			}
		});
	}

	/**
	 * Choice of library package.
	 */
	private void addTextLibraryPackage(final Composite p_container, int p_tabIdx) {

		final Text v_packageName = addTextWithLabel(p_container, "Package de la librairie (GroupId)", "", "");
		v_packageName.setData(c_TABIDX, p_tabIdx);

		v_packageName.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddLibrary) _useLibraries.get((int) v_packageName.getData(c_TABIDX)))._package = v_packageName
						.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPackageName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Choice of the name of the bookstore.
	 */
	private void addTextLibraryName(final Composite p_container, int p_tabIdx) {

		final Text v_name = addTextWithLabel(p_container, "Nom de la librairie (ArtifactId)", "", "");
		v_name.setData(c_TABIDX, p_tabIdx);

		v_name.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddLibrary) _useLibraries.get((int) v_name.getData(c_TABIDX)))._name = v_name.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForLibraryName(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Choice of the library version.
	 */
	private void addTextLibraryVersion(final Composite p_container, int p_tabIdx) {

		final Text v_version = addTextWithLabel(p_container, "Version de la librairie", "", "");
		v_version.setData(c_TABIDX, p_tabIdx);

		v_version.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddLibrary) _useLibraries.get((int) v_version.getData(c_TABIDX)))._version = v_version.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForLibraryVersion(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Choice of the library database table prefix.
	 */
	private void addTextLibraryPrefix(final Composite p_container, int p_tabIdx) {

		final Text v_prefix = addTextWithLabel(p_container, "Préfixe pour les tables de la base de données", "",
				"Ajouter automatiquement un préfixe devant le nom de l'ensemble des tables."
						+ "\nLa syntaxe du préfixe est avec toujours un underscore : 'XX_'.");
		v_prefix.setData(c_TABIDX, p_tabIdx);

		v_prefix.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddLibrary) _useLibraries.get((int) v_prefix.getData(c_TABIDX)))._prefix = v_prefix.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Choice of the library database schema.
	 */
	private void addTextLibrarySchema(final Composite p_container, int p_tabIdx) {

		final Text v_schema = addTextWithLabel(p_container, "Schéma pour la base de données", "",
				"Ajouter automatiquement un schema devant le nom de l'ensemble des tables."
						+ "\nLa syntaxe du schéma est toujours avec un point : 'XX.'.");
		v_schema.setData(c_TABIDX, p_tabIdx);

		v_schema.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				((AddLibrary) _useLibraries.get((int) v_schema.getData(c_TABIDX)))._schema = v_schema.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForSchema(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Does the library needs use a database connection ?
	 */
	private void addRadioLibraryDatabase(final Composite p_container, int p_tabIdx) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupLD = addGroupRadio(p_container);
		v_groupLD.setData(c_TABIDX, p_tabIdx);

		// _lstCompositesToManage.put(Id.RD_TU, v_groupTU);

		v_label.setText("Accède à la base de données");

		Button v_ouiButton = new Button(v_groupLD, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);

		Button v_nonButton = new Button(v_groupLD, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					((AddLibrary) _useLibraries.get((int) v_groupLD.getData(c_TABIDX)))._useDatabase = c_BOOL_STR_YES;
					computeValidity();
				}
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					((AddLibrary) _useLibraries.get((int) v_groupLD.getData(c_TABIDX)))._useDatabase = c_BOOL_STR_NO;
					computeValidity();
				}
			}
		});

//		manageActivationForLibrarySqlFields(Boolean.valueOf(((AddLibrary) _useLibraries
//				.get((int) v_groupLD.getData(c_TABIDX)))._useDatabase), (int) v_groupLD.getData(c_TABIDX));

		// Default initialization.

	}

//	/**
//	 * Choice of Java version.
//	 */
//	private void addComboJavaVersion(final Composite p_container) {
//
//		final Combo v_java = addComboBoxWithLabel(p_container, "Version pour la compilation JAVA", c_JAVA_VERSION_LIST,
//				0, "");
//
//		v_java.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(final SelectionEvent p_e) {
//				int v_selectedIndex = v_java.getSelectionIndex();
//
//				if (v_selectedIndex == -1)
//					return;
//
//				_javaVersion = c_JAVA_VERSION.get(v_java.getItem(v_selectedIndex));
//			}
//
//			@Override
//			public void widgetDefaultSelected(final SelectionEvent p_e) {
//				widgetSelected(p_e);
//			}
//		});
//
//		_javaVersion = c_JAVA_VERSION.get(v_java.getItem(0));
//	}

	/**
	 * Set the author name for the project.
	 */
	private void addTextAuthorName(final Composite p_container) {

		final Text v_author = addTextWithLabel(p_container, "Auteur", "",
				"Le nom de l'auteur ou de l'organisme à indiquer pour l'ensemble des commentaires.");
		_lstCompositesToManage.put(Id.TX_AUTHOR, v_author.getParent());
		v_author.setText(_authorName);

		v_author.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_authorName = v_author.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				// RAS.
			}
		});
	}

	/**
	 * Choice of requirement prefix.
	 */
	private void addTextRequirementPrefix(final Composite p_container) {

		final Text v_requirementPrefix = addTextWithLabel(p_container, "Requirements - Préfixe", "",
				"Ajout automatique d'un préfixe devant l'ensemble de règles de gestion.");
		_lstCompositesToManage.put(Id.TX_REQ_PREFIX, v_requirementPrefix.getParent());
		v_requirementPrefix.setText(_requirementPrefix);

		v_requirementPrefix.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_requirementPrefix = v_requirementPrefix.getText();
				computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForPrefix(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Choice of requirement level.
	 */
	private void addTextRequirementLevel(final Composite p_container) {

		final Text v_requirementLevel = addTextWithLabel(p_container, "Requirements - Niveaux", "", "");
		_lstCompositesToManage.put(Id.TX_REQ_LEVEL, v_requirementLevel.getParent());
		v_requirementLevel.setText(_requirementLevel);

		v_requirementLevel.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent p_e) {
				_requirementLevel = v_requirementLevel.getText();
				// computeValidity();
			}

			@Override
			public void keyPressed(final KeyEvent p_e) {
				if (!FormUtil.checkKeyForNumericValue(p_e.character)) {
					p_e.doit = false;
				}
			}
		});
	}

	/**
	 * Radio button to activate / inactivate debug mode.
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioModeDebug(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupModeDebug = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_DEBUG, v_groupModeDebug);
		v_label.setText("Mode debug");

		Button v_ouiButton = new Button(v_groupModeDebug, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_DEBUG);

		Button v_nonButton = new Button(v_groupModeDebug, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_DEBUG);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_modeDebug = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_modeDebug = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for generation fetching strategy.
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioFecthingStrategy(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupFetchStrategy = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_FETCHSTRATEGY, v_groupFetchStrategy);
		v_label.setText("Fetching Strategy");

		Button v_ouiButton = new Button(v_groupFetchStrategy, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_FETCH);

		Button v_nonButton = new Button(v_groupFetchStrategy, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_FETCH);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_fetchingStrategy = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_fetchingStrategy = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for generation of log4j properties files.
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioLog4j(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupLog4j = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_LOG4J, v_groupLog4j);
		v_label.setText("Générer les fichiers log4j2.xml");

		Button v_ouiButton = new Button(v_groupLog4j, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_LOG4J);

		Button v_nonButton = new Button(v_groupLog4j, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_LOG4J);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_log4J = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_log4J = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for activate the cdi injection (hk2 of jersey).
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioHk2(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		v_label.setToolTipText("Uilisation CDI HK2 (Jersey) ou de simple fabriques (pattern factory).");
		Composite v_groupHk2 = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_HK2, v_groupHk2);
		v_label.setText("Api Rest - Utilisation injection CDI");

		Button v_ouiButton = new Button(v_groupHk2, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_HK2);

		Button v_nonButton = new Button(v_groupHk2, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_HK2);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_hk2 = c_BOOL_STR_YES;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_hk2 = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Radio button for activate embedded h2 database (for poc).
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioH2Embedded(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupH2 = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_EMBEDH2, v_groupH2);
		v_label.setText("Base H2 embarquée");

		Button v_ouiButton = new Button(v_groupH2, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_EMBEDH2);

		Button v_nonButton = new Button(v_groupH2, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setData(Id.RDNO_EMBEDH2);
		v_nonButton.setSelection(true);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_eh2 = c_BOOL_STR_YES;
					computeValidity();
				}
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_eh2 = c_BOOL_STR_NO;
					computeValidity();
				}
			}
		});
	}

	/**
	 * Choice for initialization of requirement version.
	 */
	private void addRadioRequirementVersion(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupReqVersion = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_REQVERSION, v_groupReqVersion);
		v_label.setText("Requirements - Init. Version");

		Button v_ouiButton = new Button(v_groupReqVersion, SWT.RADIO);
		v_ouiButton.setText(c_LBL_NONE);
		v_ouiButton.setSelection(true);
		v_ouiButton.setData(Id.RDYES_REQVERSION);

		Button v_nonButton = new Button(v_groupReqVersion, SWT.RADIO);
		v_nonButton.setText(c_LBL_CURRENT);
		v_nonButton.setData(Id.RDNO_REQVERSION);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_requirementVersion = c_VERSION_INIT_NONE;
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_requirementVersion = c_VERSION_INIT_CURRENT;
			}
		});
	}

	/**
	 * Add combo box for additional libraries
	 */
	private void addComboUseLibraries(final Composite p_container) {

		final Combo v_nbUseLibraries = addComboBoxWithLabel(addGroupGeneric(p_container),
				"Librairies supplémentaires libres", c_SQL_NB_LIBRARIES_LIST, 0,
				"Le projet utilise des librairies externes à rajouter\n"
						+ "automatiquement comme dépendance dans les fichiers Maven.");

		_lstCompositesToManage.put(Id.LIB_USE_NBCOLUMNS, v_nbUseLibraries);

		v_nbUseLibraries.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				int v_nbLibraries = v_nbUseLibraries.getSelectionIndex();
				if (-1 == v_nbLibraries)
					v_nbLibraries = 0;

				manageActivationForUseLibraries(v_nbLibraries);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent p_e) {
				widgetSelected(p_e);
			}
		});
	}

	/**
	 * Add checkBoxs for particular additional fields (xtopsup et xdmaj)
	 */
	private void addCheckBoxSqlXtopSupXdMaj(final Composite p_container) {

		Composite v_groupMain = addGroupGeneric(p_container);

		Label v_label = new Label(v_groupMain, SWT.NONE);
		v_label.setToolTipText("Ajouter automatiquement un champ pour l'ensemble des tables."
				+ "\nTimestamp de mise à jour (et/ou) Indicateur de suppression (delete physique interdit pour la base).");
		v_label.setText("Champs supplémentaires spécifiques");

		Composite v_groupXtopSupXdMaj = addGroupRadio(v_groupMain);
		_lstCompositesToManage.put(Id.CH_CHXTOMAJ, v_groupXtopSupXdMaj);

		final Button v_btnXdMaj = new Button(v_groupXtopSupXdMaj, SWT.CHECK);
		v_btnXdMaj.setText("Mise à jour");
		v_btnXdMaj.setToolTipText("Date de mise à jour");

		v_btnXdMaj.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (v_selected) {
					AddColumn v_xdmaj = new AddColumn();
					v_xdmaj._comment = "Date de mise a jour de la ligne.";
					v_xdmaj._name = c_XDMAJ;
					v_xdmaj._notNull = c_BOOL_STR_YES;
					v_xdmaj._default = c_DEFAULT_SQL_DATE;
					v_xdmaj._type = c_SQL_TYPE_COLUMN_LIST[2];
					_sqlReservedAddColumns.add(v_xdmaj);
				} else {
					for (int v_i = 0; v_i < _sqlReservedAddColumns.size(); v_i++) {
						if (((AddColumn) _sqlReservedAddColumns.get(v_i))._name.equals(c_XDMAJ)) {
							_sqlReservedAddColumns.remove(v_i);
							break;
						}
					}
				}
			}
		});

		final Button v_btnXtopSup = new Button(v_groupXtopSupXdMaj, SWT.CHECK);
		v_btnXtopSup.setText("Supp. logique");
		v_btnXtopSup.setToolTipText("Suppression logique");

		v_btnXtopSup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent p_e) {

				// Check if button is really selected.
				boolean v_selected = ((Button) p_e.getSource()).getSelection();

				if (v_selected) {
					AddColumn v_xtopsup = new AddColumn();
					v_xtopsup._comment = "Indicateur de suppression logique.";
					v_xtopsup._name = c_XTOPSUP;
					v_xtopsup._default = c_BOOL_STR_0;
					v_xtopsup._notNull = c_BOOL_STR_YES;
					// Type spécifique pour Xtopsup...
					v_xtopsup._type = "XtopSup";
					v_xtopsup._size = "1";
					_sqlReservedAddColumns.add(v_xtopsup);
				} else {
					for (int v_i = 0; v_i < _sqlReservedAddColumns.size(); v_i++) {
						if (((AddColumn) _sqlReservedAddColumns.get(v_i))._name.equals(c_XTOPSUP)) {
							_sqlReservedAddColumns.remove(v_i);
							break;
						}
					}
				}
			}
		});

		v_btnXdMaj.setSelection(false);
		v_btnXtopSup.setSelection(false);
	}

	/**
	 * Radio button to activate tests for CRUD.
	 * 
	 * @param p_container
	 * @return
	 */
	private void addRadioCrud(final Composite p_container) {

		Label v_label = new Label(p_container, SWT.NONE);
		Composite v_groupCRUD = addGroupRadio(p_container);
		_lstCompositesToManage.put(Id.RD_CRUD, v_groupCRUD);
		v_label.setText("Génération application CRUD");

		Button v_ouiButton = new Button(v_groupCRUD, SWT.RADIO);
		v_ouiButton.setText(c_LBL_YES);
		v_ouiButton.setData(Id.RDYES_CRUD);
		v_ouiButton.setEnabled(false);

		Button v_nonButton = new Button(v_groupCRUD, SWT.RADIO);
		v_nonButton.setText(c_LBL_NO);
		v_nonButton.setSelection(true);
		v_nonButton.setData(Id.RDNO_CRUD);
		v_nonButton.setEnabled(false);

		v_ouiButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection()) {
					_crud = c_BOOL_STR_YES;
					manageSelectionForCheckBox(_lstCompositesToManage.get(Id.CH_MOD_LST_FILES), Id.CHBT_SOAP);
				}
			}
		});

		v_nonButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent p_e) {

				if (((Button) p_e.getSource()).getSelection())
					_crud = c_BOOL_STR_NO;
			}
		});
	}

	/**
	 * Computes the validity specificaly for additional user SQL fields.
	 */
	private boolean computeAdditionalFieldsValidity() {

		for (AddColumn v_addColumn : _sqlAddColumns) {
			if (v_addColumn._name.isEmpty())
				return false;
		}
		return true;
	}

	/**
	 * Computes the validity specificaly for additional libraries.
	 */
	private boolean computeUseLibrariesValidity() {

		for (AddLibrary v_library : _useLibraries) {
			if (v_library._name.isEmpty() || v_library._package.isEmpty() || v_library._version.isEmpty()) {
				return false;
			}
			if (!Boolean.valueOf(v_library._useDatabase) && !v_library._prefix.isEmpty()) {
				return false;
			}
			if (!Boolean.valueOf(v_library._useDatabase) && !v_library._schema.isEmpty()) {
				return false;
			}
			if (!v_library._prefix.isEmpty() && !FormUtil.checkValueForPrefix(v_library._prefix)) {
				return false;
			}
			if (!v_library._schema.isEmpty() && !FormUtil.checkValueForSchema(v_library._schema)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Computes fields Validity.
	 */
	private void computeValidity() {

		// ValidatorUtil.INSTANCE.setLibraryPackageOK(FormUtil.checkValueForPackageName(_libraryPackage.getText()));
		// ValidatorUtil.INSTANCE.setPackageOK(FormUtil.checkValueForPackageName(_packageName.getText()));

		ValidatorUtil.INSTANCE.setPackageOK(!_packageName.getText().isEmpty());
		ValidatorUtil.INSTANCE.setApplicationNewOk(FormUtil.checkForNewProject(_applicationName));
		ValidatorUtil.INSTANCE.setWsOk(FormUtil.checkForWs(Boolean.valueOf(_srvWS), Boolean.valueOf(_srvWMS)));
		ValidatorUtil.INSTANCE.setEmbeddedDatabaseOK(FormUtil.checkForEmbeddedDatabase(_bdNames, _eh2));
		ValidatorUtil.INSTANCE.setApplicationOK(null != _applicationName && !_applicationName.isEmpty());
		ValidatorUtil.INSTANCE.setAuthorOK(null != _authorName && !_authorName.isEmpty());
		ValidatorUtil.INSTANCE.setSqlTablePrefixOk(FormUtil.checkValueForPrefix(_sqlTablePrefix));
		ValidatorUtil.INSTANCE.setSqlTableSchema(FormUtil.checkValueForSchema(_sqlTableSchema));
		ValidatorUtil.INSTANCE.setRequirementPrefixOK(FormUtil.checkValueForPrefix(_requirementPrefix));
		ValidatorUtil.INSTANCE.setOracleOK(FormUtil.checkForOracleVersion(_bdNames));
		ValidatorUtil.INSTANCE.setAdditionalFieldsOK(computeAdditionalFieldsValidity());
		ValidatorUtil.INSTANCE.setUseLibrariesOK(computeUseLibrariesValidity());
		ValidatorUtil.INSTANCE.setDatabaseOK(FormUtil.checkForDatabase(_bdNames, _eh2));
		ValidatorUtil.INSTANCE.setSpi4JOK(!_spi4jVersion.isEmpty());

		setMessage(ValidatorUtil.INSTANCE.getMessage(), ValidatorUtil.INSTANCE.getMessageType());
		setPageComplete(ValidatorUtil.INSTANCE.isValid());
	}

	public String getApplicationName() {
		return _applicationName;
	}

	public String getPackageName() {
		return _packageName.getText();
	}

	public String getNamingCode() {
		return _namingCode;
	}

	public String getIhm() {
		return (_ihm == null) ? "" : _ihm;
	}

	public void setSpiVersion(final String p_spi4JVersion) {
		_spi4jVersion = p_spi4JVersion;
	}

	public String getSrvEjb() {
		return _srvEjb;
	}

	public String getSrvReq() {
		return _srvReq;
	}

	public String getUnitTests() {
		return _unitTests;
	}

	public String getTypeRC() {
		return _typeRC;
	}

	public String getSrvWS() {
		return _srvWS;
	}

	public String getSrvWMS() {
		return _srvWMS;
	}

	public String getGenerateMatching() {
		return _generateMatching;
	}

	public String getIsLibrary() {
		return _isLibrary;
	}

	public String getAppliCrud() {
		return _appliCrud;
	}

	public String getGenBDD() {
		return _genBDD;
	}

	public String getUseSecurity() {
		return _useSecurity;
	}

	public String getJavaVersion() {
		return _javaVersion;
	}

	public List<String> getModelCodes() {
		return _mdCodes;
	}

	public String getIsLibraryRs() {
		return _isLibraryRs;
	}

	public String getSqlTablePrefix() {
		return _sqlTablePrefix.trim().toUpperCase();
	}

	public String getSqlTableSpace() {
		return _sqlTableSpace.trim().toUpperCase();
	}

	public String getAuthorName() {
		return _authorName.trim();
	}

	public String getRequirementLevel() {
		return _requirementLevel;
	}

	public String getRequirementPrefix() {
		return _requirementPrefix.trim().toUpperCase();
	}

	public String getLog4j() {
		return _log4J;
	}

	public String getUseConfig() {
		return _useConfig;
	}

	public String getFetchingStrategy() {
		return _fetchingStrategy;
	}

	public String getHk2() {
		return _hk2;
	}

	public String getModeDebug() {
		return _modeDebug;
	}

	public String getCrud() {
		return _crud;
	}

	public String getRequirementVersion() {
		return _requirementVersion;
	}

	public String getSqlTableSchema() {
		return _sqlTableSchema.trim().toUpperCase();
	}

	public String getHttpEmbeddedServer() {
		return _httpEmbeddedServer.toLowerCase();
	}

	public String getH2EmbeddedDatabase() {
		return _eh2;
	}

	public String getDataBasesNames() {
		StringBuilder v_dbNames = new StringBuilder();
		for (int i = 0; i < _bdNames.size(); i++) {
			v_dbNames.append(_bdNames.get(i));
			if (i < _bdNames.size() - 1)
				v_dbNames.append(",");
		}
		return v_dbNames.toString();
	}

	public String getAddLibraries() {

//		StringBuilder v_addLibraries = new StringBuilder();
//		for (int i = 0; i < _useLibraries.size(); i++) {
//			v_addLibraries.append(GenerateStart.c_PROP_LIB_ADD_JAR_BASE.replace("{$name}",
//					((AddLibrary) _useLibraries.get(i))._name));
//			if (i < _useLibraries.size() - 1)
//				v_addLibraries.append(",");
//		}
//		return v_addLibraries.toString();
		return null;
	}

	public Map<String, String> getAddLibrariesDetail() {
//		Map<String, String> v_map = new HashMap<>();
//
//		if (!_useLibraries.isEmpty()) {
//			for (AddLibrary v_addLibrary : _useLibraries) {
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_NAME.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._name);
//
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_PACKAGE.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._package);
//
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_VERSION.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._version);
//
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_DATABASE.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._useDatabase);
//
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_PREFIX.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._prefix);
//
//				v_map.put(GenerateStart.c_PROP_LIB_ADD_JAR_SCHEMA.replace("{$name}", v_addLibrary._name),
//						v_addLibrary._schema);
//			}
//		}
//		return v_map;
		return null;
	}

	public String getSqlAddColumns() {

//		// Concat the reserved and user free additional columns.
//		_sqlAddColumns.addAll(_sqlReservedAddColumns);
//
//		StringBuilder v_sqlAddColumns = new StringBuilder();
//		for (int i = 0; i < _sqlAddColumns.size(); i++) {
//			v_sqlAddColumns.append(GenerateStart.c_PROP_SQL_ADD_COLUMNS_BASE.replace("{$name}",
//					((AddColumn) _sqlAddColumns.get(i))._name));
//			if (i < _sqlAddColumns.size() - 1)
//				v_sqlAddColumns.append(",");
//		}
//		return v_sqlAddColumns.toString();
		return null;
	}

	public Map<String, String> getSqlAddColumnsDetail() {
//		Map<String, String> v_map = new HashMap<>();
//
//		if (!_sqlAddColumns.isEmpty()) {
//
//			for (AddColumn v_addColumn : _sqlAddColumns) {
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_SIZE.replace("{$name}", v_addColumn._name),
//						v_addColumn._size);
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_COMMENT.replace("{$name}", v_addColumn._name),
//						v_addColumn._comment);
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_NAME.replace("{$name}", v_addColumn._name),
//						v_addColumn._name.toUpperCase());
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_DEFAULT.replace("{$name}", v_addColumn._name),
//						(c_SQL_TYPE_COLUMN_LIST[7].equalsIgnoreCase(v_addColumn._type))
//								? "'" + v_addColumn._default + "'"
//								: v_addColumn._default);
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_TYPE.replace("{$name}", v_addColumn._name),
//						v_addColumn._type);
//
//				v_map.put(GenerateStart.c_PROP_SQL_ADD_COLUMNS_NOT_NULL.replace("{$name}", v_addColumn._name),
//						v_addColumn._notNull);
//			}
//		}
//		return v_map;
		return null;
	}

	/**
	 * Private enumeration for composites ID (used for enable / disable).
	 * 
	 * @author MINARM.
	 */
	private enum Id {

		RD_DBDD, RD_TU, RD_REQUIREMENT, RD_EJB, RD_MATCH, RD_SECURITY, RD_SOA_WS, RD_LIBRARY, RD_LIBRARYREST,
		CB_TYPE_IHM, CB_USE_BDD, RD_DEBUG, RD_FETCHSTRATEGY, RD_LOG4J, RD_CRUD, RD_HK2, RD_REQVERSION, CH_TYPE_BDD,
		CH_MOD_LST_FILES, RD_USE_LIBRARY, TX_USE_LIBRARY_GROUP, TX_USE_LIBRARY_ARTIFACT, TX_USE_LIBRARY_VERSION,
		CHBT_ENTITY, CHBT_SOAP, CHBT_DATABASE, TX_REQ_PREFIX, TX_REQ_LEVEL, TX_AUTHOR, CHBT_REQUIREMENT, CHBT_CINEMATIC,
		RDNO_LIB, RDYES_LIB, RDNO_TU, RDYES_TU, RDNO_MATCH, RDYES_MATCH, RDNO_REQ, RDYES_REQ, RDNO_WS, RDYES_WS,
		RDNO_SECU, RDYES_SECU, RDNO_BDD, RDYES_BDD, RDNO_EJB, RDYES_EJB, CHBT_H2, CHBT_POSTGRES, CHBT_ORACLE,
		CHBT_ORACLE_32, CHBT_MYSQL, CHBT_MARIADB, CHBT_SQLSERVER, ODB_SQL_COLUMNS, ODB_SQL_PREFIX, ODB_SQL_TABLESPACE,
		ODB_SQL_SCHEMA, ODB_SQL_NBCOLUMNS, LIB_USE_NBCOLUMNS, LIB_USE_LIBRARY, CH_CHXTOMAJ, RDNO_USE_LIBRARY,
		RDNO_DEBUG, RDYES_DEBUG, RDYES_FETCH, RDNO_FETCH, RDYES_LOG4J, RDNO_LOG4J, RDYES_CRUD, RDNO_CRUD, RDYES_HK2,
		RDNO_HK2, RDNO_REQVERSION, RDYES_REQVERSION, CB_HTTP_SERVER, RD_SOA_WMS, RDYES_WMS, RDNO_WMS, RD_USECONFIG,
		RDYES_USECONFIG, RDNO_USECONFIG, RD_EMBEDH2, RDYES_EMBEDH2, RDNO_EMBEDH2;

		static boolean isIdType_RD_NO(Id p_id) {

			if (null != p_id) {
				if (p_id.name().indexOf("RDNO_") != -1)
					return true;
			}
			return false;
		}

		static boolean isIdType_RD(Id p_id) {

			if (null != p_id) {
				if (p_id.name().indexOf("RD_") != -1)
					return true;
			}
			return false;
		}

		static boolean isIdType_CH(Id p_id) {

			if (null != p_id) {
				if (p_id.name().indexOf("CH_") != -1)
					return true;
			}
			return false;
		}

		static boolean isIdType_TX(Id p_id) {

			if (null != p_id) {
				if (p_id.name().indexOf("TX_") != -1)
					return true;
			}
			return false;
		}
	}

	/**
	 * Internal class for additional SQL field detail. Very simple, no getter or
	 * setter.
	 * 
	 * @author MINARM
	 */
	class AddColumn {

		String _name = "";
		String _size = "";
		String _comment = "";
		String _default = "";
		String _notNull = c_BOOL_STR_NO;
		String _type = c_SQL_TYPE_COLUMN_LIST[7];
	}

	/**
	 * Internal class for additional Library field detail. Very simple, no getter or
	 * setter.
	 * 
	 * @author MINARM
	 */
	class AddLibrary {

		String _name = "";
		String _package = "";
		String _version = "";
		String _prefix = "";
		String _schema = "";
		String _useDatabase = c_BOOL_STR_NO;
	}

	@Override
	public void dispose() {
		// deallocate SWT resources
		super.dispose();
	}
}
