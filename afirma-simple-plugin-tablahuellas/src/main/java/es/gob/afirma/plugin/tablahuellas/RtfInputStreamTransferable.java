package es.gob.afirma.plugin.tablahuellas;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RtfInputStreamTransferable implements Transferable {
    private final DataFlavor _rtfDataFlavor;
    private final String _rtfText;

    public RtfInputStreamTransferable(String rtfText) throws ClassNotFoundException {
        _rtfText = rtfText;
        _rtfDataFlavor = new DataFlavor("text/rtf");
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{_rtfDataFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return "text/rtf".equals(flavor.getMimeType());

    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        InputStream stringStream = new ByteArrayInputStream(_rtfText.getBytes("utf-8"));
        return stringStream;
    }
}