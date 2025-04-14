package fr.pacman.core.service;

import java.util.HashMap;
import java.util.Map;

import org.obeonetwork.dsl.environment.Attribute;
import org.obeonetwork.dsl.environment.Enumeration;
import org.obeonetwork.dsl.environment.Literal;

public class TestUtils {

	/**
	 * Liste des types de colonne gérés par Pacman.
	 */
//	private static final String c_typeColumnId = "Id";
//	private static final String c_typeColumnIdJoin = "IdJoin";
//	private static final String c_typeColumnReference = "Reference";
	private static final String c_typeColumnXtopSup = "XtopSup";
//	private static final String c_typeColumnDefault = "Default";
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
	private static final String c_typeColumnUuid = "UUID";

	/**
	 * La liste des valeurs par défaut pour l'initialisation des entités en fonction
	 * du type de l'attribut.
	 */
	private static Map<String, String> _defaultValues;

	static {
		_defaultValues = new HashMap<>();
		_defaultValues.put(c_typeColumnString, "\"S\"");
		_defaultValues.put(c_typeColumnDate, "LocalDate.now()");
		_defaultValues.put(c_typeColumnBoolean, "false");
		_defaultValues.put(c_typeColumnBinary, "rawtohex('Test')");
		_defaultValues.put(c_typeColumnDouble, "0D");
		_defaultValues.put(c_typeColumnFloat, "0F");
		_defaultValues.put(c_typeColumnInt, "0");
		_defaultValues.put(c_typeColumnLong, "0L");
		_defaultValues.put(c_typeColumnChar, "C");
		_defaultValues.put(c_typeColumnTime, "LocalTime.now()");
		_defaultValues.put(c_typeColumnTimestamp, "Instant.now()");
		_defaultValues.put(c_typeColumnUuid, "UUID.randomUUID()");
		_defaultValues.put(c_typeColumnXtopSup, "false");
	}

	public static String get_defaulValueForType(final Attribute p_attribute) {

		if (p_attribute.getType() instanceof Enumeration) {
			Literal literal = (Literal) ((Enumeration) p_attribute.getType()).getLiterals().get(0);
			return "'" + literal.getName() + "'";
		} else {
			if (_defaultValues.containsKey(p_attribute.getType().getName()))
				return _defaultValues.get(p_attribute.getType().getName());
		}
		return "Unknown default value !!!";
	}

	public static String get_defaulValueForType(final String p_prop) {
		if (_defaultValues.containsKey(p_prop))
			return _defaultValues.get(p_prop);
		return "Unknown default value !!!";
	}
}
