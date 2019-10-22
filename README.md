# afirma-plugins

AutoFirma es una herramienta de escritorio con interfaz gráfica que permite la ejecución de operaciones de firma de ficheros locales en entornos de escritorio (Windows, Linux y OS X ) y mediante un sistema de plugins permite añadir nuevas funcionalidades.

## Plugins disponibles

| Plugin | Versión  | Descripción |
| --- | --- | --- |
| [**`InternallyDetachedView`**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/Plugin_InternallyDetachedView_v00r01.pdf?raw=yes)| [**00.01**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/afirma-ui-simpleafirma-plugin-internallydetachedview-1.6.5.jar?raw=yes)| Visualizar documentos originales almacenados en archivos de firma XML |

### 1. Plugin InternallyDetachedView

[**Manual InternallyDetachedView**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/Plugin_InternallyDetachedView_v00r01.pdf?raw=yes)
[**InternallyDetachedView Última versión**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/afirma-ui-simpleafirma-plugin-internallydetachedview-1.6.5.jar?raw=yes)

Una de las funcionalidades de AutoFirma es la verificación de documentos firmados electrónicamente permitiendo visualizar la firma y el contenido del documento firmado, pero existe una modalidad de firma  ‘Internally detached’ que genera en un único fichero XML el documento original y las firmas

```
  <documento>
     <documentoOriginal>
          …
          …
     </documentoOriginal>
     <firmas>
          …
          …
     </firmas>
  </documento>
```

y el `Visor de Firmas` de `AutoFirma` no dispone de herramientas para visualizar el contenido del documento firmado almacenado en el archivo XML.

Mediante el plugin InternallyDetachedView se añade una nueva funcionalidad en la pantalla del Visor de Firmas para visualizar el documento firmado almacenado en el archivo de firma XML. 



## Créditos

- [**Cliente @Firma - Portal Administración Electrónica**](https://administracionelectronica.gob.es/ctt/clienteafirma)
- [**Cliente @Firma - GitHub**](https://github.com/ctt-gob-es/clienteafirma)	

