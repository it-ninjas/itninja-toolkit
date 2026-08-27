package ch.itninja.toolkit.fontrender;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontRendererTest {

    @Test
    void rendersCharactersAsCodeAndGrayscalePixels() {
        int[][] matrices = FontRenderer.renderCharacters("AM");

        assertEquals(2, matrices.length);
        assertEquals('A', matrices[0][0]);
        assertEquals('M', matrices[1][0]);
        assertEquals(
                1 + FontRenderer.getCharacterWidth() * FontRenderer.getCharacterHeight(),
                matrices[0].length);
        assertPixelsAreGrayscale(matrices);
    }

    @Test
    void rendersAnInclusiveCodeRange() {
        int[][] matrices = FontRenderer.renderCodeRange('A', 'C');

        assertEquals(3, matrices.length);
        assertEquals('A', matrices[0][0]);
        assertEquals('B', matrices[1][0]);
        assertEquals('C', matrices[2][0]);
    }

    @Test
    void exposesPositiveCharacterDimensions() {
        assertTrue(FontRenderer.getCharacterWidth() > 0);
        assertTrue(FontRenderer.getCharacterHeight() > 0);
    }

    @Test
    void rejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> FontRenderer.renderCharacters(null));
        assertThrows(IllegalArgumentException.class, () -> FontRenderer.renderCodeRange(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> FontRenderer.renderCodeRange(20, 10));
        assertThrows(IllegalArgumentException.class, () -> FontRenderer.renderCodeRange(0, 65536));
    }

    private void assertPixelsAreGrayscale(int[][] matrices) {
        for (int i = 0; i < matrices.length; i++) {
            for (int p = 1; p < matrices[i].length; p++) {
                assertTrue(matrices[i][p] >= 0 && matrices[i][p] <= 255);
            }
        }
    }
}
