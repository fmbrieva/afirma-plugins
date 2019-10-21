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
import java.awt.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;

import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.standalone.plugins.OutputData;
import es.gob.afirma.standalone.plugins.SignatureProcessAction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import es.gob.afirma.plugin.internallydetachedview.Messages;
import es.gob.afirma.plugin.internallydetachedview.Propiedades;

/**
 * Acción de validación para visualizar el documento original almacenado en el
 * fichero de firma XML
 * 
 * @author Felipe Muñoz Brieva
 * 
 */
public class InternallyDetachedViewAction extends SignatureProcessAction {

	static final Logger LOGGER = Logger.getLogger(InternallyDetachedViewAction.class.getName());

	JDialog waitDialog = null;

	String extensionMimeType = Propiedades.getString(Propiedades.PROP_EXTENSION_PDF);

	@Override
	public void processSignatures(OutputData[] data, X509Certificate signingCert, Window parent) {

		try {

			// Obtener ruta al fichero de firma con el documento original embebido
			File inputFile = new File(data[0].getDataFile().toString());

			// Comprobar que el archivo de firma es XML
			Path path = inputFile.toPath();
			String mimeType = Files.probeContentType(path);

			// Windows: text/xml
			// Linux: application/xml
			// Multiplataforma comprobar endsWith /xml
			if (!mimeType.endsWith(Propiedades.getString(Propiedades.PROP_MIMETYPE_XML))) {

				JOptionPane.showMessageDialog(null, Messages.getString("InternallyDetachedViewError.1"),
						Messages.getString("InternallyDetachedViewError.0"), JOptionPane.OK_OPTION);
				return;
			}

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
				JOptionPane.showMessageDialog(null, Messages.getString("InternallyDetachedViewError.2"),
						Messages.getString("InternallyDetachedViewError.0"), JOptionPane.OK_OPTION);
				return;
			}

			// Decodificar el contenido de la etiqueta con el documento original byte[]
			byte[] decodedBytes = Base64.decode(originalEmbebido);

			// Crear fichero temporal para visualizar contenido
			File tempFile = File.createTempFile(Propiedades.getString(Propiedades.PROP_PREFIJO_ORIGINAL),
					extensionMimeType);

			OutputStream opStream = null;
			opStream = new FileOutputStream(tempFile);
			opStream.write(decodedBytes);
			opStream.flush();
			opStream.close();

			// Abrir el documento con la aplicación por defecto
			Desktop.getDesktop().open(tempFile);

			// Borrar el fichero temporal tempFile.deleteOnExit();

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
						// Consultar si existe el atributo MimeType para asignar la extensión correcta del fichero
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
