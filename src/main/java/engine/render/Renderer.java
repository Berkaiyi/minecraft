package engine.render;

public class Renderer {
    public void render(Mesh mesh, ShaderProgram shader) {
        shader.bind();
        mesh.render();
        shader.unbind();
    }
}
