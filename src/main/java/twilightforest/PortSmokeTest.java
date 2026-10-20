package twilightforest;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import twilightforest.init.TFDimension;

// TEMPORARY port smoke test, enabled with -Dtf.porttest
public final class PortSmokeTest {
	public static void register() {
		if (System.getProperty("tf.porttest") != null)
			NeoForge.EVENT_BUS.addListener(PortSmokeTest::run);
	}

	private static void run(ServerStartedEvent event) {
		ServerLevel level = event.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null) {
			TwilightForestMod.LOGGER.error("PORTTEST no twilight level");
			return;
		}
		int chunks = 0;
		for (int x = -8; x < 8; x++)
			for (int z = -8; z < 8; z++) {
				level.getChunk(x, z);
				chunks++;
			}
		TwilightForestMod.LOGGER.info("PORTTEST generated {} chunks around origin", chunks);
		for (Holder.Reference<Structure> structure : level.registryAccess().lookupOrThrow(Registries.STRUCTURE).listElements().filter(h -> h.key().identifier().getNamespace().equals(TwilightForestMod.ID)).toList()) {
			try {
				Pair<BlockPos, Holder<Structure>> found = level.getChunkSource().getGenerator().findNearestMapStructure(level, HolderSet.direct(structure), BlockPos.ZERO, 100, false);
				if (found == null) {
					TwilightForestMod.LOGGER.warn("PORTTEST {} not found", structure.key().identifier());
					continue;
				}
				BlockPos pos = found.getFirst();
				for (int dx = -2; dx <= 2; dx++)
					for (int dz = -2; dz <= 2; dz++)
						level.getChunk((pos.getX() >> 4) + dx, (pos.getZ() >> 4) + dz);
				TwilightForestMod.LOGGER.info("PORTTEST {} generated at {}", structure.key().identifier(), pos);
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("PORTTEST {} failed", structure.key().identifier(), e);
			}
		}
		TwilightForestMod.LOGGER.info("PORTTEST done");
	}
}
