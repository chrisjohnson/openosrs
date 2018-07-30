/*
 * Copyright (c) 2018, Jordan Atwood <jordan.atwood423@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.util;

import java.awt.Color;
import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.ArrayUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ImageUtilTest
{
	private static final Color BLACK_HALF_TRANSPARENT = new Color(0, 0, 0, 128);
	private static final Color BLACK_TRANSPARENT = new Color(0, true);
	private static final int CORNER_SIZE = 2;
	private static final int CENTERED_SIZE = 3;

	private static final BufferedImage BLACK_PIXEL_TOP_LEFT;
	private static final BufferedImage BLACK_PIXEL_TOP_RIGHT;
	private static final BufferedImage BLACK_PIXEL_BOTTOM_LEFT;
	private static final BufferedImage BLACK_PIXEL_BOTTOM_RIGHT;

	static
	{
		BLACK_PIXEL_TOP_LEFT = new BufferedImage(CORNER_SIZE, CORNER_SIZE, BufferedImage.TYPE_INT_ARGB);
		BLACK_PIXEL_TOP_LEFT.setRGB(0, 0, BLACK.getRGB());

		BLACK_PIXEL_TOP_RIGHT = new BufferedImage(CORNER_SIZE, CORNER_SIZE, BufferedImage.TYPE_INT_ARGB);
		BLACK_PIXEL_TOP_RIGHT.setRGB(1, 0, BLACK.getRGB());

		BLACK_PIXEL_BOTTOM_LEFT = new BufferedImage(CORNER_SIZE, CORNER_SIZE, BufferedImage.TYPE_INT_ARGB);
		BLACK_PIXEL_BOTTOM_LEFT.setRGB(0, 1, BLACK.getRGB());

		BLACK_PIXEL_BOTTOM_RIGHT = new BufferedImage(CORNER_SIZE, CORNER_SIZE, BufferedImage.TYPE_INT_ARGB);
		BLACK_PIXEL_BOTTOM_RIGHT.setRGB(1, 1, BLACK.getRGB());
	}

	@Test
	public void bufferedImageFromImage()
	{
		final BufferedImage buffered = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		assertEquals(buffered, ImageUtil.bufferedImageFromImage(buffered));
	}

	@Test
	public void grayscaleOffset()
	{
		// grayscaleOffset(BufferedImage image, int offset)
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(BLACK), -255)));
		assert(bufferedImagesEqual(oneByOne(new Color(50, 50, 50)), ImageUtil.grayscaleOffset(oneByOne(BLACK), 50)));
		assert(bufferedImagesEqual(oneByOne(GRAY), ImageUtil.grayscaleOffset(oneByOne(BLACK), 128)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(GRAY), -255)));
		assert(bufferedImagesEqual(oneByOne(WHITE), ImageUtil.grayscaleOffset(oneByOne(BLACK), 255)));
		assert(bufferedImagesEqual(oneByOne(new Color(200, 200, 200)), ImageUtil.grayscaleOffset(oneByOne(WHITE), -55)));
		assert(bufferedImagesEqual(oneByOne(WHITE), ImageUtil.grayscaleOffset(oneByOne(WHITE), 55)));

		// grayscaleOffset(BufferedImage image, float percentage)
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(BLACK), 0f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(BLACK), 1f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(BLACK), 2f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(GRAY), 0f)));
		assert(bufferedImagesEqual(oneByOne(GRAY), ImageUtil.grayscaleOffset(oneByOne(GRAY), 1f)));
		assert(bufferedImagesEqual(oneByOne(WHITE), ImageUtil.grayscaleOffset(oneByOne(GRAY), 2f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.grayscaleOffset(oneByOne(WHITE), 0f)));
		assert(bufferedImagesEqual(oneByOne(GRAY), ImageUtil.grayscaleOffset(oneByOne(WHITE), 0.503f))); // grayscaleOffset does Math.floor
		assert(bufferedImagesEqual(oneByOne(WHITE), ImageUtil.grayscaleOffset(oneByOne(WHITE), 1f)));
		assert(bufferedImagesEqual(oneByOne(WHITE), ImageUtil.grayscaleOffset(oneByOne(WHITE), 2f)));
	}

	@Test
	public void alphaOffset()
	{
		// alphaOffset(BufferedImage image, int offset)
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), -255)));
		assert(bufferedImagesEqual(oneByOne(new Color(0, 0, 0, 50)), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 50)));
		assert(bufferedImagesEqual(oneByOne(BLACK_HALF_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 128)));
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_HALF_TRANSPARENT), -255)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 255)));
		assert(bufferedImagesEqual(oneByOne(new Color(0, 0, 0, 200)), ImageUtil.alphaOffset(oneByOne(BLACK), -55)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.alphaOffset(oneByOne(BLACK), 255)));

		// alphaOffset(BufferedImage image, float offset)
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 0f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 1f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_TRANSPARENT), 2f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_HALF_TRANSPARENT), 0f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_HALF_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK_HALF_TRANSPARENT), 1f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.alphaOffset(oneByOne(BLACK_HALF_TRANSPARENT), 2f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK), 0f)));
		assert(bufferedImagesEqual(oneByOne(BLACK_HALF_TRANSPARENT), ImageUtil.alphaOffset(oneByOne(BLACK), 0.503f))); // opacityOffset does Math.floor
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.alphaOffset(oneByOne(BLACK), 1f)));
		assert(bufferedImagesEqual(oneByOne(BLACK), ImageUtil.alphaOffset(oneByOne(BLACK), 2f)));
	}

	@Test
	public void grayscaleImage()
	{
		final BufferedImage[] grayscaleColors = new BufferedImage[] {
			oneByOne(WHITE),
			oneByOne(GRAY),
			oneByOne(BLACK),
			oneByOne(BLACK_HALF_TRANSPARENT),
			oneByOne(BLACK_TRANSPARENT),
		};
		final BufferedImage[] nonGrayscaleColors = new BufferedImage[] {
			oneByOne(RED),
			oneByOne(GREEN),
			oneByOne(BLUE),
		};

		for (BufferedImage image : grayscaleColors)
		{
			assert(isGrayscale(image));
		}
		for (BufferedImage image : nonGrayscaleColors)
		{
			assert(!isGrayscale(image));
		}
		for (BufferedImage image : ArrayUtils.addAll(grayscaleColors, nonGrayscaleColors))
		{
			assert(isGrayscale(ImageUtil.grayscaleImage(image)));
		}
	}

	@Test
	public void resizeImage()
	{
		// TODO: test image contents after changing size

		final BufferedImage larger = ImageUtil.resizeImage(oneByOne(BLACK), 46, 46);
		final BufferedImage smaller = ImageUtil.resizeImage(centeredPixel(WHITE), 1, 1);
		final BufferedImage stretched = ImageUtil.resizeImage(solidColor(30, 30, RED), 12, 34);

		assertEquals(46, larger.getWidth());
		assertEquals(46, larger.getHeight());
		assertEquals(1, smaller.getWidth());
		assertEquals(1, smaller.getHeight());
		assertEquals(12, stretched.getWidth());
		assertEquals(34, stretched.getHeight());

		final BufferedImage[] assertSameAfterResize = new BufferedImage[] {
			oneByOne(WHITE),
			oneByOne(GRAY),
			oneByOne(BLACK),
			oneByOne(RED),
			oneByOne(GREEN),
			oneByOne(BLUE),
			oneByOne(BLACK_HALF_TRANSPARENT),
			oneByOne(BLACK_TRANSPARENT),
			centeredPixel(WHITE),
			centeredPixel(GRAY),
			centeredPixel(BLACK),
			BLACK_PIXEL_TOP_LEFT,
			BLACK_PIXEL_TOP_RIGHT,
			BLACK_PIXEL_BOTTOM_LEFT,
			BLACK_PIXEL_BOTTOM_RIGHT,
		};
		for (BufferedImage image : assertSameAfterResize)
		{
			assert(bufferedImagesEqual(image, ImageUtil.resizeImage(image, image.getWidth(), image.getHeight())));
		}
	}

	@Test
	public void rotateImage()
	{
		// TODO: Test more than 90° rotations

		// Evenly-sized images (2x2)
		assert(bufferedImagesEqual(BLACK_PIXEL_TOP_RIGHT, ImageUtil.rotateImage(BLACK_PIXEL_TOP_LEFT, Math.PI / 2)));
		assert(bufferedImagesEqual(BLACK_PIXEL_BOTTOM_RIGHT, ImageUtil.rotateImage(BLACK_PIXEL_TOP_LEFT, Math.PI)));
		assert(bufferedImagesEqual(BLACK_PIXEL_BOTTOM_LEFT, ImageUtil.rotateImage(BLACK_PIXEL_TOP_LEFT, Math.PI * 3 / 2)));
		assert(bufferedImagesEqual(BLACK_PIXEL_TOP_LEFT, ImageUtil.rotateImage(BLACK_PIXEL_TOP_LEFT, Math.PI * 2)));

		// Unevenly-sized images (2x1); when rotated 90° become (2x2) images
		final BufferedImage twoByOneLeft = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
		twoByOneLeft.setRGB(0, 0, BLACK.getRGB());
		final BufferedImage twoByTwoRight = new BufferedImage(2, 1, BufferedImage.TYPE_INT_ARGB);
		twoByTwoRight.setRGB(1, 0, BLACK.getRGB());
		final BufferedImage oneByTwoTop = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		oneByTwoTop.setRGB(1, 0, new Color(0, 0, 0, 127).getRGB());
		final BufferedImage oneByTwoBottom = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		oneByTwoBottom.setRGB(0, 0, new Color(0, 0, 0, 127).getRGB());
		oneByTwoBottom.setRGB(0, 1, BLACK.getRGB());

		assert(bufferedImagesEqual(oneByTwoTop, ImageUtil.rotateImage(twoByOneLeft, Math.PI / 2)));
		assert(bufferedImagesEqual(twoByTwoRight, ImageUtil.rotateImage(twoByOneLeft, Math.PI)));
		assert(bufferedImagesEqual(oneByTwoBottom, ImageUtil.rotateImage(twoByOneLeft, Math.PI * 3 / 2)));
		assert(bufferedImagesEqual(twoByOneLeft, ImageUtil.rotateImage(twoByOneLeft, Math.PI * 2)));
	}

	@Test
	public void flipImage()
	{
		assert(bufferedImagesEqual(BLACK_PIXEL_TOP_LEFT, ImageUtil.flipImage(BLACK_PIXEL_TOP_LEFT, false, false)));
		assert(bufferedImagesEqual(BLACK_PIXEL_TOP_RIGHT, ImageUtil.flipImage(BLACK_PIXEL_TOP_LEFT, true, false)));
		assert(bufferedImagesEqual(BLACK_PIXEL_BOTTOM_LEFT, ImageUtil.flipImage(BLACK_PIXEL_TOP_LEFT, false, true)));
		assert(bufferedImagesEqual(BLACK_PIXEL_BOTTOM_RIGHT, ImageUtil.flipImage(BLACK_PIXEL_TOP_LEFT, true, true)));
	}

	/**
	 * Compares whether two {@link BufferedImage}s are equal in data.
	 *
	 * @param expected The first {@link BufferedImage} to be compared.
	 * @param actual   The second {@link BufferedImage} to be compared.
	 * @return         A boolean indicating whether the given {@link BufferedImage}s are of the same image data.
	 */
	private boolean bufferedImagesEqual(final @Nonnull BufferedImage expected, final @Nonnull BufferedImage actual)
	{
		if (expected.getWidth() != actual.getWidth())
		{
			return false;
		}

		if (!expected.getColorModel().equals(actual.getColorModel()))
		{
			return false;
		}

		final DataBuffer aBuffer = expected.getRaster().getDataBuffer();
		final DataBuffer bBuffer = actual.getRaster().getDataBuffer();
		final DataBufferInt aBufferInt = (DataBufferInt) aBuffer;
		final DataBufferInt bBufferInt = (DataBufferInt) bBuffer;

		if (aBufferInt.getNumBanks() != bBufferInt.getNumBanks())
		{
			return false;
		}

		for (int i = 0; i < aBufferInt.getNumBanks(); i++)
		{
			final int[] aDataBank = aBufferInt.getData(i);
			final int[] bDataBank = bBufferInt.getData(i);
			if (!Arrays.equals(aDataBank, bDataBank))
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns whether a {@link BufferedImage} contains only grayscale pixel data.
	 *
	 * @param image The image to be checked.
	 * @return      A boolean indicating whether all of the given image's pixels are grayscale.
	 */
	private boolean isGrayscale(final @Nonnull BufferedImage image)
	{
		for (int x = 0; x < image.getWidth(); x++)
		{
			for (int y = 0; y < image.getHeight(); y++)
			{
				final int color = image.getRGB(x, y);
				final int red = (color & 0xff0000) >> 16;
				final int green = (color & 0xff00) >> 8;
				final int blue = color & 0xff;
				if (red != green
					|| green != blue)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Creates a {@link BufferedImage} of a 1-by-1px image of the given color.
	 *
	 * @param color The color to use for the image's single pixel.
	 * @return      A {@link BufferedImage} containing a single pixel of the given color.
	 */
	private BufferedImage oneByOne(final @Nonnull Color color)
	{
		return solidColor(1, 1, color);
	}

	/**
	 * Creates a {@link BufferedImage} of a single pixel of the given color centered in a 3-by-3px
	 * image.
	 *
	 * @param color The color to use for the centered pixel.
	 * @return      A {@link BufferedImage} with completely transparent pixels and one pixel of the
	 *              given color in the center.
	 */
	private BufferedImage centeredPixel(final @Nonnull Color color)
	{
		final BufferedImage out = new BufferedImage(CENTERED_SIZE, CENTERED_SIZE, BufferedImage.TYPE_INT_ARGB);
		out.setRGB(1, 1, color.getRGB());
		return out;
	}

	/**
	 * Creates a {@link BufferedImage} of a solid color of given width and height.
	 *
	 * @param width  The desired width of the color image.
	 * @param height The desired height of the color image.
	 * @param color  The desired color of the image.
	 * @return       A {@link BufferedImage} of given dimensions filled with the given color.
	 */
	private BufferedImage solidColor(final int width, final int height, final @Nonnull Color color)
	{
		final BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				out.setRGB(x, y, color.getRGB());
			}
		}
		return out;
	}
}
