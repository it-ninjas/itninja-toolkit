# itninja-toolkit

Kleine Hilfsbibliotheken für Aufgaben und Prüfungen von [IT-Ninjas](https://www.it-ninjas.ch/).

## Font-Rendering

`FontRenderer` wandelt Zeichen eines Monospace-Fonts in Graustufenmatrizen um. Die API ist vollständig
statisch und verlangt keine objektorientierten Vorkenntnisse:

```java
import ch.itninja.toolkit.fontrender.FontRenderer;

int[][] matrices = FontRenderer.renderCharacters(" .:-=+*#%@");
int[][] blockElements = FontRenderer.renderCodeRange(0x2580, 0x259F);

int characterWidth = FontRenderer.getCharacterWidth();
int characterHeight = FontRenderer.getCharacterHeight();
```

Jede Zeile des Resultats hat dieselbe Struktur:

```text
[Zeichencode, Pixel 0, Pixel 1, ...]
```

Die Pixelwerte liegen zwischen `0` (schwarz) und `255` (weiss) und sind zeilenweise gespeichert.
Zeichen, die der gewählte Font nicht exakt in einer Zeichenbreite darstellen kann, werden ausgelassen.

## Maven mit JitPack

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.it-ninjas</groupId>
  <artifactId>itninja-toolkit</artifactId>
  <version>v1.0.0</version>
</dependency>
```

Die Bibliothek benötigt Java 21 und keine Laufzeitabhängigkeiten.

## Build

```shell
mvn clean test
```
