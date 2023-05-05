package project;

import java.util.Random;

public class Rock {
    private final boolean[][] rock;
    private final Tile[][] map;
    private final int width, height;
    private final Utils utils = new Utils();
    private final int MAX_POSSION_LOOKUP = 5;

    public Rock(int width, int height, Tile[][] map) {//todo save to Tile
        this.width = width;
        this.height = height;
        this.map = map;
        rock = new boolean[width][height];
        initRocks();
    }

    private void initRocks() {
        for (int x = 0; x < rock.length; x++) {
            for (int y = 0; y < rock[0].length; y++) {
                rock[x][y] = new Random().nextBoolean();
            }
        }
    }

    public boolean[][] getRock() {
        return rock;
    }

    private void initRock() {
        int rockCount = utils.getRandomNumber(5);

        for (int i = 0; i < rockCount; i++) {
            boolean[][] rock = new Rock(2, 2, map).getRock();
            Tile tile = getPositionWithoutAreaCollision(rock, 1);

            if (tile == null) {
                continue;
            }

            int tileX = tile.getX();
            int tileY = tile.getY();

            for (int x = 0; x < rock.length; x++) {
                for (int y = 0; y < rock[0].length; y++) {
                    if (width <= tileX + x && height <= tileY + y) {
                        Tile targetTile = getTile(tileX + x, tileY + y);

                        if (targetTile.getObject() == 0)
                            targetTile.setObject(rock[x][y] ? 1 : 0);
                    }
                }
            }
        }
    }

    private Tile getPositionWithoutAreaCollision(boolean[][] area, int attempt) {
        Tile tile = getRandomTile();

        if (!isAreaEmpty(tile, area)) {
            attempt++;

            if (attempt > MAX_POSSION_LOOKUP)
                return null;

            return getPositionWithoutAreaCollision(area, attempt);
        }

        return tile;
    }

    private Tile getTile(int x, int y) {
        return map[x][y];
    }

    private Tile getRandomTile() {
        int x = utils.getRandomNumber(width);
        int y = utils.getRandomNumber(height);

        return map[x][y];
    }

    private boolean isAreaEmpty(Tile startTile, boolean[][] area) {
        int areaWidth = area.length;
        int areaHeight = area[0].length;

        if (startTile.getX() + areaWidth > width)
            areaWidth = width;

        if (startTile.getY() + areaHeight > height)
            areaHeight = height;

        for (int x = startTile.getX(), areaX = 0; x < areaWidth; x++, areaX++) {
            for (int y = startTile.getY(), areaY = 0; y < areaHeight; y++, areaY++) {
                if (area[areaX][areaY] && getTile(x, y).getObject() != 0)
                    return false;
            }
        }

        return true;
    }
}
