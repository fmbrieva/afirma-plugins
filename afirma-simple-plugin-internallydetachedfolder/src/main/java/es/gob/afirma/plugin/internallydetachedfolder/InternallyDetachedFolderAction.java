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

import java.awt.Container;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.gob.afirma.core.AOCancelledOperationException;
import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.misc.Platform;
import es.gob.afirma.core.ui.AOUIFactory;
import es.gob.afirma.standalone.plugins.DataProcessAction;
import es.gob.afirma.standalone.plugins.InputData;

/**
 * Acción de validación para extraer documentos almacenados en archivos de firma
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class InternallyDetachedFolderAction extends DataProcessAction {

	static final Logger LOGGER = Logger.getLogger(InternallyDetachedFolderAction.class.getName());

	private JDialog dialogoEntrada = null;

	private JButton botonExtraer = new JButton(Messages.getString("FolderActionTexto.0"));
	private JButton botonCancelar = new JButton(Messages.getString("FolderActionTexto.1"));

	private JButton botonExaminarXML = new JButton(Messages.getString("FolderActionTexto.2"));
	private JButton botonExaminarPDF = new JButton(Messages.getString("FolderActionTexto.2"));

	private final JTextField directorioXML = new JTextField();
	private final JTextField directorioPDF = new JTextField();

	private JLabel etiquetaDirectorioXML = new JLabel(Messages.getString("FolderActionTexto.3"));
	private JLabel etiquetaDirectorioPDF = new JLabel(Messages.getString("FolderActionTexto.4"));

	private final JCheckBox checkBoxRecursivo = new JCheckBox(Messages.getString("FolderActionTexto.5"));
	private final JCheckBox checkBoxMismoDirectorio = new JCheckBox(Messages.getString("FolderActionTexto.6"));

	private final JPanel panelAceptarCancelar = new JPanel();

	boolean extraerDocumentos = false;

	JDialog waitDialog = null;

	String extensionMimeType = Propiedades.getString(Propiedades.PROP_EXTENSION_PDF);

    public InternallyDetachedFolderAction() {
        createUI();
    }
    
	@Override
	public void processData(InputData[] data, Window parent) {

		this.dialogoEntrada.setResizable(false);
		this.dialogoEntrada.setSize(600, 350);
		this.dialogoEntrada.setLocationRelativeTo(parent);
		this.dialogoEntrada.setVisible(true);

		if (extraerDocumentos) {

			final SwingWorker<?, ?> validationWorker = extraerDocumentosXML();
			showWaitDialog(parent, validationWorker);
			AOUIFactory.showMessageDialog(
					parent,
					Messages.getString("FolderActionTexto.12"),
					Messages.getString("FolderActionTexto.11"),
					JOptionPane.PLAIN_MESSAGE
				);
		}

	}

	private void createUI() {

		// Etiqueta para visualizar directorio XML
		directorioXML.setEditable(false);
		directorioXML.setFocusable(false);
		directorioXML.setColumns(10);

		// Botón para examinar el directorio XML
		botonExaminarXML.setEnabled(true);

		botonExaminarXML.addActionListener(e -> {
			try {
				directorioXML.setText(AOUIFactory.getLoadFiles(Messages.getString("FolderActionTexto.2"), null, null,
						null, Messages.getString("FolderActionTexto.8"), true, false, null, null)[0]
								.getAbsolutePath());
				ajustarEstadobotonExtraer();
			} catch (final AOCancelledOperationException ex) {
				// Operacion cancelada por el usuario
			}
		});

		// Eiqueta para visualizar directorio PDF
		directorioPDF.setEditable(false);
		directorioPDF.setFocusable(false);
		directorioPDF.setColumns(10);

		// Botón para examinar el directorio PDF
		botonExaminarPDF.setEnabled(true);

		botonExaminarPDF.addActionListener(e -> {
			try {
				directorioPDF.setText(AOUIFactory.getLoadFiles(Messages.getString("FolderActionTexto.2"), null, null,
						null, Messages.getString("FolderActionTexto.9"), true, false, null, null)[0]
								.getAbsolutePath());
				ajustarEstadobotonExtraer();
			} catch (final AOCancelledOperationException ex) {
				// Operacion cancelada por el usuario
			}
		});

		// Checkbox recursivo
		checkBoxRecursivo.setSelected(true);

		// Checkbox recursivo
		checkBoxMismoDirectorio.setSelected(false);

		checkBoxMismoDirectorio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkBoxMismoDirectorio.isSelected()) {
					directorioPDF.setText("");
					directorioPDF.setEnabled(false);
					botonExaminarPDF.setEnabled(false);
					ajustarEstadobotonExtraer();
				} else {
					directorioPDF.setEnabled(true);
					botonExaminarPDF.setEnabled(true);
					ajustarEstadobotonExtraer();
				}
			}
		});

		// Boton Aceptar
		botonExtraer.setEnabled(false);

		botonExtraer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogoEntrada.dispose();
				extraerDocumentos = true;
			}
		});

		// Boton Cancelar
		botonCancelar.setEnabled(true);

		botonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogoEntrada.dispose();
				extraerDocumentos = false;

			}
		});

		// Panel con botones centrado

		panelAceptarCancelar.setLayout(new FlowLayout(FlowLayout.CENTER));

		// En Mac OS X el orden de los botones es distinto
		if (Platform.OS.MACOSX.equals(Platform.getOS())) {
			panelAceptarCancelar.add(botonCancelar);
			panelAceptarCancelar.add(botonExtraer);
		} else {
			panelAceptarCancelar.add(botonExtraer);
			panelAceptarCancelar.add(botonCancelar);
		}

		dialogoEntrada = new JDialog(null, Messages.getString("FolderActionTexto.7"), ModalityType.APPLICATION_MODAL);

		final Container c = this.dialogoEntrada.getContentPane();
		final GridBagLayout gbl = new GridBagLayout();
		c.setLayout(gbl);
		final GridBagConstraints gbc = new GridBagConstraints();

		this.dialogoEntrada.setLocationRelativeTo(null);

		// Añadir elementos a dialogoEntrada

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weightx = 1.0;
		gbc.insets = new Insets(15, 10, 5, 10);
		this.dialogoEntrada.add(etiquetaDirectorioXML, gbc);

		gbc.insets = new Insets(0, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		c.add(directorioXML, gbc);

		gbc.gridx = 1;
		c.add(botonExaminarXML, gbc);

		gbc.insets = new Insets(15, 10, 5, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		// gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.dialogoEntrada.add(etiquetaDirectorioPDF, gbc);

		gbc.insets = new Insets(0, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		c.add(directorioPDF, gbc);

		gbc.gridx = 1;
		c.add(botonExaminarPDF, gbc);

		gbc.insets = new Insets(15, 10, 5, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		this.dialogoEntrada.add(checkBoxRecursivo, gbc);

		gbc.insets = new Insets(15, 10, 5, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		this.dialogoEntrada.add(checkBoxMismoDirectorio, gbc);

		gbc.insets = new Insets(15, 10, 5, 10);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		c.add(panelAceptarCancelar, gbc);

	}
	
	private void ajustarEstadobotonExtraer() {

		if ((directorioXML.getText().length() > 0 && directorioPDF.getText().length() > 0)
				|| (directorioXML.getText().length() > 0 && checkBoxMismoDirectorio.isSelected())) {
			botonExtraer.setEnabled(true);
		} else {
			botonExtraer.setEnabled(false);
		}

	}

	private SwingWorker<Void, Void> extraerDocumentosXML() {

		final SwingWorker<Void, Void> validationWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				leerDirectorio(directorioXML.getText(), checkBoxRecursivo.isSelected());
				return null;
			}

			@Override
			protected void done() {

				hideWaitDialog();

			}

		};

		validationWorker.execute();
		return validationWorker;

	}

	public void leerDirectorio(String rutaDirectorioXML, boolean recursivo) {

		File root = new File(rutaDirectorioXML);

		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				if (recursivo) {
					leerDirectorio(f.getAbsolutePath(), recursivo);
				}
			} else {
				try {
					Path path = f.toPath();
					String mimeType = Files.probeContentType(path);

					// Windows: text/xml
					// Linux: application/xml
					// Multiplataforma comprobar endsWith /xml
					if (mimeType.endsWith(Propiedades.getString(Propiedades.PROP_MIMETYPE_XML))) {
						extraerPDFEmbebido(f);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void showWaitDialog(Window parent, SwingWorker<?, ?> worker) {
		this.waitDialog = new JDialog(parent, Messages.getString("FolderActionTexto.10"), ModalityType.APPLICATION_MODAL);
		this.waitDialog.setLayout(new GridBagLayout());

		final JLabel textLabel = new JLabel(Messages.getString("FolderActionTexto.11"));
		textLabel.setMinimumSize(new Dimension(250, 15));
		final JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.gridy = 0;
		c.insets = new Insets(11, 11, 0, 11);
		this.waitDialog.add(textLabel, c);
		c.gridy++;
		c.insets = new Insets(6, 11, 11, 11);
		this.waitDialog.add(progressBar, c);

		this.waitDialog.pack();
		this.waitDialog.setLocationRelativeTo(parent);
		this.waitDialog.setVisible(true);

		if (worker != null) {
			worker.cancel(true);
		}

	}

	void hideWaitDialog() {
		this.waitDialog.dispose();
	}

	// Extraer el documento embebido en el documento XML (Habitualmente será PDF)
	public void extraerPDFEmbebido(File fichero) {

		try {

			File inputFile = new File(fichero.getAbsolutePath());

			// Extraer el contenido de la etiqueta con el original
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);

			Node nodeElement = doc.getDocumentElement();

			nodeElement.normalize();

			String rootNodeName = nodeElement.getNodeName();

			String originalEmbebido = "";

			for (int i = 0; i < Propiedades.etiquetasOriginal.size(); i++) {

				ArrayList<String> listaEtiquetasOriginal = new ArrayList<String>(
						Arrays.asList(Propiedades.etiquetasOriginal.get(i)
								.split(Propiedades.getString(Propiedades.PROP_SEPARADOR_ETIQUETA))));

				if (rootNodeName.equals(listaEtiquetasOriginal.get(0))) {
					listaEtiquetasOriginal.remove(0);
					originalEmbebido = getOriginalEmbebido(listaEtiquetasOriginal, nodeElement);
					try {
						if (!originalEmbebido.equals("")) {
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (originalEmbebido.equals("")) {
				return;
			}

			// Decodificar el contenido de la etiqueta con el documento original byte[]
			byte[] decodedBytes = Base64.decode(originalEmbebido);

			String ficheroPdfEmbebido = fichero.getAbsolutePath() + "." + extensionMimeType;

			if (!checkBoxMismoDirectorio.isSelected()) {

				ficheroPdfEmbebido = ficheroPdfEmbebido.replace(directorioXML.getText(), directorioPDF.getText());
				// Comprobar que exista el directorio
				Files.createDirectories(
						Paths.get(fichero.getParent().replace(directorioXML.getText(), directorioPDF.getText())));
			}

			// Crear fichero con el contenido extraido
			File tempFile = new File(ficheroPdfEmbebido);

			OutputStream opStream = null;
			opStream = new FileOutputStream(tempFile);
			opStream.write(decodedBytes);
			opStream.flush();
			opStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Obtener el contenido de la etiqueta con el documento original
	public String getOriginalEmbebido(ArrayList<String> listaEtiquetasOriginal, Node nodeElement) {

		NodeList nodosHijos = nodeElement.getChildNodes();

		int length = nodosHijos.getLength();

		for (int i = 0; i < length; i++) {
			if (nodosHijos.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element nodoHijo = (Element) nodosHijos.item(i);

				if (nodoHijo.getNodeName().equals(listaEtiquetasOriginal.get(0))) {
					if (listaEtiquetasOriginal.size() > 1) {
						listaEtiquetasOriginal.remove(0);
						return getOriginalEmbebido(listaEtiquetasOriginal, nodoHijo);
					} else {
						// Consultar si existe el atributo MimeType para asignar la extensión correcta
						// del fichero
						String atributoMimeType = "";
						atributoMimeType = nodoHijo
								.getAttribute(Propiedades.getString(Propiedades.PROP_ATTRIBUTE_MIMETYPE));
						if (atributoMimeType.equals(Propiedades.getString(Propiedades.PROP_MIMETYPE_PDF))) {
							extensionMimeType = Propiedades.getString(Propiedades.PROP_EXTENSION_PDF);
						} else if (atributoMimeType.equals(Propiedades.getString(Propiedades.PROP_MIMETYPE_TXT))) {
							extensionMimeType = Propiedades.getString(Propiedades.PROP_EXTENSION_TXT);
						} else {
							extensionMimeType = Propiedades.getString(Propiedades.PROP_EXTENSION_PDF);
						}
						return nodoHijo.getChildNodes().item(0).getNodeValue();
					}
				}
			}
		}

		return "";

	}

}
