package engine.world;

import engine.util.Log;

public class Chunk {
    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 256;
    public static final int SIZE_Z = 16;

    private final ChunkPos pos;
    private final Block[][][] blocks;

    public Chunk(ChunkPos pos) {
        Log.debug("Chunk", "new Chunk pos=%s", pos);
        this.pos = pos;
        this.blocks = new Block[SIZE_X][SIZE_Y][SIZE_Z];
        initEmpty();
    }

    public ChunkPos getPos() { return pos; }

    public Block getBlock(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public void setBlock(int x, int y, int z, BlockType type) {
        BlockType oldType = blocks[x][y][z].getType();
        //Log.debug("Chunk", "setBlock local=(%d,%d,%d) %s -> %s (chunk=%s)",
        //        x,y,z, oldType, type, pos);
        blocks[x][y][z].setType(type);
    }

    private void initEmpty() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                for (int z = 0; z < SIZE_Z; z++) {
                    blocks[x][y][z] = new Block(BlockType.AIR);
                }
            }
        }
    }
}
