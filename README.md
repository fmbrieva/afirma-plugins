# afirma-plugins

AutoFirma es una herramienta de escritorio con interfaz gráfica que permite la ejecución de operaciones de firma de ficheros locales en entornos de escritorio (Windows, Linux y OS X ) y `mediante un sistema de plugins permite añadir nuevas funcionalidades`.

## Plugins para AutoFirma

| Plugin | Versión  | Descripción |
| --- | --- | --- |
| [**`InternallyDetachedView`**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.02/Plugin_InternallyDetachedView_v00r02.pdf?raw=yes)| [**00.01**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedView/00.02/afirma-ui-simpleafirma-plugin-internallydetachedview-1.6.5_v00r02.jar?raw=yes)| Visualizar documentos originales almacenados en archivos de firma XML |
| [**`InternallyDetachedFolder`**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedFolder/00.01/Plugin_InternallyDetachedFolder_v00r01.pdf?raw=yes)| [**00.01**](https://github.com/digitaliza-aapp/afirma-plugins/blob/master/plugins/InternallyDetachedFolder/00.01/afirma-ui-simpleafirma-plugin-internallydetachedview-1.6.5_v00r01.jar?raw=yes)| Visualizar documentos originales almacenados en archivos de firma XML |

### 1. Plugin InternallyDetachedView

`Plugin que permite visualizar el documento original firmado almacenado internamente en un archivo de firma XML`

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

y el `Visor de Firmas` de `AutoFirma` no dispone de herramientas para visualizar el documento original.

Mediante el plugin `InternallyDetachedView` se añade una nueva funcionalidad en la pantalla del `Visor de Firmas` para visualizar el documento original firmado almacenado en el archivo de firma XML. 

<p align="center">
    <img src="imagenes/InternallyDetachedView_Boton.png" alt="Plugin" width="60%" />
</p>

## Instalación de plugins

`Los plugins están empaquetados en un único JAR`

Para añadir un plugin en AutoFirma debe acceder al módulo `Herramientas&Gestionar plugins`

<p align="center">
    <img src="imagenes/AutoFirma_Herramientas_Plugins.png" alt="Plugin" width="30%" />
</p>

y con desde el botón `Agregar` debe seleccionar el archivo JAR 

<p align="center">
    <img src="imagenes/AutoFirma_Agregar_Plugins.png" alt="Plugin" width="60%" />
</p>

## Contacto

Para hacer sugerencias ó reportar incidencias puede enviar un correo electrónico a digitaliza.aapp@gmail.com 

## Créditos

- [**Cliente @Firma - Portal Administración Electrónica**](https://administracionelectronica.gob.es/ctt/clienteafirma)
- [**Cliente @Firma - GitHub**](https://github.com/ctt-gob-es/clienteafirma)	
