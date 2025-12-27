package engine.world;

import engine.render.TextureAtlas;
import engine.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChunkMeshBuilder {
    public static ChunkMesh build(Chunk chunk, TextureAtlas atlas) {
        long t0 = System.nanoTime();
        Log.debug("Mesher", "build chunk=%s", chunk.getPos());

        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int indexOffset = 0;

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {

                    Block block = chunk.getBlock(x, y, z);
                    if (!block.isSolid()) { continue; }

                    for (Face face : Face.values()) {
                        if (isFaceVisible(chunk, x, y, z, face)) {
                            addFace(vertices, indices, x, y, z, face, indexOffset, block.getType(), atlas);
                            indexOffset += 4;
                        }
                    }
                }
            }
        }

        int vertexCount = vertices.size() / 8;
        int triCount = indices.size() / 3;
        double ms = (System.nanoTime() - t0) / 1_000_000.0;
        Log.info("Mesher", "chunk=%s vertices=%d tris=%d time=%.2fms",
                chunk.getPos(), vertexCount, triCount, ms);

        return new ChunkMesh(vertices, indices);
    }

    private static void addFace(
            List<Float> vertices,
            List<Integer> indices,
            int x, int y, int z,
            Face face,
            int indexOffset,
            BlockType type,
            TextureAtlas atlas
    ) {
        float nx = face.nx;
        float ny = face.ny;
        float nz = face.nz;

        float[] rect = atlas.uvRect(type.tileX(), type.tileY());
        float u0 = rect[0], v0 = rect[1], u1 = rect[2], v1 = rect[3];

        for (int i = 0; i < 4; i++) {
            int[] lv = face.vertices[i];

            vertices.add((float) x + lv[0]);
            vertices.add((float) y + lv[1]);
            vertices.add((float) z + lv[2]);

            vertices.add(nx);
            vertices.add(ny);
            vertices.add(nz);

            float fu = face.uvs[i * 2];
            float fv = face.uvs[i * 2 + 1];
            float u = (fu == 0f) ? u0 : u1;
            float v = (fv == 0f) ? v0 : v1;
            vertices.add(u);
            vertices.add(v);
        }

        indices.add(indexOffset);
        indices.add(indexOffset+1);
        indices.add(indexOffset+2);
        indices.add(indexOffset+2);
        indices.add(indexOffset+3);
        indices.add(indexOffset);
    }

    private static boolean isFaceVisible(Chunk c, int x, int y, int z, Face f) {
        int nx = x + f.nx;
        int ny = y + f.ny;
        int nz = z + f.nz;

        if (nx < 0 || nx >= Chunk.SIZE_X ||
            ny < 0 || ny >= Chunk.SIZE_Y ||
            nz < 0 || nz >= Chunk.SIZE_Z) {
            return true;
        }

        return !c.getBlock(nx, ny, nz).isSolid();
    }
}
