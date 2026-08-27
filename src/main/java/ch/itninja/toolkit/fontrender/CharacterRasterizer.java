package ch.itninja.toolkit.fontrender;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/** Measures one font and performs the actual character-to-pixel conversion. */
final class CharacterRasterizer {

    private final Font font;
    private final int characterWidth;
    private final int characterHeight;

    /** Measures the dimensions of one complete character cell once. */
    CharacterRasterizer(Font font) {
        this.font = font;

        // A tiny helper image is sufficient to obtain the font metrics.
        BufferedImage helper = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = helper.createGraphics();
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();
        characterWidth = metrics.charWidth('M');
        characterHeight = metrics.getHeight();
        graphics.dispose();
    }

    int getCharacterWidth() {
        return characterWidth;
    }

    int getCharacterHeight() {
        return characterHeight;
    }

    /** Renders all candidates that fit into exactly one character cell. */
    int[][] render(char[] candidates) {
        BufferedImage helper = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = helper.createGraphics();
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();

        int[][] result = new int[candidates.length][];
        int count = 0;
        for (int i = 0; i < candidates.length; i++) {
            char character = candidates[i];

            // Fallback glyphs or proportional characters would break the grid.
            if (font.canDisplay(character) && metrics.charWidth(character) == characterWidth) {
                result[count] = renderCharacter(character);
                count++;
            }
        }
        graphics.dispose();

        // Remove unused entries left by skipped characters.
        return Arrays.copyOf(result, count);
    }

    /** Renders white ink on a black cell and flattens the pixels row by row. */
    private int[] renderCharacter(char character) {
        BufferedImage image = new BufferedImage(
                characterWidth, characterHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, characterWidth, characterHeight);
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);

        // The ascent places the baseline at the normal position inside the cell.
        graphics.drawString(String.valueOf(character), 0, graphics.getFontMetrics().getAscent());
        graphics.dispose();

        int[] matrix = new int[1 + characterWidth * characterHeight];
        matrix[0] = character;
        int position = 1;
        for (int y = 0; y < characterHeight; y++) {
            for (int x = 0; x < characterWidth; x++) {
                matrix[position] = toGray(image.getRGB(x, y));
                position++;
            }
        }
        return matrix;
    }

    /** Converts RGB to perceived brightness using integer arithmetic. */
    private int toGray(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return (299 * red + 587 * green + 114 * blue) / 1000;
    }
}
