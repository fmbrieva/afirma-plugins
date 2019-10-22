# afirma-plugins

AutoFirma es una herramienta de escritorio con interfaz gráfica que permite la ejecución de operaciones de firma de ficheros locales en entornos de escritorio (Windows, Linux y OS X ) y mediante un sistema de plugins permite añadir nuevas funcionalidades.

## Plugins para AutoFirma

| Plugin | Versión  | Descripción |
| --- | --- | --- |
| [**`InternallyDetachedView`**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/Plugin_InternallyDetachedView_v00r01.pdf?raw=yes)| [**00.01**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.01/afirma-ui-simpleafirma-plugin-internallydetachedview-1.6.5.jar?raw=yes)| Visualizar documentos originales almacenados en archivos de firma XML |

### 1. Plugin InternallyDetachedView

`Plugin que permite visualizar el documento almacenado internamente en un archivo de firma XML`

Existe una modalidad de firma `Internally detached` en la cual se almacena en un único fichero XML el documento original y las firmas

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

y el `Visor de Firmas` de `AutoFirma` no dispone de herramientas para visualizar el contenido del documento almacenado en el archivo XML.

Mediante el plugin InternallyDetachedView se añade una nueva funcionalidad en la pantalla del `Visor de Firmas` para visualizar el documento firmado almacenado en el archivo de firma XML. 

![](imagenes/InternallyDetachedView_Boton.png)

![Plugin](imagenes/InternallyDetachedView_Boton.png){: width=50% }

<img src="imagenes/InternallyDetachedView_Boton.png" alt="Image" width="50%" height="50%" style="display: block; margin: 0 auto" />

## Instalación de plugins

`Los plugins están empaquetados en un único JAR`

Para añadir un plugin en AutoFirma debe acceder al módulo `Herramientas&Gestionar plugins`


![](imagenes/AutoFirma_Herramientas_Plugins.png | width=50)

y con desde el botón `Agregar` debe seleccionar el archivo JAR 

![](imagenes/AutoFirma_Agregar_Plugins.pn g| width=50)

## Créditos

- [**Cliente @Firma - Portal Administración Electrónica**](https://administracionelectronica.gob.es/ctt/clienteafirma)
- [**Cliente @Firma - GitHub**](https://github.com/ctt-gob-es/clienteafirma)	
