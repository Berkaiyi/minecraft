package engine.world;

import engine.render.Mesh;

import java.util.List;

public class ChunkMesh {
    private final Mesh mesh;

    public ChunkMesh(List<Float> vertices, List<Integer> indices) {
        float[] vert = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            vert[i] = vertices.get(i);
        }

        int[] ind = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            ind[i] = indices.get(i);
        }

        mesh = new Mesh(vert, ind);
    }

    public Mesh getMesh() { return mesh; }
    public void cleanup() { mesh.cleanup(); }
}
