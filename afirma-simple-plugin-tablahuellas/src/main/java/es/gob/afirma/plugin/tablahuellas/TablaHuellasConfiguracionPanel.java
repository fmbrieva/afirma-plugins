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

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import es.gob.afirma.standalone.plugins.ConfigurationPanel;

/**
 * Panel de configuracion
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class TablaHuellasConfiguracionPanel extends ConfigurationPanel {

	private static final long serialVersionUID = 1L;

	private JPanel mensajeEtiquetas = new JPanel();

	// Botones
	private JButton botonAyuda;
	
	public TablaHuellasConfiguracionPanel() {

		// Definir Layout
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gbc = new GridBagConstraints();

		// Botón de ayuda
		gbc.insets = new Insets(15, 15, 3, 15);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		botonAyuda = new JButton(Messages.getString("ConfiguracionBotonTexto.0"));

		botonAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Mostrar fichero de ayuda
				try {
					InputStream inputStream = getClass().getClassLoader()
							.getResourceAsStream(Propiedades.getString(Propiedades.PROP_RUTA_RECURSOS)
									+ Propiedades.getString(Propiedades.PROP_FICHERO_AYUDA));

					File tempFile = File.createTempFile(Propiedades.getString(Propiedades.PROP_PREFIJO_MANUAL),
							Propiedades.getString(Propiedades.PROP_SEPARADOR_EXTENSION)
									+ Propiedades.getString(Propiedades.PROP_EXTENSION_PDF));

					FileOutputStream fos = new FileOutputStream(tempFile);
					while (inputStream.available() > 0) {
						fos.write(inputStream.read());
					}
					fos.close();

					Desktop.getDesktop().open(tempFile);

					tempFile.deleteOnExit();

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		this.add(botonAyuda, gbc);

		// Mensaje etiquetas
		mensajeEtiquetas.setBorder(new BevelBorder(BevelBorder.LOWERED));
		mensajeEtiquetas.add(new JLabel(Messages.getString("ConfiguracionMensaje.0")));
		gbc.insets = new Insets(10, 15, 15, 15);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(mensajeEtiquetas, gbc);

		
	}

	@Override
	public void init(final Properties config) {

		Propiedades.etiquetasOriginal.clear();

		if (config.isEmpty()) {
			
		} else {
						
		}

	}

	@Override
	public Properties getConfiguration() {

		// Crear el objeto de propiedades y guardar los valores

		final Properties config = new Properties();
		config.setProperty(Propiedades.getString(Propiedades.PROP_CONFIG_ETIQUETAS), "");

		return config;
	}
}