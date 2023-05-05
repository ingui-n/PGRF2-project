package project;

public class Map {
    private Tile[][] map;
    private final int width, height;
    private int mapType;
    private final Utils<Object> utils = new Utils<>();
    private final Terrain terrain;


    public Map(int width, int height, boolean onlyInit) {
        this.width = width;
        this.height = height;

        setMapType();
        initMap();

        terrain = new Terrain(width, height, map);

        if (onlyInit) {
            terrain.initElevations();
            terrain.generateLayerTerrain(mapType);

            setRandomObjects();
        } else {
            terrain.generateFullRandomTerrain();
        }
    }

    private void setMapType() {
        mapType = utils.getRandomNumberInRage(1, 2);
    }

    private void initMap() {
        map = new Tile[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = new Tile(x, y, 5);
            }
        }
    }

    private void setRandomObjects() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = getTile(x, y);

                if (isFlat(tile) && tile.getTerrainType() != 5) {
                    tile.setObject(utils.getRandomNumberInRage(0, 3));
                }
            }
        }
    }

    private boolean isFlat(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();

        int z0 = getElevation(x, y + 1);
        int z1 = getElevation(x + 1, y + 1);
        int z2 = getElevation(x + 1, y);
        int z3 = getElevation(x, y);

        return z0 == z1 && z1 == z2 && z2 == z3;
    }

    public Tile getTile(int x, int y) {
        return map[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getElevation(int x, int y) {
        return terrain.getElevation(x, y);
    }
}
