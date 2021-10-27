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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Clase para la carga de mensajes.
 *
 * @author Felipe Muñoz Brieva
 * 
 */
public class Messages {
	private static final String BUNDLE_NAME = "es.gob.afirma.plugin.marcarprimerafirma.messages"; 

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
