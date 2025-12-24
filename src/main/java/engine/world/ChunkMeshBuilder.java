package engine.world;

import java.util.ArrayList;
import java.util.List;

public class ChunkMeshBuilder {
    public static ChunkMesh build(Chunk chunk) {
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
                            addFace(vertices, indices, x, y, z, face, indexOffset);
                            indexOffset += 4;
                        }
                    }
                }
            }
        }

        return new ChunkMesh(vertices, indices);
    }

    private static void addFace(
            List<Float> vertices,
            List<Integer> indices,
            int x, int y, int z,
            Face face,
            int indexOffset
    ) {
        float nx = face.nx;
        float ny = face.ny;
        float nz = face.nz;

        // Color
        float r = 0.3f, g = 0.8f, b = 0.3f;

        for (int i = 0; i < 4; i++) {
            int[] v = face.vertices[i];

            vertices.add((float) x + v[0]);
            vertices.add((float) y + v[1]);
            vertices.add((float) z + v[2]);

            vertices.add(nx);
            vertices.add(ny);
            vertices.add(nz);

            vertices.add(r);
            vertices.add(g);
            vertices.add(b);
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

        if (x == 0 && y == 2 && z == 0 && f == Face.SOUTH) {
            System.out.println(
                    "CHECK SOUTH: neighbor coords = (" +
                            nx + "," + ny + "," + nz + ")"
            );
        }

        if (nx < 0 || nx >= Chunk.SIZE_X ||
            ny < 0 || ny >= Chunk.SIZE_Y ||
            nz < 0 || nz >= Chunk.SIZE_Z) {
            return true;
        }

        return !c.getBlock(nx, ny, nz).isSolid();
    }
}
