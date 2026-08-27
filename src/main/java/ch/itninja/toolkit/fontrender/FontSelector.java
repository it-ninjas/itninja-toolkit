package ch.itninja.toolkit.fontrender;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/** Selects a predictable monospaced font without exposing AWT in the public API. */
final class FontSelector {

    private FontSelector() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns the first installed preferred font. Java's logical Monospaced
     * font acts as the final fallback and is available on every JDK.
     */
    static Font select(String[] preferredFonts, int fontSize) {
        String[] installedFonts = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        for (int i = 0; i < preferredFonts.length; i++) {
            if (isInstalled(preferredFonts[i], installedFonts)) {
                return new Font(preferredFonts[i], Font.PLAIN, fontSize);
            }
        }
        return new Font(Font.MONOSPACED, Font.PLAIN, fontSize);
    }

    private static boolean isInstalled(String fontName, String[] installedFonts) {
        if (fontName.equals(Font.MONOSPACED)) {
            return true;
        }
        for (int i = 0; i < installedFonts.length; i++) {
            if (installedFonts[i].equals(fontName)) {
                return true;
            }
        }
        return false;
    }
}
