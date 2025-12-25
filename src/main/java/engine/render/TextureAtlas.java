package engine.render;

public class TextureAtlas {
    private final int tilesX;
    private final int tilesY;

    public TextureAtlas(int tilesX, int tilesY) {
        this.tilesX = tilesX;
        this.tilesY = tilesY;
    }

    public float[] uvRect(int tileX, int tileY) {
        float u0 = (float) tileX / tilesX;
        float v0 = (float) tileY / tilesY;
        float u1 = u0 + 1f / tilesX;
        float v1 = v0 + 1f / tilesY;
        return new float[] { u0, v0, u1, v1 };
    }
}
