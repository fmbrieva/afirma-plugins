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

package es.gob.afirma.plugin.tablahuellas;

import es.gob.afirma.standalone.plugins.AfirmaPlugin;
import es.gob.afirma.standalone.plugins.PluginControlledException;

/**
 * Instalación del plugin con todas las etiquetas definidas en Propiedades
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class TablaHuellasPlugin extends AfirmaPlugin {

	// Ejecutar durante la instalación del plugin
	public void install() throws PluginControlledException {

		for (int i = 0; i < Propiedades.ETIQUETAS_ORIGINAL_BASE.size(); i++) {
			Propiedades.etiquetasOriginal.add(Propiedades.ETIQUETAS_ORIGINAL_BASE.get(i));		
		}
		
	}

}
