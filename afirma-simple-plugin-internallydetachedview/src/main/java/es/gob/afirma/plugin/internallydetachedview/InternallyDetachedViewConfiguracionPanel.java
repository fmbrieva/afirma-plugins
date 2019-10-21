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

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import es.gob.afirma.standalone.plugins.ConfigurationPanel;

import es.gob.afirma.plugin.internallydetachedview.Messages;
import es.gob.afirma.plugin.internallydetachedview.Propiedades;

/**
 * Panel de configuracion
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class InternallyDetachedViewConfiguracionPanel extends ConfigurationPanel {

	private static final long serialVersionUID = 1L;

	private JPanel mensajeEtiquetas = new JPanel();

	private final JTextField etiquetaNueva;

	final DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> etiquetasOriginales;
	private JScrollPane scrollpane;

	// Botones
	private JButton botonAyuda;
	private JButton botonBorrar;
	private JButton botonAnadir;

	public InternallyDetachedViewConfiguracionPanel() {

		// Definir Layout
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gbc = new GridBagConstraints();

		// Botón de ayuda
		gbc.insets = new Insets(15, 15, 3, 15);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		botonAyuda = new JButton(Messages.getString("BotonTexto.0"));

		botonAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Mostrar fichero de ayuda
				try {
					InputStream inputStream = getClass().getClassLoader()
							.getResourceAsStream(Propiedades.getString(Propiedades.PROP_RUTA_RECURSOS)
									+ Propiedades.getString(Propiedades.PROP_FICHERO_AYUDA));

					File tempFile = new File(Propiedades.getString(Propiedades.PROP_FICHERO_AYUDA));
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

		// Etiquetas
		this.etiquetaNueva = new JTextField();
		final JLabel lblNuevasEtiquetas = new JLabel(Messages.getString("ConfiguracionMensaje.1"));
		lblNuevasEtiquetas.setLabelFor(this.etiquetaNueva);

		gbc.insets = new Insets(0, 15, 3, 15);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(lblNuevasEtiquetas, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(this.etiquetaNueva, gbc);

		// Botón para añadir y borrar etiquetas
		botonAnadir = new JButton(Messages.getString("BotonTexto.1"));

		botonAnadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!etiquetaNueva.getText().isEmpty()) {
					model.addElement(etiquetaNueva.getText());
					etiquetaNueva.setText("");
				}
			}
		});

		botonBorrar = new JButton(Messages.getString("BotonTexto.2"));

		botonBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ListSelectionModel selmodel = etiquetasOriginales.getSelectionModel();
				int indexA = selmodel.getMinSelectionIndex();
				if (indexA >= 0) {
					model.remove(indexA);
				}

			}
		});

		JPanel botonesNuevasEtiquetas = new JPanel();
		botonesNuevasEtiquetas.add(botonAnadir);
		botonesNuevasEtiquetas.add(botonBorrar);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;

		this.add(botonesNuevasEtiquetas, gbc);

		// Etiquetas añadidas para localizar documentos originales en archivos de firma
		// XML

		etiquetasOriginales = new JList<String>(model);
		etiquetasOriginales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		etiquetasOriginales.setSelectedIndex(0);

		etiquetasOriginales.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()) {
				}
				if (etiquetasOriginales.getSelectedIndex() == -1) {
					botonBorrar.setEnabled(false);

				} else {
					botonBorrar.setEnabled(true);
				}
			}
		});
		scrollpane = new JScrollPane(etiquetasOriginales);

		gbc.insets = new Insets(20, 15, 3, 15);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		add(new JLabel(Messages.getString("ConfiguracionMensaje.2")), gbc);

		gbc.insets = new Insets(0, 15, 3, 15);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;

		scrollpane.setPreferredSize(
				new Dimension(scrollpane.getWidth(), (int) scrollpane.getPreferredSize().getHeight()));

		add(scrollpane, gbc);

	}

	@Override
	public void init(final Properties config) {

		Propiedades.etiquetasOriginal.clear();

		if (config.isEmpty()) {
			// Si el fichero de configuracion no tiene datos cargar los valores base por
			// defecto
			for (int i = 0; i < Propiedades.ETIQUETAS_ORIGINAL_BASE.size(); i++) {
				model.addElement(Propiedades.ETIQUETAS_ORIGINAL_BASE.get(i));
				Propiedades.etiquetasOriginal.add(Propiedades.ETIQUETAS_ORIGINAL_BASE.get(i));
			}
		} else {
			// Recuperar todas las etiquetas del fichero de configuración
			//
			// ETIQUETAS + SEPARADOR_ETIQUETAS
			//
			String[] etiquetas = config.getProperty(Propiedades.getString(Propiedades.PROP_CONFIG_ETIQUETAS))
					.split(Propiedades.getString(Propiedades.PROP_SEPARADOR_LISTA_ETIQUETAS));

			for (int i = 0; i < etiquetas.length; i++) {
				model.addElement(etiquetas[i]);
				Propiedades.etiquetasOriginal.add(etiquetas[i]);
			}
		}

	}

	@Override
	public Properties getConfiguration() {

		// Crear el objeto de propiedades y guardar los valores
		String etiquetasOriginalConSeparador = "";

		Propiedades.etiquetasOriginal.clear();

		for (int i = 0; i < model.getSize(); i++) {
			etiquetasOriginalConSeparador = etiquetasOriginalConSeparador + model.getElementAt(i)
					+ Propiedades.getString(Propiedades.PROP_SEPARADOR_LISTA_ETIQUETAS);
			Propiedades.etiquetasOriginal.add(model.getElementAt(i));

		}

		final Properties config = new Properties();
		config.setProperty(Propiedades.getString(Propiedades.PROP_CONFIG_ETIQUETAS), etiquetasOriginalConSeparador);

		return config;
	}
}