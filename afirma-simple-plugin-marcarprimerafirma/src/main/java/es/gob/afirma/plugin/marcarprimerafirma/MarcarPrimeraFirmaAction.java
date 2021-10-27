/* 
 * 
 * InternallyDetachedFolder (Plugin): 
 * 
 * Plugin para añadir un cuadro de texto la primera vez que se firma un documento
 * 
 * El código fuente de AutoFirma se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/ctt-gob-es/clienteafirma
 * 
 * TablaHuellas (Plugin) puede redistribuirse y/o modificarse bajo los términos:
 * 
 *   - GNU General Public License versión 2 (GPLv2) o superior 
 *   
 * y se encuentra disponible desde el repositorio público de GitHub:
 * 
 *    https://github.com/digitaliza-aapp/afirma-plugins
 *   
 */

package es.gob.afirma.plugin.marcarprimerafirma;

import java.awt.Color;
import java.awt.Container;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import es.gob.afirma.standalone.SimpleAfirmaMessages;
import es.gob.afirma.standalone.plugins.AfirmaPlugin;
import es.gob.afirma.standalone.plugins.DataProcessAction;
import es.gob.afirma.standalone.plugins.InputData;
import es.gob.afirma.standalone.plugins.PluginException;
import es.gob.afirma.standalone.plugins.PluginsManager;
import es.gob.afirma.standalone.plugins.PluginsPreferences;
import es.gob.afirma.standalone.ui.plugins.PluginConfigurationDialog;

/**
 * Acción: Mostrar panel de configuracion del plugin
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class MarcarPrimeraFirmaAction extends DataProcessAction {

	static final Logger LOGGER = Logger.getLogger(MarcarPrimeraFirmaAction.class.getName());

	public MarcarPrimeraFirmaAction() {

	}

	@Override
	public void processData(InputData[] data, Window parent) {

		List<AfirmaPlugin> pluginsList;

		try {
			pluginsList = PluginsManager.getInstance().getPluginsLoadedList();
		} catch (final PluginException e) {
			return;
		}

		int posicionPlugin = -1;

		for (int i = 0; i < pluginsList.size(); i++) {

			if (pluginsList.get(i).getInfo().toString().equals(Propiedades.getString(Propiedades.PROP_PLUGIN_NAME))) {
				posicionPlugin = i;
			}
		}

		AfirmaPlugin plugin;

		if (posicionPlugin == -1) {
			return;
		} else {
			plugin = pluginsList.get(posicionPlugin);
		}

		final PluginsPreferences preferences = PluginsPreferences.getInstance(plugin);

		PluginConfigurationDialog dialog;
		try {
			dialog = new PluginConfigurationDialog(SwingUtilities.getWindowAncestor(parent), plugin);
		} catch (final PluginException e) {
			return;
		}
		dialog.init(preferences.recoverConfig());
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(final WindowEvent e) {
				if (dialog.isAccepted()) {
					preferences.saveConfig(dialog.recoverConfig());
				}
			}
		});
		dialog.setVisible(true);
	}

}
