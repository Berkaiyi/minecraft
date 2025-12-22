package engine.render;

import engine.math.Matrix4f;

public class Renderer {
    public void render(Mesh mesh, ShaderProgram shader, Matrix4f transform) {
        shader.bind();
        shader.setUniformMat4f("uTransform", transform);
        mesh.render();
        shader.unbind();
    }
}
