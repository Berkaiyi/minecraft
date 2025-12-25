package engine.world;

public enum BlockType {
    AIR(false, -1, -1),
    GRASS(true, 0, 0),
    DIRT(true, 1, 0),
    STONE(true, 2, 0);

    private final boolean solid;
    private final int tileX, tileY;

    BlockType(boolean solid, int tileX, int tileY) {
        this.solid = solid;
        this.tileX = tileX;
        this.tileY = tileY;
    }

    public boolean isSolid() { return solid; }
    public int tileX() { return tileX; }
    public int tileY() { return tileY; }
}
