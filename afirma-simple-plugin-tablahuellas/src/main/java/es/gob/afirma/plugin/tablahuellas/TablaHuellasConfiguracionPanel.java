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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import es.gob.afirma.standalone.plugins.ConfigurationPanel;

/**
 * Panel de configuracion
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class TablaHuellasConfiguracionPanel extends ConfigurationPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelEjemploRemisionDocumentos = new JPanel();

	private TitledBorder borderEjemploRemisionDocumentos = new TitledBorder(
			Messages.getString("ConfiguracionMensaje.1"));

	// Botones
	private JButton botonAyuda;
	private JButton botonVerDocumento;

	private final JComboBox<String> comboDocumentos = new JComboBox<>();

	private JLabel labelDocumentosParaRemitir = new JLabel(Messages.getString("ConfiguracionBotonTexto.2"));

	private JSeparator separador01 = new JSeparator();
	private JSeparator separador02 = new JSeparator();
	private JSeparator separador03 = new JSeparator();

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
				mostrarPdf(Propiedades.getString(Propiedades.PROP_FICHERO_AYUDA));
			}
		});

		this.add(botonAyuda, gbc);

		// Combo para seleccionar el algoritmo de Huella Digital

		for (int i = 0; i < Propiedades.EJEMPLO_DOCUMENTOS.length; i++) {

			comboDocumentos.addItem(Propiedades.EJEMPLO_DOCUMENTOS[i][0]);
		}

		// comboDocumentos.setSelectedIndex(1);
		/*
		 * comboDocumentos.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) {
		 * JOptionPane.showMessageDialog(null, Propiedades.EJEMPLO_DOCUMENTOS[(int)
		 * comboDocumentos.getSelectedItem()][1]);
		 * 
		 * // mostrarPdf(Propiedades.getString(Propiedades.PROP_FICHERO_DOCUMENTO1)); }
		 * });
		 */
		// gbc.gridx = 0;
		// gbc.gridy++;
		// gbc.gridwidth = GridBagConstraints.REMAINDER;
		// gbc.fill = GridBagConstraints.HORIZONTAL;

		// separador.setOrientation(SwingConstants.HORIZONTAL);
		// this.add(separador, gbc);

		botonVerDocumento = new JButton(Messages.getString("ConfiguracionBotonTexto.1"));
		botonVerDocumento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPdf(Propiedades.EJEMPLO_DOCUMENTOS[(int) comboDocumentos.getSelectedIndex()][1].toString());
			}
		});

		final GridBagConstraints remisionGbc = new GridBagConstraints();
		borderEjemploRemisionDocumentos.setTitleJustification(TitledBorder.LEFT);
		borderEjemploRemisionDocumentos.setTitlePosition(TitledBorder.TOP);

		panelEjemploRemisionDocumentos.setLayout(new GridBagLayout());
		panelEjemploRemisionDocumentos.setBorder(borderEjemploRemisionDocumentos);

		remisionGbc.insets = new Insets(15, 15, 3, 15);

		remisionGbc.gridx = 0;
		remisionGbc.gridy = 0;
		remisionGbc.gridwidth = GridBagConstraints.REMAINDER;
		remisionGbc.fill = GridBagConstraints.HORIZONTAL;

		separador01.setOrientation(SwingConstants.HORIZONTAL);
		panelEjemploRemisionDocumentos.add(separador01, remisionGbc);

		remisionGbc.gridx = 0;
		remisionGbc.gridy++;

		panelEjemploRemisionDocumentos.add(this.labelDocumentosParaRemitir, remisionGbc);

		remisionGbc.gridx = 0;
		remisionGbc.gridy++;
		remisionGbc.gridwidth = 1;
		remisionGbc.fill = GridBagConstraints.NONE;
		panelEjemploRemisionDocumentos.add(comboDocumentos, remisionGbc);

		remisionGbc.gridx = 1;
		panelEjemploRemisionDocumentos.add(botonVerDocumento, remisionGbc);

		separador02.setOrientation(SwingConstants.HORIZONTAL);
		panelEjemploRemisionDocumentos.add(separador02, remisionGbc);

		remisionGbc.gridx = 0;
		remisionGbc.gridy++;
		remisionGbc.gridwidth = GridBagConstraints.REMAINDER;
		remisionGbc.fill = GridBagConstraints.HORIZONTAL;

		separador03.setOrientation(SwingConstants.HORIZONTAL);
		panelEjemploRemisionDocumentos.add(separador03, remisionGbc);

		// Mensaje etiquetas

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(panelEjemploRemisionDocumentos, gbc);

	}

	public void mostrarPdf(String ficheroPdf) {

		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(Propiedades.getString(Propiedades.PROP_RUTA_RECURSOS) + ficheroPdf);

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