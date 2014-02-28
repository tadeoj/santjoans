santjoans
=========

==Proyecto==
En este repositorio encontrareis el código fuente del proyecto http://www.santjoans.es 

==Información general==
Si solo os interesan las imágenes para ilustrar cualquier tipo de obra podéis descargarlas directamente en este [http://www.santjoans.es/proyecto.html#imagenes enlace].

Si tenéis curiosidad en los aspectos mas generales de porque se realizo este proyecto, como se ejecuto y otros datos generales, podéis seguir este [http://www.santjoans.es/proyecto.html enlace].

Las distintas entradas del blog con todos las noticias relacionadas con el proyecto, las encontrareis siguiendo siguiente [http://jtpadilla.blogspot.com/search/label/santjoans enlace].

==Información para desarrolladores==
Esta aplicación se termino de desarrollar con la versión 2.1.1 de GWT y por tanto utiliza las posibilidades que ofrecía dicha versión.

Dependencias:
  # En aquel momento no existía soporte para Canvas en el SDK por lo que se utilizaron librerías [http://code.google.com/p/google-web-toolkit-incubator/ externas] que han dado muy buen resultado (creo que era la incubadora de las que actualmente se integran en el SDK).
  # Para la carga de imagenes asincrona utilice [http://code.google.com/p/gwt-image-loader/ gwt-image-loader].
He verificado que se puede compilar y funciona correctamente con la ultima versión (version 2.4 en el momento de la plublicación del codigo).

Cuando inicie el desarrollo la decisión de utilizar HTML5 en lugar de Flash no fue facil ya que algunos navegaores todavia no soportaban el Canvas y parecía que me metía en un gran lió, si embargo parece que nuestro amigo Jobs tenia razon.

Naturalmente hay muchas cosas que se han quedado en el tintero, pero desgraciadamente ahora no tengo demesiado tiempo. 

Algunas de estas cosas son:
  * Adaptarlo a las versiones recientes de GWT
  * Adaptarlo para su correcto funcionamiento en tables de Android y IPad
  * Mejorar el aspecto general (claramente no se mucho de diseñar interfaces)

== Ejecutandolo desde Eclipse ==
Para ejecutar esta aplicación desde Eclipse hay que tener en cuenta un par de cosas (para usuarios con experiencia en GWT):
  * Hay que seleccionar el fichero Santjoans.html como punto de entrada de la lista que se nos ofrecera.
  * La ejecución en el modo Hosted resulta muy pesada y hay que reservarla para depuración. Primero compilar y despues en lugar de abrir la URL que nos proporciona (algo asi como http://127.0.0.1:8888/Santjoans.html?gwt.codesvr=127.0.0.1:9997) abrir (http://127.0.0.1:8888/Santjoans.html) que ejecutara la versión estática compilada.

== Una cosa mas ==
La verdad es que mientras trabajaba en este proyecto no pensé en que algún día lo publicaría en código abierto así que los comentarios que encontrareis están pensados para uso propio que para terceros (espero que no haya escondida por ahí dentro ninguna sorpresa).

También confieso que es la primera aplicación que desarrollo que se ejecuta en un navegador y he afrontado este proyecto mas como un programador convencional que como un programador Web (creo que se nota en el codigo) lo cual ha sido posible gracias a este fantástico producto llamado [http://code.google.com/intl/en/webtoolkit/overview.html GWT]. 

Ahora ya no veo el navegador como un renderizador de paginas si no un potente runtime para ejecutar complejas aplicaciones.
