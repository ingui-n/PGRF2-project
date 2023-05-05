package project;

public class Tile {
    private final int x, y;
    private int terrainType, object, objectTextureNum;

    public Tile(int x, int y, int terrainType) {
        this.x = x;
        this.y = y;
        this.terrainType = terrainType;
        this.object = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTerrainType() {
        return terrainType;
    }

    public int getObject() {
        return object;
    }

    public void setObject(int object) {
        this.object = object;

        switch (object) {
            case 1 -> setRandomObjectTexture(9);
            case 2, 3 -> setRandomObjectTexture(3);
        }
    }

    private void setRandomObjectTexture(int range) {
        objectTextureNum = new Utils<Integer>().getRandomNumber(range);
    }

    public void setTerrainType(int terrainType) {
        this.terrainType = terrainType;
    }

    public int getObjectTextureNum() {
        return objectTextureNum;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
