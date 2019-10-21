/* 
 * 
 * InternallyDetachedView (Plugin): 
 * 
 * Plugin para  visualizar el documento original almacenado en archivos de firma XML, se integra 
 * con AutoFirma.
 * 
 * El código fuente de AutoFirma se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/ctt-gob-es/clienteafirma
 * 
 * InternallyDetachedView (Plugin) puede redistribuirse y/o modificarse bajo los términos:
 * 
 *   - GNU General Public License versión 2 (GPLv2) o superior 
 *   
 * y se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/digitaliza-aapp/afirma-plugins
 *   
 */

package es.gob.afirma.plugin.internallydetachedview;

import es.gob.afirma.standalone.plugins.AfirmaPlugin;
import es.gob.afirma.standalone.plugins.PluginControlledException;

import es.gob.afirma.plugin.internallydetachedview.Propiedades;

/**
 * Instalación del plugin con todas las etiquetas definidas en Propiedades
 * 
 */
public class InternallyDetachedViewPlugin extends AfirmaPlugin {

	// Ejecutar durante la instalación del plugin
	public void install() throws PluginControlledException {

		for (int i = 0; i < Propiedades.ETIQUETAS_ORIGINAL_BASE.size(); i++) {
			Propiedades.etiquetasOriginal.add(Propiedades.ETIQUETAS_ORIGINAL_BASE.get(i));		
		}
		
	}

}
