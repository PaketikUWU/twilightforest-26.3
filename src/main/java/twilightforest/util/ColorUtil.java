package twilightforest.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public record ColorUtil(Function<DyeColor, Block> function) {
	public static final ColorUtil WOOL = new ColorUtil(color -> switch (color) {
		default -> Blocks.WOOL.pick(DyeColor.WHITE);
		case ORANGE -> Blocks.WOOL.pick(DyeColor.ORANGE);
		case MAGENTA -> Blocks.WOOL.pick(DyeColor.MAGENTA);
		case LIGHT_BLUE -> Blocks.WOOL.pick(DyeColor.LIGHT_BLUE);
		case YELLOW -> Blocks.WOOL.pick(DyeColor.YELLOW);
		case LIME -> Blocks.WOOL.pick(DyeColor.LIME);
		case PINK -> Blocks.WOOL.pick(DyeColor.PINK);
		case GRAY -> Blocks.WOOL.pick(DyeColor.GRAY);
		case LIGHT_GRAY -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY);
		case CYAN -> Blocks.WOOL.pick(DyeColor.CYAN);
		case PURPLE -> Blocks.WOOL.pick(DyeColor.PURPLE);
		case BLUE -> Blocks.WOOL.pick(DyeColor.BLUE);
		case BROWN -> Blocks.WOOL.pick(DyeColor.BROWN);
		case GREEN -> Blocks.WOOL.pick(DyeColor.GREEN);
		case RED -> Blocks.WOOL.pick(DyeColor.RED);
		case BLACK -> Blocks.WOOL.pick(DyeColor.BLACK);
	});

	public static final ColorUtil TERRACOTTA = new ColorUtil(color -> switch (color) {
		default -> Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE);
		case ORANGE -> Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE);
		case MAGENTA -> Blocks.DYED_TERRACOTTA.pick(DyeColor.MAGENTA);
		case LIGHT_BLUE -> Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_BLUE);
		case YELLOW -> Blocks.DYED_TERRACOTTA.pick(DyeColor.YELLOW);
		case LIME -> Blocks.DYED_TERRACOTTA.pick(DyeColor.LIME);
		case PINK -> Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK);
		case GRAY -> Blocks.DYED_TERRACOTTA.pick(DyeColor.GRAY);
		case LIGHT_GRAY -> Blocks.DYED_TERRACOTTA.pick(DyeColor.LIGHT_GRAY);
		case CYAN -> Blocks.DYED_TERRACOTTA.pick(DyeColor.CYAN);
		case PURPLE -> Blocks.DYED_TERRACOTTA.pick(DyeColor.PURPLE);
		case BLUE -> Blocks.DYED_TERRACOTTA.pick(DyeColor.BLUE);
		case BROWN -> Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN);
		case GREEN -> Blocks.DYED_TERRACOTTA.pick(DyeColor.GREEN);
		case RED -> Blocks.DYED_TERRACOTTA.pick(DyeColor.RED);
		case BLACK -> Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK);
	});

	public static final ColorUtil STAINED_GLASS = new ColorUtil(color -> switch (color) {
		default -> Blocks.STAINED_GLASS.pick(DyeColor.WHITE);
		case ORANGE -> Blocks.STAINED_GLASS.pick(DyeColor.ORANGE);
		case MAGENTA -> Blocks.STAINED_GLASS.pick(DyeColor.MAGENTA);
		case LIGHT_BLUE -> Blocks.STAINED_GLASS.pick(DyeColor.LIGHT_BLUE);
		case YELLOW -> Blocks.STAINED_GLASS.pick(DyeColor.YELLOW);
		case LIME -> Blocks.STAINED_GLASS.pick(DyeColor.LIME);
		case PINK -> Blocks.STAINED_GLASS.pick(DyeColor.PINK);
		case GRAY -> Blocks.STAINED_GLASS.pick(DyeColor.GRAY);
		case LIGHT_GRAY -> Blocks.STAINED_GLASS.pick(DyeColor.LIGHT_GRAY);
		case CYAN -> Blocks.STAINED_GLASS.pick(DyeColor.CYAN);
		case PURPLE -> Blocks.STAINED_GLASS.pick(DyeColor.PURPLE);
		case BLUE -> Blocks.STAINED_GLASS.pick(DyeColor.BLUE);
		case BROWN -> Blocks.STAINED_GLASS.pick(DyeColor.BROWN);
		case GREEN -> Blocks.STAINED_GLASS.pick(DyeColor.GREEN);
		case RED -> Blocks.STAINED_GLASS.pick(DyeColor.RED);
		case BLACK -> Blocks.STAINED_GLASS.pick(DyeColor.BLACK);
	});


	public BlockState getColor(DyeColor color) {
		return this.function.apply(color).defaultBlockState();
	}

	public Block getRandomColor(Random rand) {
		DyeColor color = DyeColor.byId(rand.nextInt(16));
		return this.getColor(color).getBlock();
	}

	public static float[] rgbToHSV(int r, int g, int b) {
		float h = 0;
		float s;
		float rabs = r / 255.0F;
		float gabs = g / 255.0F;
		float babs = b / 255.0F;
		float v = Math.max(rabs, Math.max(gabs, babs));
		float diff = v - Math.min(rabs, Math.min(gabs, babs));
		Function<Float, Float> diffc = c -> (v - c) / 6 / diff + 1 / 2;
		if (diff == 0) {
			h = s = 0;
		} else {
			s = diff / v;
			float rr = diffc.apply(rabs);
			float gg = diffc.apply(gabs);
			float bb = diffc.apply(babs);

			if (rabs == v) {
				h = bb - gg;
			} else if (gabs == v) {
				h = (1.0F / 3.0F) + rr - bb;
			} else if (babs == v) {
				h = (2.0F / 3.0F) + gg - rr;
			}
			if (h < 0) {
				h += 1;
			} else if (h > 1) {
				h -= 1;
			}
		}
		return new float[]{h, s, v};
	}

	public static int hsvToRGB(float hue, float saturation, float value) {
		final float normaliedHue = (hue - (float) Math.floor(hue));
		final int h = (int) (normaliedHue * 6);
		final float f = normaliedHue * 6 - h;
		final float p = value * (1 - saturation);
		final float q = value * (1 - f * saturation);
		final float t = value * (1 - (1 - f) * saturation);

		return switch (h) {
			case 0 -> rgb(value, t, p);
			case 1 -> rgb(q, value, p);
			case 2 -> rgb(p, value, t);
			case 3 -> rgb(p, q, value);
			case 4 -> rgb(t, p, value);
			case 5 -> rgb(value, p, q);
			default -> throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
		};
	}

	private static int rgb(float r, float g, float b) {
		return (((int) ((r * 255F) + 0.5F) & 0xFF) << 16) | (((int) ((g * 255F) + 0.5F) & 0xFF) << 8) | ((int) ((b * 255F) + 0.5F) & 0xFF);
	}

	public static int argbToABGR(int argbColor) {
		int r = (argbColor >> 16) & 0xFF;
		int b = argbColor & 0xFF;
		return (argbColor & 0xFF00FF00) | (b << 16) | r;
	}

	//We COULD use the WOOL method at the very top of this class, but then we have to use the order of the dyecolor enum, which doesnt show the wools in the order the ram displays them.
	//I personally like this order better so suck it
	public static final Map<DyeColor, Block> WOOL_TO_DYE_IN_RAM_ORDER = ImmutableMap.ofEntries(
		entryOf(DyeColor.WHITE, Blocks.WOOL.pick(DyeColor.WHITE)), entryOf(DyeColor.LIGHT_GRAY, Blocks.WOOL.pick(DyeColor.LIGHT_GRAY)),
		entryOf(DyeColor.GRAY, Blocks.WOOL.pick(DyeColor.GRAY)), entryOf(DyeColor.BLACK, Blocks.WOOL.pick(DyeColor.BLACK)),
		entryOf(DyeColor.RED, Blocks.WOOL.pick(DyeColor.RED)), entryOf(DyeColor.ORANGE, Blocks.WOOL.pick(DyeColor.ORANGE)),
		entryOf(DyeColor.YELLOW, Blocks.WOOL.pick(DyeColor.YELLOW)), entryOf(DyeColor.LIME, Blocks.WOOL.pick(DyeColor.LIME)),
		entryOf(DyeColor.GREEN, Blocks.WOOL.pick(DyeColor.GREEN)), entryOf(DyeColor.LIGHT_BLUE, Blocks.WOOL.pick(DyeColor.LIGHT_BLUE)),
		entryOf(DyeColor.CYAN, Blocks.WOOL.pick(DyeColor.CYAN)), entryOf(DyeColor.BLUE, Blocks.WOOL.pick(DyeColor.BLUE)),
		entryOf(DyeColor.PURPLE, Blocks.WOOL.pick(DyeColor.PURPLE)), entryOf(DyeColor.MAGENTA, Blocks.WOOL.pick(DyeColor.MAGENTA)),
		entryOf(DyeColor.PINK, Blocks.WOOL.pick(DyeColor.PINK)), entryOf(DyeColor.BROWN, Blocks.WOOL.pick(DyeColor.BROWN)));

	static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}

	public static int blendColors(int a, int b, double ratio) {
		int mask1 = 0x00FF00FF;
		int mask2 = 0xFF00FF00;

		int f2 = (int) (256 * ratio);
		int f1 = 256 - f2;

		return (((((a & mask1) * f1) + ((b & mask1) * f2)) >> 8) & mask1)
			| (((((a & mask2) * f1) + ((b & mask2) * f2)) >> 8) & mask2);
	}
}
