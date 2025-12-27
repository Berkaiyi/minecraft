package engine.world;

import engine.util.Log;

import java.util.HashMap;
import java.util.Map;

public class World {
    private final Map<ChunkPos, Chunk> chunks = new HashMap<>();

    public void putChunk(Chunk chunk) {
        chunks.put(chunk.getPos(), chunk);
        Log.debug("World", "putChunk %s", chunk.getPos());
    }

    public Chunk getChunk(ChunkPos pos) {
        return chunks.get(pos);
    }

    public Map<ChunkPos, Chunk> getChunks() {
        return chunks;
    }

    public BlockType getBlockType(int worldX, int worldY, int worldZ) {
        if (worldY < 0 || worldY >= Chunk.SIZE_Y) { return BlockType.AIR; }

        int chunkPosX = Math.floorDiv(worldX, Chunk.SIZE_X);
        int chunkPosZ = Math.floorDiv(worldZ, Chunk.SIZE_Z);

        int chunkX = Math.floorMod(worldX, Chunk.SIZE_X);
        int chunkZ = Math.floorMod(worldZ, Chunk.SIZE_Z);

        Chunk c = chunks.get(new ChunkPos(chunkPosX, chunkPosZ));
        if (c == null) { return BlockType.AIR; }

        return c.getBlock(chunkX, worldY, chunkZ).getType();
    }

    public void setBlockType(int worldX, int worldY, int worldZ, BlockType type) {
        if (worldY < 0 || worldY >= Chunk.SIZE_Y) { return; }

        int chunkPosX = Math.floorDiv(worldX, Chunk.SIZE_X);
        int chunkPosZ = Math.floorDiv(worldZ, Chunk.SIZE_Z);

        int chunkX = Math.floorMod(worldX, Chunk.SIZE_X);
        int chunkZ = Math.floorMod(worldZ, Chunk.SIZE_Z);

        Chunk c = chunks.get(new ChunkPos(chunkPosX, chunkPosZ));
        if (c == null) { return; }

        c.setBlock(chunkX, worldY, chunkZ, type);
    }
}
