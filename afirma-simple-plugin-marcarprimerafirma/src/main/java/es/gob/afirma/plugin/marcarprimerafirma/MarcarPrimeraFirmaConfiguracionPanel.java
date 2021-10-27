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

package es.gob.afirma.plugin.marcarprimerafirma;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import es.gob.afirma.standalone.plugins.ConfigurationPanel;

/**
 * Panel de configuracion
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class MarcarPrimeraFirmaConfiguracionPanel extends ConfigurationPanel {

	private static final long serialVersionUID = 1L;

	private JPanel panelFirma = new JPanel();
	private JPanel panelAspecto = new JPanel();
	private JPanel panelPosicion = new JPanel();

	private TitledBorder borderFirma = new TitledBorder(Messages.getString("ConfiguracionMensaje.5"));
	private TitledBorder borderAspecto = new TitledBorder(Messages.getString("ConfiguracionMensaje.7"));
	private TitledBorder borderPosicion = new TitledBorder(Messages.getString("ConfiguracionMensaje.8"));

	// Botones
	private JButton botonAyuda = new JButton(Messages.getString("ConfiguracionMensaje.1"));
	private JButton botonAspectoPosicion = new JButton(Messages.getString("ConfiguracionMensaje.16"));
	private JButton botonResetPlugin = new JButton(Messages.getString("ConfiguracionMensaje.13"));
	private JButton botonColor = new JButton(Messages.getString("ConfiguracionMensaje.14"));

	private JLabel labelColorFirma = new JLabel(Messages.getString("ConfiguracionMensaje.15"));
	private JLabel labelTamanoFuente = new JLabel(Messages.getString("ConfiguracionMensaje.2"));
	private JLabel labelGrosorLinea = new JLabel(Messages.getString("ConfiguracionMensaje.4"));
	private JCheckBox checkBoxRectanguloMostrarLinea = new JCheckBox(Messages.getString("ConfiguracionMensaje.3"));
	private JCheckBox checkSoloPrimeraPagina = new JCheckBox(Messages.getString("ConfiguracionMensaje.18"));

	private JLabel labelFormatoHoja = new JLabel(Messages.getString("ConfiguracionMensaje.12"));
	private JLabel labelposicionH = new JLabel(Messages.getString("ConfiguracionMensaje.10"));
	private JLabel labelposicionW = new JLabel(Messages.getString("ConfiguracionMensaje.11"));

	private JTextField textLinea1 = new JTextField();
	private JTextField textLinea2 = new JTextField();

	private JTextField textColor = new JTextField("", 6);

	private JTextField textTamanoFuente = new JTextField("8", 3);
	private JTextField textGrosorLinea = new JTextField("1", 3);

	private JTextField textFirmaposicionH = new JTextField("", 3);
	private JTextField textFirmaposicionW = new JTextField("", 3);

	private JComboBox<String> comboHoja = new JComboBox<>();

	private Color colorFirma;

	private int a4HorizontalW;
	private int a4HorizontalY;
	private int a4VerticalH;
	private int a4VerticalY;
	private int a3HorizontalW;
	private int a3HorizontalY;
	private int a3VerticalH;
	private int a3VerticalY;
	private int defectoH;
	private int defectoW;

	private JDialog dialogoAviso = null;
	private final JPanel panelAviso = new JPanel();
	private final TitledBorder borderAviso = new TitledBorder(Messages.getString("AccionMensaje.0"));
	private JSeparator separador01 = new JSeparator();
	private final JCheckBox checkBoxActivarPlugin = new JCheckBox(Messages.getString("AccionMensaje.5"));
	private JButton botonSalir = new JButton(Messages.getString("AccionMensaje.9"));
	private JTextPane avisoPane = new JTextPane();
	private StringBuilder mensaje = new StringBuilder();
	private String color = "";
	private String estado = "";

	public MarcarPrimeraFirmaConfiguracionPanel() {

		crearPanel();

	}

	public void crearPanel() {

		// Definir Layout
		this.setLayout(new GridBagLayout());

		final GridBagConstraints gbc = new GridBagConstraints();

		//

		botonAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPdf(Propiedades.getString(Propiedades.PROP_FICHERO_AYUDA));
			}
		});

		botonAspectoPosicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VerDialogoAspectoPosicion();

			}
		});

		botonResetPlugin.setBackground(Color.ORANGE);

		botonResetPlugin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetPlugin();
			}
		});

		textColor.setBackground(colorFirma);
		labelColorFirma.setForeground(colorFirma);

		botonColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				colorFirma = JColorChooser.showDialog(null, Messages.getString("AccionMensaje.17"), colorFirma);

				textColor.setBackground(colorFirma);
				labelColorFirma.setForeground(colorFirma);

			}
		});

		if (checkBoxRectanguloMostrarLinea.isSelected()) {
			textGrosorLinea.setEnabled(true);
		} else {
			textGrosorLinea.setEnabled(false);
		}

		checkBoxRectanguloMostrarLinea.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkBoxRectanguloMostrarLinea.isSelected()) {
					textGrosorLinea.setEnabled(true);
				} else {
					textGrosorLinea.setEnabled(false);
				}

			}
		});

		mensaje.append(Messages.getString("AccionMensaje.1"));
		mensaje.append(Messages.getString("AccionMensaje.2"));
		mensaje.append(Messages.getString("AccionMensaje.3"));
		mensaje.append(Messages.getString("AccionMensaje.4"));

		avisoPane.setContentType("text/html");
		avisoPane.setText(mensaje.toString());

		avisoPane.setBorder(BorderFactory.createEmptyBorder());

		ajustarPanelAviso();

		checkBoxActivarPlugin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				ajustarPanelAviso();
			}
		});

		for (int i = 0; i < Propiedades.HOJAS_FORMATO.length; i++) {

			this.comboHoja.addItem(Propiedades.HOJAS_FORMATO[i][0]);
		}

		labelposicionH.setText(Messages.getString("ConfiguracionMensaje.10").replace(
				Propiedades.getString(Propiedades.PROP_CAMPO_POSICION_H),
				Propiedades.HOJAS_FORMATO[(int) comboHoja.getSelectedIndex()][1].toString()));
		labelposicionW.setText(Messages.getString("ConfiguracionMensaje.11").replace(
				Propiedades.getString(Propiedades.PROP_CAMPO_POSICION_W),
				Propiedades.HOJAS_FORMATO[(int) comboHoja.getSelectedIndex()][2].toString()));

		textFirmaposicionH.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				ajustarFocusLost();
			}

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		textFirmaposicionW.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent arg0) {
				ajustarFocusLost();
			}

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		comboHoja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				labelposicionH.setText(Messages.getString("ConfiguracionMensaje.10").replace(
						Propiedades.getString(Propiedades.PROP_CAMPO_POSICION_H),
						Propiedades.HOJAS_FORMATO[(int) comboHoja.getSelectedIndex()][1].toString()));
				labelposicionW.setText(Messages.getString("ConfiguracionMensaje.11").replace(
						Propiedades.getString(Propiedades.PROP_CAMPO_POSICION_W),
						Propiedades.HOJAS_FORMATO[(int) comboHoja.getSelectedIndex()][2].toString()));

				ajustarTextoFirmaPosicion();

			}
		});

		// Boton Salir
		botonSalir.setEnabled(true);
		botonSalir.setPreferredSize(new Dimension(80, 40));

		botonSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialogoAviso.dispose();

			}
		});

		// PANELES -----------------------------------------------------------------

		final GridBagConstraints avisoGbc = new GridBagConstraints();

		panelAviso.setLayout(new GridBagLayout());
		panelAviso.setBorder(borderAviso);

		avisoGbc.gridx = 0;
		avisoGbc.gridy = 0;
		avisoGbc.gridwidth = 1;
		avisoGbc.gridheight = 1;
		avisoGbc.weightx = 1.0;
		avisoGbc.fill = GridBagConstraints.BOTH;
		panelAviso.add(avisoPane, avisoGbc);

		avisoGbc.gridx = 0;
		avisoGbc.gridy++;
		avisoGbc.gridwidth = GridBagConstraints.REMAINDER;
		avisoGbc.fill = GridBagConstraints.HORIZONTAL;
		avisoGbc.weighty = 2;
		separador01.setOrientation(SwingConstants.HORIZONTAL);
		panelAviso.add(separador01, avisoGbc);

		avisoGbc.gridx = 0;
		avisoGbc.gridy++;
		avisoGbc.anchor = GridBagConstraints.WEST;
		panelAviso.add(checkBoxActivarPlugin, avisoGbc);

		final GridBagConstraints firmaGbc = new GridBagConstraints();
		borderFirma.setTitleJustification(TitledBorder.LEFT);
		borderFirma.setTitlePosition(TitledBorder.TOP);

		panelFirma.setLayout(new GridBagLayout());
		panelFirma.setBorder(borderFirma);

		// firmaGbc.insets = new Insets(15, 15, 3, 15);

		firmaGbc.gridx = 0;
		firmaGbc.gridy = 0;
		firmaGbc.fill = GridBagConstraints.HORIZONTAL;
		firmaGbc.gridwidth = 1;
		firmaGbc.gridheight = 1;
		firmaGbc.weightx = 1.0;

		textLinea1.addKeyListener(new java.awt.event.KeyAdapter() {

			public void keyTyped(java.awt.event.KeyEvent evt) {
				if ((textLinea1.getText() + evt.getKeyChar()).length() > Integer
						.parseInt(Propiedades.getString(Propiedades.PROP_LONGITUD_MAXIMA_LINEA_FIRMA))) {
					evt.consume();
				}
			}
		});

		panelFirma.add(textLinea1, firmaGbc);

		firmaGbc.gridx = 0;
		firmaGbc.gridy++;
		textLinea2.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				if ((textLinea2.getText() + evt.getKeyChar()).length() > Integer
						.parseInt(Propiedades.getString(Propiedades.PROP_LONGITUD_MAXIMA_LINEA_FIRMA))) {
					evt.consume();
				}
			}
		});

		panelFirma.add(textLinea2, firmaGbc);

		// Mensaje etiquetas

		gbc.insets = new Insets(15, 15, 3, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(botonResetPlugin, gbc);

		gbc.gridx = 1;
		this.add(botonAspectoPosicion, gbc);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.EAST;
		this.add(botonAyuda, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(panelAviso, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(panelFirma, gbc);

	}

	private void VerDialogoAspectoPosicion() {

		final GridBagConstraints aspectoGbc = new GridBagConstraints();

		borderAspecto.setTitleJustification(TitledBorder.LEFT);
		borderAspecto.setTitlePosition(TitledBorder.TOP);

		panelAspecto.setLayout(new GridBagLayout());
		panelAspecto.setBorder(borderAspecto);

		aspectoGbc.insets = new Insets(15, 15, 3, 15);

		textColor.setBackground(colorFirma);
		textColor.setEditable(false);

		aspectoGbc.gridx = 0;
		aspectoGbc.gridy = 0;
		aspectoGbc.anchor = GridBagConstraints.WEST;
		panelAspecto.add(botonColor, aspectoGbc);

		aspectoGbc.gridx = 1;
		panelAspecto.add(textColor, aspectoGbc);

		aspectoGbc.gridx = 2;
		panelAspecto.add(labelColorFirma, aspectoGbc);

		aspectoGbc.gridx = 0;
		aspectoGbc.gridy++;
		aspectoGbc.anchor = GridBagConstraints.WEST;
		panelAspecto.add(checkBoxRectanguloMostrarLinea, aspectoGbc);

		aspectoGbc.gridx = 1;
		panelAspecto.add(labelGrosorLinea, aspectoGbc);

		aspectoGbc.gridx = 2;
		panelAspecto.add(textGrosorLinea, aspectoGbc);

		aspectoGbc.gridx = 1;
		aspectoGbc.gridy++;

		panelAspecto.add(this.labelTamanoFuente, aspectoGbc);

		aspectoGbc.gridx = 2;
		panelAspecto.add(textTamanoFuente, aspectoGbc);

		// -----------------------------
		final GridBagConstraints posicionGbc = new GridBagConstraints();

		borderPosicion.setTitleJustification(TitledBorder.LEFT);
		borderPosicion.setTitlePosition(TitledBorder.TOP);

		panelPosicion.setLayout(new GridBagLayout());
		panelPosicion.setBorder(borderPosicion);

		posicionGbc.insets = new Insets(15, 15, 3, 15);

		posicionGbc.gridx = 0;
		posicionGbc.gridy = 0;
		posicionGbc.anchor = GridBagConstraints.WEST;
		panelPosicion.add(labelFormatoHoja, posicionGbc);

		posicionGbc.gridx = 1;
		panelPosicion.add(comboHoja, posicionGbc);

		posicionGbc.gridx = 2;
		panelPosicion.add(labelposicionH, posicionGbc);

		posicionGbc.gridx = 3;
		panelPosicion.add(textFirmaposicionH, posicionGbc);

		posicionGbc.gridx = 2;
		posicionGbc.gridy++;
		panelPosicion.add(labelposicionW, posicionGbc);

		posicionGbc.gridx = 3;
		panelPosicion.add(textFirmaposicionW, posicionGbc);

		posicionGbc.gridx = 0;
		posicionGbc.gridy++;
		posicionGbc.gridwidth = GridBagConstraints.REMAINDER;
		posicionGbc.fill = GridBagConstraints.HORIZONTAL;
		posicionGbc.anchor = GridBagConstraints.WEST;
		panelPosicion.add(checkSoloPrimeraPagina, posicionGbc);

		dialogoAviso = new JDialog(null, Messages.getString("AccionMensaje.7"), ModalityType.APPLICATION_MODAL);

		final Container container = this.dialogoAviso.getContentPane();
		final GridBagLayout containerGBL = new GridBagLayout();
		final GridBagConstraints containerGBC = new GridBagConstraints();

		container.setLayout(containerGBL);

		containerGBC.insets = new Insets(15, 10, 5, 10);
		containerGBC.gridx = 0;
		containerGBC.gridy = 0;
		containerGBC.weightx = 1.0;
		containerGBC.weighty = 1.0;
		containerGBC.fill = GridBagConstraints.BOTH;
		container.add(panelAspecto, containerGBC);

		containerGBC.gridx = 0;
		containerGBC.gridy++;
		container.add(panelPosicion, containerGBC);

		containerGBC.gridx = 0;
		containerGBC.gridy++;
		containerGBC.fill = GridBagConstraints.NONE;
		container.add(botonSalir, containerGBC);

		dialogoAviso.setResizable(false);
		dialogoAviso.setSize(550, 500);
		dialogoAviso.setLocationRelativeTo(this);
		dialogoAviso.setVisible(true);

	}

	public void ajustarTextoFirmaPosicion() {

		if (comboHoja.getSelectedItem().toString().equals(Propiedades.A4_VERTICAL)) {

			textFirmaposicionH.setText(String.valueOf(a4VerticalH));
			textFirmaposicionW.setText(String.valueOf(a4VerticalY));

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A4_HORIZONTAL)) {

			textFirmaposicionH.setText(String.valueOf(a4HorizontalW));
			textFirmaposicionW.setText(String.valueOf(a4HorizontalY));

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A3_HORIZONTAL)) {

			textFirmaposicionH.setText(String.valueOf(a3HorizontalW));
			textFirmaposicionW.setText(String.valueOf(a3HorizontalY));

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A3_VERTICAL)) {

			textFirmaposicionH.setText(String.valueOf(a3VerticalH));
			textFirmaposicionW.setText(String.valueOf(a3VerticalY));

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.VALOR_DEFECTO)) {

			textFirmaposicionH.setText(String.valueOf(defectoH));
			textFirmaposicionW.setText(String.valueOf(defectoW));

		}

	}

	public void ajustarFocusLost() {

		if (comboHoja.getSelectedItem().toString().equals(Propiedades.A4_VERTICAL)) {

			a4VerticalH = Integer.parseInt(textFirmaposicionH.getText());
			a4VerticalY = Integer.parseInt(textFirmaposicionW.getText());

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A4_HORIZONTAL)) {

			a4HorizontalW = Integer.parseInt(textFirmaposicionH.getText());
			a4HorizontalY = Integer.parseInt(textFirmaposicionW.getText());

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A3_VERTICAL)) {

			a3VerticalH = Integer.parseInt(textFirmaposicionH.getText());
			a3VerticalY = Integer.parseInt(textFirmaposicionW.getText());

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.A3_HORIZONTAL)) {

			a3HorizontalW = Integer.parseInt(textFirmaposicionH.getText());
			a3HorizontalY = Integer.parseInt(textFirmaposicionW.getText());

		} else if (comboHoja.getSelectedItem().toString().equals(Propiedades.VALOR_DEFECTO)) {

			defectoH = Integer.parseInt(textFirmaposicionH.getText());
			defectoW = Integer.parseInt(textFirmaposicionW.getText());

		}

	}

	public void ajustarPanelAviso() {

		if (checkBoxActivarPlugin.isSelected()) {
			color = Propiedades.getString(Propiedades.PROP_COLOR_ACTIVADO);
			estado = Propiedades.getString(Propiedades.PROP_TEXTO_ACTIVADO);

		} else {
			color = Propiedades.getString(Propiedades.PROP_COLOR_DESACTIVADO);
			estado = Propiedades.getString(Propiedades.PROP_TEXTO_DESACTIVADO);
		}

		avisoPane.setText(mensaje.toString().replace(Propiedades.getString(Propiedades.PROP_CAMPO_COLOR), color)
				.replace(Propiedades.getString(Propiedades.PROP_CAMPO_ESTADO), estado));

		Color backgroundColor = new Color(214, 217, 223);
		SimpleAttributeSet background = new SimpleAttributeSet();
		StyleConstants.setBackground(background, backgroundColor);
		avisoPane.getStyledDocument().setParagraphAttributes(0, avisoPane.getDocument().getLength(), background, false);

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

	public void resetPlugin() {

		textLinea1.setText(Propiedades.getString(Propiedades.PROP_SETUP_LINEA1));
		textLinea2.setText(Propiedades.getString(Propiedades.PROP_SETUP_LINEA2));
		checkBoxActivarPlugin
				.setSelected(Boolean.parseBoolean(Propiedades.getString(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN)));
		colorFirma = new Color(Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_COLOR_RED)),
				Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_COLOR_GREEN)),
				Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_COLOR_BLUE)));
		textGrosorLinea.setText(Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA));
		textTamanoFuente.setText(Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE));
		checkBoxRectanguloMostrarLinea.setSelected(
				Boolean.parseBoolean(Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA)));
		checkSoloPrimeraPagina
				.setSelected(Boolean.parseBoolean(Propiedades.getString(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA)));

		a4VerticalH = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_H));
		a4VerticalY = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_W));

		a4HorizontalW = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_H));
		a4HorizontalY = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_W));

		a3VerticalH = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_H));
		a3VerticalY = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_W));

		a3HorizontalW = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_H));
		a3HorizontalY = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_W));

		defectoH = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_H));
		defectoW = Integer.parseInt(Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_W));

	}

	@Override
	public void init(final Properties config) {

		if (config.isEmpty()) {

			resetPlugin();

		} else {

			textLinea1.setText(config.getProperty(Propiedades.PROP_SETUP_LINEA1));
			textLinea2.setText(config.getProperty(Propiedades.PROP_SETUP_LINEA2));
			checkBoxActivarPlugin
					.setSelected(Boolean.parseBoolean(config.getProperty(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN)));
			colorFirma = new Color(Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_COLOR_RED)),
					Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_COLOR_GREEN)),
					Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_COLOR_BLUE)));
			textGrosorLinea.setText(config.getProperty(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA));
			textTamanoFuente.setText(config.getProperty(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE));
			checkBoxRectanguloMostrarLinea.setSelected(
					Boolean.parseBoolean(config.getProperty(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA)));
			checkSoloPrimeraPagina
					.setSelected(Boolean.parseBoolean(config.getProperty(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA)));

			a4VerticalH = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A4_VERTICAL_H));
			a4VerticalY = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A4_VERTICAL_W));

			a4HorizontalW = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_H));
			a4HorizontalY = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_W));

			a3VerticalH = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A3_VERTICAL_H));
			a3VerticalY = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A3_VERTICAL_W));

			a3HorizontalW = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_H));
			a3HorizontalY = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_W));

			defectoH = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_DEFECTO_H));
			defectoW = Integer.parseInt(config.getProperty(Propiedades.PROP_SETUP_DEFECTO_W));

		}

		textFirmaposicionH.setText(String.valueOf(a4VerticalH));
		textFirmaposicionW.setText(String.valueOf(a4VerticalY));

	}

	@Override
	public Properties getConfiguration() {

		final Properties config = new Properties();

		config.setProperty(Propiedades.PROP_SETUP_LINEA1, textLinea1.getText().toString());
		config.setProperty(Propiedades.PROP_SETUP_LINEA2, textLinea2.getText().toString());
		config.setProperty(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN, String.valueOf(checkBoxActivarPlugin.isSelected()));
		config.setProperty(Propiedades.PROP_SETUP_COLOR_RED, String.valueOf(colorFirma.getRed()));
		config.setProperty(Propiedades.PROP_SETUP_COLOR_GREEN, String.valueOf(colorFirma.getGreen()));
		config.setProperty(Propiedades.PROP_SETUP_COLOR_BLUE, String.valueOf(colorFirma.getBlue()));
		config.setProperty(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA, textGrosorLinea.getText().toString());
		config.setProperty(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE, textTamanoFuente.getText().toString());
		config.setProperty(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA,
				String.valueOf(checkBoxRectanguloMostrarLinea.isSelected()));
		config.setProperty(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA,
				String.valueOf(checkSoloPrimeraPagina.isSelected()));

		config.setProperty(Propiedades.PROP_SETUP_A4_VERTICAL_H, String.valueOf(a4VerticalH));
		config.setProperty(Propiedades.PROP_SETUP_A4_VERTICAL_W, String.valueOf(a4VerticalY));

		config.setProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_H, String.valueOf(a4HorizontalW));
		config.setProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_W, String.valueOf(a4HorizontalY));

		config.setProperty(Propiedades.PROP_SETUP_A3_VERTICAL_H, String.valueOf(a3VerticalH));
		config.setProperty(Propiedades.PROP_SETUP_A3_VERTICAL_W, String.valueOf(a3VerticalY));

		config.setProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_H, String.valueOf(a3HorizontalW));
		config.setProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_W, String.valueOf(a3HorizontalY));

		config.setProperty(Propiedades.PROP_SETUP_DEFECTO_H, String.valueOf(defectoH));
		config.setProperty(Propiedades.PROP_SETUP_DEFECTO_W, String.valueOf(defectoW));

		return config;

	}

}