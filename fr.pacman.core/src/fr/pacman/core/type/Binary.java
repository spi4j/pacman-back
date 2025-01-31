/**
 * (C) Copyright Ministere des Armees (France)
 *
 * Apache License 2.0
 */
package fr.pacman.core.type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.IOUtils;

/**
 * Permet de gérer le Binary Large Object.
 * 
 * @author MINARM
 */
public class Binary implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Le flux à lire. */
	private InputStream _InputStream;

	/**
	 * Constructeur.
	 */
	public Binary() {
		// RAS
	}

	/**
	 * Constructeur à partir d'un stream
	 * 
	 * @param p_InputStream Le Stream en entrée
	 */
	public Binary(final InputStream p_InputStream) {
		super();
		_InputStream = p_InputStream;
	}

	/**
	 * Retourne le binaryStream de la variable d'instance _BlobSql ou de la variable
	 * d'instance _InputStream si _BlobSql est null.
	 * 
	 * @return InputStream
	 */
	public InputStream getInputStream() {
		InputStream v_Return;

		v_Return = _InputStream;
		return v_Return;
	}

	/**
	 * Affecte la variable d'instance _InputStream
	 * 
	 * @param p_InputStream (In) Le InputStream à affecter
	 */
	public void setInputStream(final InputStream p_InputStream) {
		_InputStream = p_InputStream;
	}

	/**
	 * Ferme les streams.
	 * 
	 * @throws IOException e
	 */
	public void close() throws IOException {
		if (_InputStream != null) {
			_InputStream.close();
		}
	}

	/**
	 * Affecte la variable d'instance _BlobSql
	 * 
	 * @param p_BlobSql (In) Le blob à affecter
	 */
	public void setBlobSql(final Blob p_BlobSql) {
		try {
			_InputStream = p_BlobSql.getBinaryStream();
		} catch (final SQLException v_e) {
			// TODO Auto-generated catch block
		}
	}

	/**
	 * Getter du _BlobSql
	 * 
	 * @return java.sql.Blob
	 */
	public Blob getBlobSql() {
		Blob v_blob = null;
		try {
			v_blob = new SerialBlob(IOUtils.toByteArray(_InputStream));
		} catch (final SerialException v_seriale_e) {
			// TODO Auto-generated catch block
		} catch (final SQLException v_sql_e) {
			// TODO Auto-generated catch block
		} catch (final IOException v_io_e) {
			// TODO Auto-generated catch block
		}

		return v_blob;
	}

	/**
	 * Fonction de hashage de Binary
	 * 
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int v_prime = 31;
		int v_toHashCode = 0;
		final byte[] v_byteArray = toByteArray();
		if ((v_byteArray != null)) {
			v_toHashCode = Arrays.hashCode(v_byteArray);
		}
		int v_result = 1;
		v_result = v_prime * v_result + v_toHashCode;
		return v_result;
	}

	/**
	 * Fonction de test equals de Binary
	 * 
	 * @param p_obj (In) L'objet à comparer au Binary
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object p_obj) {

		if (this == p_obj) {
			return true;
		}
		if (p_obj == null) {
			return false;
		}
		if (getClass() != p_obj.getClass()) {
			return false;
		}
		final Binary v_other = (Binary) p_obj;
		if (_InputStream == null) {
			if (v_other._InputStream != null) {
				return false;
			}
		} else if (!Arrays.equals(toByteArray(_InputStream), toByteArray(v_other._InputStream))) {
			return false;
		}
		return true;
	}

	/**
	 * Convertir en tableau de 'byte' l'instance 'InputStream'.
	 * 
	 * @return Le tableau désiré.
	 */
	public byte[] toByteArray() {
		return toByteArray(_InputStream);
	}

	/**
	 * Retourne en tableau de 'byte' le parametre 'p_InputStream'.
	 * 
	 * @param p_InputStream Le Stream en entree
	 * @return Le tableau désiré.
	 */
	public static byte[] toByteArray(final InputStream p_InputStream) {
		ByteArrayOutputStream v_Return;

		if (p_InputStream == null) {
			v_Return = null;
		} else {
			v_Return = new ByteArrayOutputStream();
			try {
				// Se repositionner au début du flux
				p_InputStream.reset();
				final byte[] v_buf = new byte[p_InputStream.available()];
				int v_len = 0;

				while ((v_len = p_InputStream.read(v_buf)) > 0) {
					v_Return.write(v_buf, 0, v_len);
				}
				// Se repositionner au début du flux
				p_InputStream.reset();
			} catch (final IOException v_Err) {
				try {
					// Ré-essayer sans le "p_InputStream.reset()"
					final byte[] v_buf = new byte[p_InputStream.available()];
					int v_len = 0;

					while ((v_len = p_InputStream.read(v_buf)) > 0) {
						v_Return.write(v_buf, 0, v_len);
					}
				} catch (final IOException v_Err2) {
					throw new RuntimeException("Problème pour convertir InputStream --> byte[] : " + v_Err.getMessage(),
							v_Err2);
				}
			}
		}

		if (v_Return != null) {
			return v_Return.toByteArray();
		} else {
			return null;
		}

	}

	/**
	 * Retourne l'affichage en String du Binary
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		String v_Return = "";
		final String v_Sep = "";
		if (_InputStream != null) {
			v_Return = v_Return + v_Sep + "_InputStream non null : " + toByteArray();
		}
		return v_Return;
	}
}
