package engine.world;

public enum Face {
    TOP(
            0, 1, 0,
            new int[][] {
                    {0,1,1}, {1,1,1}, {1,1,0}, {0,1,0}
            }
    ),

    BOTTOM(
            0, -1, 0,
            new int[][] {
                    {0,0,0}, {1,0,0}, {1,0,1}, {0,0,1}
            }
    ),

    NORTH(
            0, 0, -1,
            new int[][] {
                    {1,0,0}, {0,0,0}, {0,1,0}, {1,1,0}
            }
    ),

    SOUTH(
            0, 0, 1,
            new int[][] {
                    {0,0,1}, {1,0,1}, {1,1,1}, {0,1,1}
            }
    ),

    WEST(
            -1, 0, 0,
            new int[][] {
                    {0,0,0}, {0,0,1}, {0,1,1}, {0,1,0}
            }
    ),

    EAST(
            1, 0, 0,
            new int[][] {
                    {1,0,1}, {1,0,0}, {1,1,0}, {1,1,1}
            }
    );

    public final int nx, ny, nz;
    public final int[][] vertices;

    Face(int nx, int ny, int nz, int[][] vertices) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
        this.vertices = vertices;
    }
}
