package engine.render;

import engine.math.Matrix4f;

public class Renderer {
    public void render(Mesh mesh, ShaderProgram shader,
                       Matrix4f model, Matrix4f view, Matrix4f projection) {
        shader.bind();
        shader.setUniformMat4f("uModel", model);
        shader.setUniformMat4f("uView", view);
        shader.setUniformMat4f("uProjection", projection);
        mesh.render();
        shader.unbind();
    }
}
