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
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.aowagie.text.DocumentException;
import com.aowagie.text.Element;
import com.aowagie.text.Font;
import com.aowagie.text.FontFactory;
import com.aowagie.text.Phrase;
import com.aowagie.text.Rectangle;
import com.aowagie.text.pdf.AcroFields;
import com.aowagie.text.pdf.BaseFont;
import com.aowagie.text.pdf.ColumnText;
import com.aowagie.text.pdf.PdfContentByte;
import com.aowagie.text.pdf.PdfReader;
import com.aowagie.text.pdf.PdfStamper;

import es.gob.afirma.standalone.plugins.AfirmaPlugin;
import es.gob.afirma.standalone.plugins.PluginControlledException;
import es.gob.afirma.standalone.plugins.PluginException;
import es.gob.afirma.standalone.plugins.PluginPreferences;
import es.gob.afirma.standalone.plugins.PluginsManager;
import es.gob.afirma.standalone.plugins.PluginsPreferences;
import es.gob.afirma.standalone.ui.plugins.PluginConfigurationDialog;

/**
 * Incluir marca de firma en el preprocesado de firma
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class MarcarPrimeraFirmaPlugin extends AfirmaPlugin {

	public byte[] preSignProcess(byte[] data, String format) throws PluginControlledException {

		try {

			Preferences preferences = Preferences.userNodeForPackage(getClass());

			if (preferences.keys().length == 0) {

				Properties setupProperties = new Properties();

				setupProperties.setProperty(Propiedades.PROP_SETUP_LINEA1,
						Propiedades.getString(Propiedades.PROP_SETUP_LINEA1));
				setupProperties.setProperty(Propiedades.PROP_SETUP_LINEA2,
						Propiedades.getString(Propiedades.PROP_SETUP_LINEA2));
				setupProperties.setProperty(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN,
						Propiedades.getString(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN));
				setupProperties.setProperty(Propiedades.PROP_SETUP_COLOR_RED,
						Propiedades.getString(Propiedades.PROP_SETUP_COLOR_RED));
				setupProperties.setProperty(Propiedades.PROP_SETUP_COLOR_GREEN,
						Propiedades.getString(Propiedades.PROP_SETUP_COLOR_GREEN));
				setupProperties.setProperty(Propiedades.PROP_SETUP_COLOR_BLUE,
						Propiedades.getString(Propiedades.PROP_SETUP_COLOR_BLUE));
				setupProperties.setProperty(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA,
						Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA));
				setupProperties.setProperty(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE,
						Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE));
				setupProperties.setProperty(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA,
						Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA));
				setupProperties.setProperty(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA,
						Propiedades.getString(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A4_VERTICAL_H,
						Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_H));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A4_VERTICAL_W,
						Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_W));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_H,
						Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_H));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A4_HORIZONTAL_W,
						Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_W));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A3_VERTICAL_H,
						Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_H));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A3_VERTICAL_W,
						Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_W));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_H,
						Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_H));
				setupProperties.setProperty(Propiedades.PROP_SETUP_A3_HORIZONTAL_W,
						Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_W));
				setupProperties.setProperty(Propiedades.PROP_SETUP_DEFECTO_H,
						Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_H));
				setupProperties.setProperty(Propiedades.PROP_SETUP_DEFECTO_W,
						Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_W));

				PluginPreferences.getInstance(this).saveConfig(setupProperties);
				
			}

			if (Boolean.parseBoolean(getConfig().getProperty(Propiedades.PROP_SETUP_ACTIVAR_PLUGIN).toString())) {
				return imprimirMarcaAgua(data);
			}

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null,
					Messages.getString("PluginMensaje.0") + Messages.getString("PluginMensaje.1")
							+ Messages.getString("PluginMensaje.2"),
					Messages.getString("PluginMensaje.3"), JOptionPane.INFORMATION_MESSAGE);

			System.exit(0);

		}

		return data;

	}

	public void install() throws PluginControlledException {

	}

	public void uninstall() throws PluginControlledException {

	}

	public byte[] imprimirMarcaAgua(byte[] data) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			PdfReader reader = new PdfReader(data);
			PdfStamper stamper = new PdfStamper(reader, baos);

			AcroFields acroFields = reader.getAcroFields();

			List<String> signatureNames = acroFields.getSignatureNames();

			// Solamente se imprime la marca de agua si el documento no contiene firmas
			if (!signatureNames.isEmpty()) {

				return data;
			}

			Date hoy = new Date();

			int rectanguloAnchura = 0;
			int rectanguloAltura = 0;
			int rectanguloMargen = 10;
			int fuenteAltura = 0;

			int rectanguloTamanoFuente = Integer.parseInt(getConfig()
					.getOrDefault(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE,
							Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_TAMANO_FUENTE).toString())
					.toString());

			Color colorFirma = new Color(
					Integer.parseInt(
							getConfig()
									.getOrDefault(Propiedades.PROP_SETUP_COLOR_RED,
											Propiedades.getString(Propiedades.PROP_SETUP_COLOR_RED).toString())
									.toString()),
					Integer.parseInt(
							getConfig()
									.getOrDefault(Propiedades.PROP_SETUP_COLOR_GREEN,
											Propiedades.getString(Propiedades.PROP_SETUP_COLOR_GREEN).toString())
									.toString()),
					Integer.parseInt(
							getConfig()
									.getOrDefault(Propiedades.PROP_SETUP_COLOR_BLUE,
											Propiedades.getString(Propiedades.PROP_SETUP_COLOR_BLUE).toString())
									.toString()));

			Font font = FontFactory.getFont(FontFactory.HELVETICA, rectanguloTamanoFuente, Font.NORMAL);
			font.setColor(colorFirma);

			Phrase p1 = new Phrase(getConfig()
					.getOrDefault(Propiedades.PROP_SETUP_LINEA1,
							Propiedades.getString(Propiedades.PROP_SETUP_LINEA1).toString())
					.toString().replace(Propiedades.getString(Propiedades.PROP_CAMPO_FECHA),
							new SimpleDateFormat("dd/MM/yyyy").format(hoy)),
					font);

			Phrase p2 = new Phrase(getConfig()
					.getOrDefault(Propiedades.PROP_SETUP_LINEA2,
							Propiedades.getString(Propiedades.PROP_SETUP_LINEA2).toString())
					.toString().replace(Propiedades.getString(Propiedades.PROP_CAMPO_FECHA),
							new SimpleDateFormat("dd/MM/yyyy").format(hoy)),
					font);

			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

			// No se tiene en cuenta "descent"
			fuenteAltura = Math.round(bf.getAscentPoint(p1.getContent(), rectanguloTamanoFuente));

			if (Math.round(bf.getWidth(p1.getContent())) > Math.round(bf.getWidth(p2.getContent()))) {
				rectanguloAnchura = Math.round(bf.getWidth(p1.getContent()) * 0.001f * rectanguloTamanoFuente);
			} else {
				rectanguloAnchura = Math.round(bf.getWidth(p2.getContent()) * 0.001f * rectanguloTamanoFuente);
			}

			// Calcular la altura del rectangulo en función de las lineas de firma
			if (p2.getContent().trim().length() > 0) {
				rectanguloAltura = (int) (Math.round(fuenteAltura * 2.5) + (rectanguloMargen * 2));
			} else {
				rectanguloAltura = (int) (fuenteAltura + (rectanguloMargen * 2));

			}

			rectanguloAnchura = rectanguloAnchura + (rectanguloMargen * 2);

			PdfContentByte over;

			int n = reader.getNumberOfPages();

			Rectangle formatoPagina;

			ArrayList<Integer> posicionRectangulo;

			for (int i = 1; i <= n; i++) {

				if (Boolean.parseBoolean(getConfig().getOrDefault(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA,
						Propiedades.getString(Propiedades.PROP_SETUP_SOLO_PRIMERA_PAGINA)).toString()) && i > 1) {
					break;
				}

				formatoPagina = reader.getPageSizeWithRotation(i);

				posicionRectangulo = obtenerFormatoPagina(formatoPagina);

				int posicionH = posicionRectangulo.get(0);
				int posicionW = posicionRectangulo.get(1);

				over = stamper.getOverContent(i);
				over.saveState();

				if (Boolean
						.parseBoolean(getConfig()
								.getOrDefault(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA,
										Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_MOSTRAR_LINEA))
								.toString())) {

					over.setColorStroke(colorFirma);

					over.setLineWidth(Float.parseFloat(getConfig()
							.getOrDefault(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA,
									Propiedades.getString(Propiedades.PROP_SETUP_RECTANGULO_GROSOR_LINEA).toString())
							.toString()));

					over.rectangle(posicionH, posicionW, rectanguloAltura, rectanguloAnchura);
					over.stroke();
				}

				// Linea 1
				ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p1, (posicionH + rectanguloMargen + fuenteAltura),
						(posicionW + rectanguloMargen), 90);
				// Linea 2 (separacion entre lineas: tamaño fuente / 2
				ColumnText.showTextAligned(over, Element.ALIGN_LEFT, p2,
						(posicionH + rectanguloMargen + Math.round(fuenteAltura * 2.5)), (posicionW + rectanguloMargen),
						90);
				over.restoreState();

			}

			stamper.close();
			reader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "FileNotFoundException MarcarPrimeraFirmaPlugin" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "IOException MarcarPrimeraFirmaPlugin" + e.getMessage());
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "DocumentException MarcarPrimeraFirmaPlugin" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showConfirmDialog(null, "Exception MarcarPrimeraFirmaPlugin" + e.getMessage());
			e.printStackTrace();
		}

		return baos.toByteArray();
	}

	ArrayList<Integer> obtenerFormatoPagina(Rectangle formatoPagina) {

		int posicionFormato;

		// No se compara el valor por defecto porque esta inicializado con ???
		for (posicionFormato = 0; posicionFormato < (Propiedades.HOJAS_FORMATO.length - 1); posicionFormato++) {

			if ((Math.round(formatoPagina.getWidth()) == Integer.parseInt(Propiedades.HOJAS_FORMATO[posicionFormato][1])
					&& Math.round(formatoPagina.getHeight()) == Integer
							.parseInt(Propiedades.HOJAS_FORMATO[posicionFormato][2]))) {
				;
				break;

			}
		}

		int posicionH = 0;
		int posicionW = 0;

		switch (Propiedades.HOJAS_FORMATO[posicionFormato][0]) {

		case Propiedades.A4_VERTICAL:

			posicionH = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_A4_VERTICAL_H,
					Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_H).toString()).toString());
			posicionW = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_A4_VERTICAL_W,
					Propiedades.getString(Propiedades.PROP_SETUP_A4_VERTICAL_W).toString()).toString());

			break;

		case Propiedades.A4_HORIZONTAL:

			posicionH = Integer
					.parseInt(getConfig()
							.getOrDefault(Propiedades.PROP_SETUP_A4_HORIZONTAL_H,
									Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_H).toString())
							.toString());
			posicionW = Integer
					.parseInt(getConfig()
							.getOrDefault(Propiedades.PROP_SETUP_A4_HORIZONTAL_W,
									Propiedades.getString(Propiedades.PROP_SETUP_A4_HORIZONTAL_W).toString())
							.toString());

			break;

		case Propiedades.A3_HORIZONTAL:

			posicionH = Integer
					.parseInt(getConfig()
							.getOrDefault(Propiedades.PROP_SETUP_A3_HORIZONTAL_H,
									Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_H).toString())
							.toString());
			posicionW = Integer
					.parseInt(getConfig()
							.getOrDefault(Propiedades.PROP_SETUP_A3_HORIZONTAL_W,
									Propiedades.getString(Propiedades.PROP_SETUP_A3_HORIZONTAL_W).toString())
							.toString());

			break;

		case Propiedades.A3_VERTICAL:

			posicionH = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_A3_VERTICAL_H,
					Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_H).toString()).toString());
			posicionW = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_A3_VERTICAL_W,
					Propiedades.getString(Propiedades.PROP_SETUP_A3_VERTICAL_W).toString()).toString());

			break;

		default:

			posicionH = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_DEFECTO_H,
					Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_H).toString()).toString());
			posicionW = Integer.parseInt(getConfig().getOrDefault(Propiedades.PROP_SETUP_DEFECTO_W,
					Propiedades.getString(Propiedades.PROP_SETUP_DEFECTO_W).toString()).toString());

			break;
		}

		return new ArrayList<>(Arrays.asList(posicionH, posicionW));

	}

}
