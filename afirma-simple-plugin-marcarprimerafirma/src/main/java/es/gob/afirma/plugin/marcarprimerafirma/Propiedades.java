/* 
 * 
 * MarcarPrimeraFirma (Plugin): 
 * 
 * El código fuente de AutoFirma se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/ctt-gob-es/clienteafirma
 * 
 * MarcarPrimeraFirma (Plugin) puede redistribuirse y/o modificarse bajo los términos:
 * 
 *   - GNU General Public License versión 2 (GPLv2) o superior 
 *   
 * y se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/digitaliza-aapp/afirma-plugins
 *   
 */

package es.gob.afirma.plugin.marcarprimerafirma;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Clase para la carga de propiedades
 * 
 * @author Felipe Muñoz Brieva
 */
public class Propiedades {

	public static final String A4_VERTICAL = "A4 Vertical";
	public static final String A4_HORIZONTAL = "A4 Horizontal";
	public static final String A3_VERTICAL = "A3 Vertical";
	public static final String A3_HORIZONTAL = "A3 Horizontal";
	public static final String VALOR_DEFECTO = "Valor por defecto";
	
	public static final String[][] HOJAS_FORMATO = { { A4_VERTICAL, "595", "842" }, { A4_HORIZONTAL, "842", "595" },
			{ A3_VERTICAL, "842", "1191" }, { A3_HORIZONTAL, "1191", "842" }, { VALOR_DEFECTO, "???", "???" } };

	// Archivo con propiedades
	private static final String BUNDLE_NAME = "es.gob.afirma.plugin.marcarprimerafirma.propiedades";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	// Propiedades
	public static final String PROP_PLUGIN_NAME = "pluginName";
	public static final String PROP_EXTENSION_PDF = "extensionPdf";
	public static final String PROP_PREFIJO_MANUAL = "prefijoManual";
	public static final String PROP_RUTA_RECURSOS = "rutaRecursos";
	public static final String PROP_FICHERO_AYUDA = "ficheroAyuda";
	public static final String PROP_SEPARADOR_EXTENSION = "separadorExtension";
	public static final String PROP_CAMPO_FECHA = "campoFecha";
	public static final String PROP_CAMPO_POSICION_H = "campoPosicionH";
	public static final String PROP_CAMPO_POSICION_W = "campoPosicionW";
	public static final String PROP_CAMPO_COLOR = "campoColor";
	public static final String PROP_CAMPO_ESTADO = "campoEstado";
	public static final String PROP_LONGITUD_MAXIMA_LINEA_FIRMA = "longitudMaximaLineaFirma";

	public static final String PROP_SETUP_LINEA1 = "setupLinea1";
	public static final String PROP_SETUP_LINEA2 = "setupLinea2";
	public static final String PROP_SETUP_COLOR_RED = "setupColorRed";
	public static final String PROP_SETUP_COLOR_GREEN = "setupColorGreen";
	public static final String PROP_SETUP_COLOR_BLUE = "setupColorBlue";
	public static final String PROP_SETUP_RECTANGULO_GROSOR_LINEA = "setupRectanguloGrosorLinea";
	public static final String PROP_SETUP_RECTANGULO_TAMANO_FUENTE = "setupRectanguloTamanoFuente";
	public static final String PROP_SETUP_RECTANGULO_MOSTRAR_LINEA = "setUpRectanguloMostrarLinea";
	public static final String PROP_SETUP_SOLO_PRIMERA_PAGINA = "setupSoloPrimeraPagina";
	public static final String PROP_SETUP_A4_HORIZONTAL_H = "setupA4HorizontalH";
	public static final String PROP_SETUP_A4_HORIZONTAL_W = "setupA4HorizontalW";
	public static final String PROP_SETUP_A4_VERTICAL_H = "setupA4VerticalH";
	public static final String PROP_SETUP_A4_VERTICAL_W = "setupA4VerticalW";
	public static final String PROP_SETUP_A3_HORIZONTAL_H = "setupA3HorizontalH";
	public static final String PROP_SETUP_A3_HORIZONTAL_W = "setupA3HorizontalW";
	public static final String PROP_SETUP_A3_VERTICAL_H = "setupA3VerticalH";
	public static final String PROP_SETUP_A3_VERTICAL_W = "setupA3VerticalW";
	public static final String PROP_SETUP_DEFECTO_H = "setupDefectoH";
	public static final String PROP_SETUP_DEFECTO_W = "setupDefectoW";
	public static final String PROP_SETUP_ACTIVAR_PLUGIN = "setupActivarPlugin";

	public static final String PROP_TEXTO_ACTIVADO = "textoActivado";
	public static final String PROP_TEXTO_DESACTIVADO = "textoDesactivado";
	public static final String PROP_COLOR_ACTIVADO = "colorActivado";
	public static final String PROP_COLOR_DESACTIVADO = "colorDesactivado";

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
