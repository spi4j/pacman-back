package fr.pacman.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.obeonetwork.dsl.entity.Entity;
import org.obeonetwork.dsl.environment.Attribute;
import org.obeonetwork.dsl.environment.Enumeration;
import org.obeonetwork.dsl.environment.Literal;
import org.obeonetwork.dsl.environment.Reference;

import fr.pacman.core.enumeration.AnnotationEnum;

/**
 * Classe utilitaire pour la génération des scripts SQL. On concentre ici toute
 * les méthodes nécessaires pour faciliter la génération de l'ensemble des
 * scripts pour les différents types de base de données gérés par Pacman.
 * 
 * @author MINARM
 */
public class DatabaseUtils {

	/**
	 * Liste des types de base gérés par Pacman.
	 */
	private static final String c_typeOracle = "ORACLE";
	private static final String c_typeOracle32 = "ORACLE_32";
	private static final String c_typeMySql = "MYSQL";
	private static final String c_typeMariaDb = "MARIADB";
	private static final String c_typePostgres = "POSTGRESQL";
	private static final String c_typeH2 = "H2";

	/**
	 * Liste des types de colonne gérés par Pacman.
	 */
	private static final String c_typeColumnId = "Id";
	private static final String c_typeColumnIdJoin = "IdJoin";
	private static final String c_typeColumnReference = "Reference";
	private static final String c_typeColumnXtopSup = "XtopSup";
	private static final String c_typeColumnUuid = "UUID";
	private static final String c_typeColumnDefault = "Default";
	private static final String c_typeColumnLong = "Long";
	private static final String c_typeColumnInt = "Integer";
	private static final String c_typeColumnFloat = "Float";
	private static final String c_typeColumnDouble = "Double";
	private static final String c_typeColumnString = "String";
	private static final String c_typeColumnDate = "Date";
	private static final String c_typeColumnTimestamp = "Timestamp";
	private static final String c_typeColumnTime = "Time";
	private static final String c_typeColumnBinary = "Binary";
	private static final String c_typeColumnBoolean = "Boolean";
	private static final String c_typeColumnChar = "Char";

	/**
	 * La liste des types de base de données sélectionnés pour le projet. La liste
	 * est initialisée avec l'ensemble des types de base de données gérés par Pacman
	 * et, si la base est utilisée dans le cadre du projet, la valeur 'true' est
	 * alors positionnée au niveau de la clé représentant le type de base.
	 */
	private static Map<String, Boolean> _types;

	/**
	 * La liste des types pour les colonnes en fonction des différentes bases de
	 * données.
	 */
	private static Map<String, Map<String, String>> _columnTypes;

	/**
	 * La liste des tailles par défaut pour les différentes colonnes si la taille
	 * n'a pas été initialement précisée par le développeur du générateur.
	 */
	private static Map<String, String> _defaultColumnLengths;

	/**
	 * La liste des valeurs par défaut pour l'initialisation des tables en fonction
	 * du type de la colonne.
	 */
	private static Map<String, String> _defaultValues;

	/**
	 * Initialisation des variables pour les compteurs de nommage.
	 */
	private static int _oldCpt = 0;
	private static int _currentCpt = 1;
	private static Entity _oldEntity;
	private static _ConstraintEnum _oldConstraint;

	/**
	 * Liste des types de contraintes possibles pour les compteurs de nommage.
	 */
	private enum _ConstraintEnum {
		FK, UN, IDX, CK
	}

	/**
	 * Constructeur privé pour la classe.
	 */
	private DatabaseUtils() {
		// RAS.
	}

	/**
	 * Initialisation des données de travail (pas forcement en bloc static... a
	 * voir).
	 */
	static {
		_types = new HashMap<>();
		_types.put(c_typeOracle, false);
		_types.put(c_typeOracle32, false);
		_types.put(c_typeMariaDb, false); // Mysql Fork.
		_types.put(c_typeMySql, false);
		_types.put(c_typePostgres, false);
		_types.put(c_typeH2, false);

		_defaultColumnLengths = new HashMap<>();
		_defaultColumnLengths.put(c_typeColumnLong, "10");
		_defaultColumnLengths.put(c_typeColumnInt, "10");
		_defaultColumnLengths.put(c_typeColumnFloat, "14,2");
		_defaultColumnLengths.put(c_typeColumnDouble, "14,2");
		_defaultColumnLengths.put(c_typeColumnString, "100");
		_defaultColumnLengths.put(c_typeColumnUuid, "36");

		_defaultValues = new HashMap<>();
		_defaultValues.put(c_typeColumnString, "'S'");
		_defaultValues.put(c_typeColumnDate, "current_date");
		_defaultValues.put(c_typeColumnBoolean, "false");
		_defaultValues.put(c_typeColumnBinary, "rawtohex('Test')");
		_defaultValues.put(c_typeColumnDouble, "0");
		_defaultValues.put(c_typeColumnFloat, "0");
		_defaultValues.put(c_typeColumnInt, "0");
		_defaultValues.put(c_typeColumnLong, "0");
		_defaultValues.put(c_typeColumnChar, "C");
		_defaultValues.put(c_typeColumnTime, "current_timestamp");
		_defaultValues.put(c_typeColumnTimestamp, "current_timestamp");
		_defaultValues.put(c_typeColumnUuid, "'a81bc81b-dead-4e5d-abff-90865d1e13b1'");
		_defaultValues.put(c_typeColumnXtopSup, "'0'");

		_columnTypes = new HashMap<>();
		_columnTypes.put(c_typeOracle, new HashMap<>());
		_columnTypes.put(c_typeOracle32, new HashMap<>());
		_columnTypes.put(c_typeMariaDb, new HashMap<>());
		_columnTypes.put(c_typeMySql, new HashMap<>());
		_columnTypes.put(c_typePostgres, new HashMap<>());
		_columnTypes.put(c_typeH2, new HashMap<>());

		_columnTypes.get(c_typeOracle).put(c_typeColumnId, "NUMBER(19) not null");
		_columnTypes.get(c_typeOracle).put(c_typeColumnIdJoin, "NUMBER(19) not null");
		_columnTypes.get(c_typeOracle).put(c_typeColumnReference, "NUMBER(19)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnXtopSup, "VARCHAR(1)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnUuid, "VARCHAR(36)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnDefault, "VARCHAR(200)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnLong, "NUMBER(?)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnInt, "NUMBER(?)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnFloat, "NUMBER(?)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnDouble, "NUMBER(?)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnString, "VARCHAR(?)");
		_columnTypes.get(c_typeOracle).put(c_typeColumnDate, "DATE");
		_columnTypes.get(c_typeOracle).put(c_typeColumnTimestamp, "TIMESTAMP");
		_columnTypes.get(c_typeOracle).put(c_typeColumnTime, "TIMESTAMP");
		_columnTypes.get(c_typeOracle).put(c_typeColumnBinary, "BLOB");
		_columnTypes.get(c_typeOracle).put(c_typeColumnBoolean, "NUMBER(1)");

		_columnTypes.get(c_typeMySql).put(c_typeColumnId, "BIGINT(19) unsigned not null auto_increment");
		_columnTypes.get(c_typeMySql).put(c_typeColumnIdJoin, "BIGINT(19) unsigned not null");
		_columnTypes.get(c_typeMySql).put(c_typeColumnReference, "BIGINT(19) unsigned");
		_columnTypes.get(c_typeMySql).put(c_typeColumnXtopSup, "VARCHAR(1)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnUuid, "VARCHAR(36)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnDefault, "VARCHAR(200)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnLong, "BIGINT(?)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnInt, "INT(?)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnFloat, "FLOAT(?)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnDouble, "DOUBLE(?)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnString, "VARCHAR(?)");
		_columnTypes.get(c_typeMySql).put(c_typeColumnDate, "DATETIME");
		_columnTypes.get(c_typeMySql).put(c_typeColumnTimestamp, "TIMESTAMP");
		_columnTypes.get(c_typeMySql).put(c_typeColumnTime, "TIMESTAMP");
		_columnTypes.get(c_typeMySql).put(c_typeColumnBinary, "LONGBLOB");
		_columnTypes.get(c_typeMySql).put(c_typeColumnBoolean, "BOOLEAN");
		_columnTypes.get(c_typePostgres).put(c_typeColumnId, "BIGINT not null");
		_columnTypes.get(c_typePostgres).put(c_typeColumnIdJoin, "BIGINT not null");
		_columnTypes.get(c_typePostgres).put(c_typeColumnReference, "BIGINT");
		_columnTypes.get(c_typePostgres).put(c_typeColumnXtopSup, "VARCHAR(1)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnUuid, "VARCHAR(36)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnDefault, "VARCHAR(200)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnLong, "BIGINT|NUMERIC(?)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnInt, "INTEGER|NUMERIC(?)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnFloat, "NUMERIC(?)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnDouble, "NUMERIC(?)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnString, "VARCHAR(?)");
		_columnTypes.get(c_typePostgres).put(c_typeColumnDate, "TIMESTAMP");
		_columnTypes.get(c_typePostgres).put(c_typeColumnTimestamp, "TIMESTAMP");
		_columnTypes.get(c_typePostgres).put(c_typeColumnTime, "TIME");
		_columnTypes.get(c_typePostgres).put(c_typeColumnBinary, "BYTEA");
		_columnTypes.get(c_typePostgres).put(c_typeColumnBoolean, "BOOLEAN");

		_columnTypes.get(c_typeH2).put(c_typeColumnId, "NUMBER(19) not null");
		_columnTypes.get(c_typeH2).put(c_typeColumnIdJoin, "NUMBER(19) not null");
		_columnTypes.get(c_typeH2).put(c_typeColumnReference, "NUMBER(19)");
		_columnTypes.get(c_typeH2).put(c_typeColumnXtopSup, "VARCHAR(1)");
		_columnTypes.get(c_typeH2).put(c_typeColumnUuid, "VARCHAR(36)");
		_columnTypes.get(c_typeH2).put(c_typeColumnDefault, "VARCHAR(200)");
		_columnTypes.get(c_typeH2).put(c_typeColumnLong, "NUMBER(?)");
		_columnTypes.get(c_typeH2).put(c_typeColumnInt, "NUMBER(?)");
		_columnTypes.get(c_typeH2).put(c_typeColumnFloat, "NUMBER(?)");
		_columnTypes.get(c_typeH2).put(c_typeColumnDouble, "NUMBER(?)");
		_columnTypes.get(c_typeH2).put(c_typeColumnString, "VARCHAR(?)");
		_columnTypes.get(c_typeH2).put(c_typeColumnDate, "TIMESTAMP");
		_columnTypes.get(c_typeH2).put(c_typeColumnTimestamp, "TIMESTAMP");
		_columnTypes.get(c_typeH2).put(c_typeColumnTime, "TIMESTAMP");
		_columnTypes.get(c_typeH2).put(c_typeColumnBinary, "BLOB");
		_columnTypes.get(c_typeH2).put(c_typeColumnBoolean, "BOOLEAN");

		_columnTypes.get(c_typeMariaDb).putAll(_columnTypes.get(c_typeMySql));
		_columnTypes.get(c_typeOracle32).putAll(_columnTypes.get(c_typeOracle32));
	}

	// Voir le nom et le mode d'initialisation....
	public static String init(final String p_selectedTypes) {
		for (String type : p_selectedTypes.split(",")) {
			if (!_types.containsKey(type.trim()))
				throw new RuntimeException("pouet !");
			_types.put(type.trim(), true);
		}
		return "";
	}

	/**
	 * Demande la remise à blanc de l'ensemble des compteurs pour le nommage des
	 * indexs et des différentes contraintes sur les tables au niveau des scripts
	 * SQL.
	 * 
	 * @param p_full demande de remise à blanc totale.
	 */
	private static void resetCounters(final Boolean p_full) {
		if (p_full) {
			_oldConstraint = null;
			_oldEntity = null;
		}
		_currentCpt = 1;
		_oldCpt = 0;
	}

	/**
	 * Vérifie si le projet utilise une ou plusieurs bases de données. Le projet
	 * utilise des bases de données à partir du moment ou au moins un indicateur
	 * d'utilisation est à 'true' dans la liste des types de bases de données.
	 * 
	 * @param p_object tracabilité aql.
	 * @return la valeur 'true' si le projet utilise des bases de données, sinon,
	 *         retourne la valeur 'false'.
	 */
	public static boolean get_useDatabases(final Object p_object) {
		for (Entry<String, Boolean> type : _types.entrySet()) {
			if (type.getValue())
				return true;
		}
		return false;
	}

	/**
	 * Retourne le type pour la colonne (colonne technique) en fonction du type de
	 * la base de données et du type initialement demandé en fonction de la colonne.
	 * (autre que dans le cas spécifique d'un attribut).
	 * <p>
	 * Plutôt que de tester la validité des paramètres ainsi que les potentielles
	 * nullités, on part sur une approche optimiste (gain de performance) et une
	 * exception est levée sur un potentiel nullPointer.
	 * 
	 * @param p_dbType     le type de la base de données.
	 * @param p_columnType le type de la colonne.
	 * @param p_size       la taille demandée (optionnel).
	 * @return le type en fonction de la colonne et de la base de données.
	 */
	public static String get_columnTypeForDatabase(final String p_dbType, final String p_columnType,
			final String p_size) {
		try {
			String columnType = _columnTypes.get(p_dbType.trim().toUpperCase()).get(p_columnType.trim());
			if (columnType.indexOf("?") != -1) {
				if (null != p_size && !p_size.equals("invalide") && !p_size.trim().isEmpty()
						&& Integer.valueOf(p_size) > 0) {
					columnType = columnType.replace("?", p_size.trim());
				} else {
					columnType = columnType.replace("?", _defaultColumnLengths.get(p_columnType.trim()));
				}
			}
			return columnType;
		} catch (Exception p_e) {
			return "INVALID!!";
		}
	}

	/**
	 * Retourne le type pour la colonne (colonne technique) en fonction du type de
	 * la base de données et du type initialement demandé (autre que dans le cas
	 * spécifique d'un attribut).
	 * 
	 * @param p_dbType     le type de la base de données.
	 * @param p_columnType le type de la colonne.
	 * @return le type en fonction de la colonne et de la base de données.
	 */
	public static String get_columnTypeForDatabase(final String p_dbType, final String p_columnType) {
		return get_columnTypeForDatabase(p_dbType, p_columnType, "");
	}

	/**
	 * Retourne le type de colonne spécifiquement pour un attribut en fonction du
	 * type de la base de donnnées et du type initialement demandé.
	 * 
	 * @param p_attribute l'attribut pour lequel on demande le type de colonne.
	 * @param p_dbType    le type de la base de données.
	 * @return le type en fonction de la colonne et de la base de données.
	 */
	public static String get_columnTypeForDatabase(final Attribute p_attribute, final String p_dbType) {
		if (null != p_attribute && null != p_attribute.getType()) {
			String attributeSize;
			if (p_attribute.getType() instanceof Enumeration
					&& !((Enumeration) p_attribute.getType()).getLiterals().isEmpty()) {
				attributeSize = EnumerationUtils.get_maxSqlSizeForEnumeration((Enumeration) p_attribute.getType());
				return get_columnTypeForDatabase(p_dbType, "String", attributeSize);
			} else {
				if (AnnotationUtils.is_annotationExists(p_attribute, AnnotationEnum.PHYSICAL_SIZE)) {
					attributeSize = AnnotationUtils.get_annotationBody(p_attribute.getMetadatas(),
							AnnotationEnum.PHYSICAL_SIZE);
				} else {
					attributeSize = "";
				}
				return get_columnTypeForDatabase(p_dbType, p_attribute.getType().getName(), attributeSize);
			}
		} else {
			return get_columnTypeForDatabase(p_dbType, "");
		}
	}

	/**
	 * Retourne une valeur par défaut en fonction du type de l'attribut.
	 * 
	 * @param p_attribute l'attribut pour lequel retourner une valeur par défaut.
	 * @return la valeur par défaut (pour les tests de non-régression par expample).
	 */
	public static String get_defaulValueForColumn(final Attribute p_attribute) {
		if (p_attribute.getType() instanceof Enumeration) {
			Literal literal = (Literal) ((Enumeration) p_attribute.getType()).getLiterals().get(0);
			return "'" + literal.getName() + "'";
		} else {
			if (_defaultValues.containsKey(p_attribute.getType().getName()))
				return _defaultValues.get(p_attribute.getType().getName());
		}
		return "Unknown default value !!!";
	}

	/**
	 * Retourne une valeur par défaut en fonction du type de l'attribut.
	 * 
	 * @param p_type le type pour lequel retourner une valeur par défaut.
	 * @return la valeur par défaut (pour les tests de non-régression par expample).
	 */
	public static String get_defaulValueForColumn(final String p_type) {
		if (_defaultValues.containsKey(p_type))
			return _defaultValues.get(p_type);
		return "Unknown default value !!!";
	}
//
//
//	if(not (a = null)
//
//	and not (a.type = null)) then a.typeDataBase2(dbType, size)  else dbType.typeDataBase('', size) endif/]
// if (a.isTypeOfEnumeration()) then dbType.typeDataBase('String', size) else dbType.typeDataBase(a.type.name, size) endif/]

	/**
	 * Vérifie si la base de données qui a été sélectionnée utilise ou non les
	 * séquences pour l'incrémentation de ses clés primaires.
	 * 
	 * @param p_dbType le type de la base de données.
	 * @return la valeur 'true' si le type de base de données permet l'utilisation
	 *         de séquences sinon 'false'.
	 */
	public static boolean use_sequences(final String p_dbType) {
		return !is_mySqlType(p_dbType);
	}

	/**
	 * Vérifie si la base de données qui a été sélectionnée est de type MySql.
	 * 
	 * @param p_dbType le type de la base de données.
	 * @return la valeur 'true' si le type de base de données est de type MySql ou
	 *         MariaDb (fork MySql) sinon 'false'.
	 */
	public static boolean is_mySqlType(final String p_dbType) {
		return (p_dbType.trim().toUpperCase().equals(c_typeMySql)
				|| p_dbType.trim().toUpperCase().equals(c_typeMariaDb)) ? true : false;
	}

	/**
	 * Vérifie si la base de données qui a été sélectionnée est de type H2.
	 * 
	 * @param p_dbType le type de la base de données.
	 * @return la valeur 'true' si le type de base de données est de type H2 sinon
	 *         'false'.
	 */
	public static boolean is_h2Type(final String p_dbType) {
		return (p_dbType.trim().toUpperCase().equals(c_typeH2)) ? true : false;
	}

	/**
	 * Demande la remise à blanc de l'ensemble des compteurs pour le nommage des
	 * indexs et des contraintes au niveau des différents scripts SQL.
	 * 
	 * @param p_object object uniquement pour la tracabilité aql.
	 * @return une chaine nulle pour renvoyer qqe chose au niveau aql.
	 */
	public static String do_resetCounters(final Object p_object) {
		resetCounters(true);
		return null;
	}

	/**
	 * 
	 * @param p_prefix
	 * @param p_suffix
	 * @return
	 */
	public static int do_calcMaxSize(final int p_prefix, final int p_suffix) {
		return (_types.get(c_typeOracle32)) ? 30 - (p_prefix + p_suffix) : 128;
	}

	/**
	 * 
	 * @param p_e
	 * @param p_nbToIncrement
	 * @return
	 */
	public static int get_counterFK(final Entity p_e, final int p_nbToIncrement) {
		return get_nextCounter(_ConstraintEnum.FK, p_e, p_nbToIncrement);
	}

	/**
	 * 
	 * @param p_e
	 * @param p_nbToIncrement
	 * @return
	 */
	public static int get_counterUN(final Entity p_e, final int p_nbToIncrement) {
		return get_nextCounter(_ConstraintEnum.IDX, p_e, p_nbToIncrement);
	}

	/**
	 * 
	 * @param p_e
	 * @param p_nbToIncrement
	 * @return
	 */
	public static int get_counterIDX(final Entity p_e, final int p_nbToIncrement) {
		return get_nextCounter(_ConstraintEnum.IDX, p_e, p_nbToIncrement);
	}

	/**
	 * 
	 * @param p_e
	 * @return
	 */
	public static int get_counterCK(final Entity p_e) {
		return get_nextCounter(_ConstraintEnum.CK, p_e, 1);
	}

	/**
	 * Calcul du compteur pour le nommage des contraintes et des indexs.
	 * 
	 * @param p_constraint
	 * @param p_e
	 * @param p_nbToIncrement
	 * @return
	 */
	private static int get_nextCounter(final _ConstraintEnum p_constraint, final Entity p_e,
			final int p_nbToIncrement) {
		if (_oldConstraint != p_constraint) {
			resetCounters(true);
			_oldConstraint = p_constraint;
		}
		if (_oldEntity != p_e) {
			_oldEntity = p_e;
			resetCounters(false);
		}
		_oldCpt = _currentCpt;
		_currentCpt += p_nbToIncrement;
		return _oldCpt;
	}

	/**
	 * Récupère la liste des colonnes pour la création d'une contrainte d'unicité
	 * sur une table et renvoi le nombre de colonnes pour pouvoir créer le bon
	 * compteur au niveau de la génération de la contrainte.
	 * 
	 * @param p_lstColumns la chaine contenant l'ensemble des colonnes sur
	 *                     lesquelles porte la contrainte d'unicité.
	 * @return le nombre de colonnes impactées par la contrainte d'unicité
	 */
	public static int get_nbColumnsInUniqueIndex(final String p_lstColumns) {
		return p_lstColumns.split(",").length;
	}

	/**
	 * Retourne la liste des colonnes demandées pour la création d'indexs uniques.
	 * Les indexs uniques sont séparés par le caractère "|", si l'unicité porte sur
	 * plusieurs colonnes alors la liste des colonnes est séparée par le caractère
	 * ",".
	 * 
	 * @param p_str la chaine initiale pour la liste des colonnes portant sur des
	 *              indexs uniques.
	 * @return la liste des colonnes regroupées en indexs uniques
	 */
	public static List<String> get_columnsForUniqueIndexs(final String p_str) {
		List<String> v_unIdxs = new ArrayList<>();
		if (p_str.indexOf("|") != -1) {
			StringTokenizer v_tokenizer = new StringTokenizer(p_str, "|");
			while (v_tokenizer.hasMoreTokens()) {
				v_unIdxs.add(v_tokenizer.nextToken());
			}
			return v_unIdxs;
		}
		v_unIdxs.add(p_str);
		return v_unIdxs;
	}

	/**
	 * Elimine les doublons fonctionnels dans le cadre des références multiples de
	 * type (0,*)(1,*). Dans la liste passée en paramètre, les références sont
	 * trouvées deux fois (A->B) et (B->A). Pour créer des tables de liaison, il
	 * suffit uniquement d'une seule des deux références. On supprime donc
	 * arbitrairement une des deux références.
	 * <p>
	 * Si l'opposite n'existe pas, il s'agit d'une récursive multiple et il ne peut
	 * y avoir de doublon (tout au moins au niveau technique).
	 * 
	 * @param p_references la liste des références
	 * @return la liste des références expurgée des doublons fonctionnels
	 */
	public static List<Reference> do_skipDuplicates(final List<Reference> p_references) {

		Map<Integer, Reference> filteredReferences = new HashMap<>();
		if (p_references == null || p_references.isEmpty())
			return Collections.emptyList();

		for (Reference reference : p_references) {
			String technicalId = (null != reference.getOppositeOf())
					? reference.getTechnicalid() + reference.getOppositeOf().getTechnicalid()
					: reference.getTechnicalid();

			int sum = 0;
			for (int i = 0; i < technicalId.length(); i++) {
				sum += technicalId.charAt(i);
			}
			if (!filteredReferences.containsKey(sum))
				filteredReferences.put(sum, reference);
		}
		return new ArrayList<Reference>(filteredReferences.values());
	}

	/**
	 * Retourne l'index pour le nommage de la référence. Si la référence ne doit pas
	 * avoir d'index (une seule référence pour l'entité) alors l'index est
	 * positionné à 0.
	 * <p>
	 * Cette nouvelle manière de procéder par rapport à la version AQL7 est liée au
	 * fait que le script a été simplifié à l'extrème et qu'il n'y a plus qu'une
	 * seule ligne au lieu de 18 avec suppression de boucles et de structures
	 * conditionnelles. Le fonctionnement de la requête initiale de récupération des
	 * références a été aussi profondément modifié.
	 * <p>
	 * ATTENTION, la liste des références doit toujours être triée de la même
	 * manière sinon il y aura un décalage entre les différents appels (voir plus
	 * tard si on effectue le tri au niveau de cette méthode afin d'être plus
	 * robuste).
	 * 
	 * @param p_reference  la référence en cours de traitement
	 * @param p_references la liste des références
	 * @return l'index pour le nommage de la référence
	 */
	public static Integer get_namingIndex(final Reference p_reference, final Set<Reference> p_references) {
		int idx = 0, idx2 = 0;
		for (Reference ref : p_references) {
			if (ref.getReferencedType() == p_reference.getReferencedType()) {
				idx++;
				if (ref == p_reference)
					break;
			}
		}
		for (Reference ref : p_references) {
			if (ref.getReferencedType() == p_reference.getReferencedType())
				idx2++;
		}
		if (idx2 == 1)
			idx--;
		return idx;
	}
}
