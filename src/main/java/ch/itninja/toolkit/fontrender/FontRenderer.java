package ch.itninja.toolkit.fontrender;

import java.awt.Font;

/**
 * Static facade for rendering monospaced characters as grayscale matrices.
 *
 * The class deliberately exposes only arrays, integers and static methods so
 * learners do not need to understand AWT or object-oriented programming.
 */
public final class FontRenderer {

    /** Font size used for every character matrix. */
    private static final int FONT_SIZE = 14;

    /** Fonts are tried from left to right; the logical font is always available. */
    private static final String[] PREFERRED_FONTS = {
            "Cascadia Mono", "Consolas", Font.MONOSPACED
    };

    /** Internal renderer shared by all static API calls. */
    private static final CharacterRasterizer RASTERIZER = new CharacterRasterizer(
            FontSelector.select(PREFERRED_FONTS, FONT_SIZE));

    private FontRenderer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Renders the supplied characters.
     *
     * Each result row contains the character code at index 0, followed by the
     * grayscale pixels from 0 to 255 in row-major order. Characters that the
     * selected font cannot render in exactly one cell are skipped.
     *
     * @param characters characters to render
     * @return one flattened grayscale matrix per usable character
     */
    public static int[][] renderCharacters(String characters) {
        if (characters == null) {
            throw new IllegalArgumentException("Characters must not be null");
        }
        return RASTERIZER.render(characters.toCharArray());
    }

    /**
     * Renders all characters in an inclusive UTF-16 code range.
     *
     * @param firstCode first character code, inclusive
     * @param lastCode last character code, inclusive
     * @return one flattened grayscale matrix per usable character
     */
    public static int[][] renderCodeRange(int firstCode, int lastCode) {
        validateRange(firstCode, lastCode);

        char[] characters = new char[lastCode - firstCode + 1];
        for (int i = 0; i < characters.length; i++) {
            characters[i] = (char) (firstCode + i);
        }
        return RASTERIZER.render(characters);
    }

    /** @return width of one rendered character cell in pixels */
    public static int getCharacterWidth() {
        return RASTERIZER.getCharacterWidth();
    }

    /** @return height of one rendered character cell in pixels */
    public static int getCharacterHeight() {
        return RASTERIZER.getCharacterHeight();
    }

    private static void validateRange(int firstCode, int lastCode) {
        if (firstCode < Character.MIN_VALUE || lastCode > Character.MAX_VALUE) {
            throw new IllegalArgumentException("Character codes must be between 0 and 65535");
        }
        if (firstCode > lastCode) {
            throw new IllegalArgumentException("First character code must not be greater than last character code");
        }
    }
}
