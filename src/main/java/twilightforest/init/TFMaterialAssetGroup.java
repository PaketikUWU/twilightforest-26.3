package twilightforest.init;

import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;

// 26.3: MaterialAssetGroup is gone, trim materials now reference a palette id directly (per-armor overrides moved to the client equipment assets)
public class TFMaterialAssetGroup {

	public static final Identifier IRONWOOD = create("ironwood");
	public static final Identifier STEELEAF = create("steeleaf");
	public static final Identifier KNIGHTMETAL = create("knightmetal");
	public static final Identifier FIERY = create("fiery");
	public static final Identifier NAGA_SCALE = create("naga_scale");
	public static final Identifier CARMINITE = create("carminite");

	public static Identifier create(String base) {
		return TwilightForestMod.prefix("trim/" + base);
	}
}
