/* 
 * 
 * InternallyDetachedFolder (Plugin): 
 * 
 * Plugin para extraer los documentos almacenados en los archivos de firma XML (Internally Detached) de una carpeta
 * 
 * El código fuente de AutoFirma se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/ctt-gob-es/clienteafirma
 * 
 * InternallyDetachedFolder (Plugin) puede redistribuirse y/o modificarse bajo los términos:
 * 
 *   - GNU General Public License versión 2 (GPLv2) o superior 
 *   
 * y se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/digitaliza-aapp/afirma-plugins
 *   
 */

package es.gob.afirma.plugin.internallydetachedfolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Clase para la carga de propiedades
 * 
 * @author Felipe Muñoz Brieva
 */
public class Propiedades {

	// Ficheros de firma con documento original embebido:
	//
	// - HELP: Diputación Provincial de Málaga -> document,content
	//
	public static final ArrayList<String> ETIQUETAS_ORIGINAL_BASE = new ArrayList<String>(
			Arrays.asList("document,content", "enidoc:documento,enifile:contenido,enifile:ValorBinario"));

	// Archivo con propiedades
	private static final String BUNDLE_NAME = "es.gob.afirma.plugin.internallydetachedfolder.propiedades";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	// Propiedades
	public static final String PROP_PREFIJO_ORIGINAL = "prefijoOriginal";
	public static final String PROP_PREFIJO_MANUAL = "prefijoManual";
	public static final String PROP_EXTENSION_PDF = "extensionPdf";
	public static final String PROP_EXTENSION_TXT = "extensionTxt";
	public static final String PROP_MIMETYPE_PDF = "mimeTypePdf";
	public static final String PROP_MIMETYPE_TXT = "mimeTypeTxt";
	public static final String PROP_MIMETYPE_XML = "mimeTypeXml";
	public static final String PROP_ATTRIBUTE_MIMETYPE = "attributeMimeType";
	public static final String PROP_RUTA_RECURSOS = "rutaRecursos";
	public static final String PROP_FICHERO_AYUDA = "ficheroAyuda";
	public static final String PROP_SEPARADOR_LISTA_ETIQUETAS = "separadorListaEtiquetas";
	public static final String PROP_SEPARADOR_ETIQUETA = "separadorEtiqueta";
	public static final String PROP_SEPARADOR_EXTENSION = "separadorExtension";
	public static final String PROP_CONFIG_ETIQUETAS = "configEtiquetas";

	// Etiquetas a documentos originales
	public static ArrayList<String> etiquetasOriginal = new ArrayList<String>();

	private Propiedades() {
	}

	/**
	 * Obtiene un mensaje del fichero de recursos.
	 * 
	 * @param key Parametro
	 * @return Valor
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
