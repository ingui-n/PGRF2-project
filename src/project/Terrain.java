package project;

import java.util.ArrayList;

public class Terrain {
    private int[][] terrain;
    private int[][] tempTerrain;
    private Tile[][] map;
    private int width, height;
    private final Utils utils = new Utils();

    public Terrain(int width, int height, Tile[][] map) {
        this.width = width;
        this.height = height;
        this.map = map;
    }

    public Terrain(int elevation, int radius) {
        generateShapeOneLayerTerrain(elevation, radius);
    }

    /**
     * for testing
     */
    public void initElevations() {
        terrain = new int[width + 1][height + 1];

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                terrain[x][y] = 0;
            }
        }
    }

    public void generateLayerTerrain(int mapType) {
        generatePositions(new Point(-2, -2), mapType);
        generatePositions(new Point(12, 0), mapType);
        generatePositions(new Point(0, 12), mapType);
        generatePositions(new Point(12, 12), mapType);
    }

    private void generatePositions(Point point, int mapType) {
        generateTerrain(point, mapType, 3, 8);
        generateTerrain(point, mapType, 6, 4);
        generateTerrain(point, mapType, 9, 3);
    }

    private void generateTerrain(Point startPoint, int mapType, int elevation, int radius) {
        int[][] ter = new Terrain(elevation, radius).getTerrain();

        for (int x = 0; x < ter.length; x++) {
            for (int y = 0; y < ter[0].length; y++) {
                if (ter[x][y] == elevation) {
                    if (isInTerrain(startPoint.getX() + x, startPoint.getY() + y)) {//todo if neighbours elevation > 2
                        terrain[startPoint.getX() + x][startPoint.getY() + y] = ter[x][y];
                    }
                    if (isInMap(startPoint.getX() + x, startPoint.getY() + y)) {
                        getTile(startPoint.getX() + x, startPoint.getY() + y).setTerrainType(mapType);
                    }
                }
            }
        }
    }

    private void generateShapeOneLayerTerrain(int elevation, int radius) {
        ArrayList<Point> points = new ShapeGenerator(radius, .5, .5, 8).generatePolygon();

        tempTerrain = convertToArray(points);

        this.width = tempTerrain.length;
        this.height = tempTerrain[0].length;

        Point previous = points.get(0);

        for (int i = 1; i <= points.size(); i++) {
            Point current = i == points.size() ? points.get(0) : points.get(i);
            drawLine(current.getX(), current.getY(), previous.getX(), previous.getY());
            previous = current;
        }

        fillTerrainLayer();

        setElevationOnLayer(elevation);
        terrain = tempTerrain;
    }

    private int[][] convertToArray(ArrayList<Point> points) {
        int lowX = points.get(0).getX();
        int lowY = points.get(0).getY();
        int topX = points.get(0).getX();
        int topY = points.get(0).getY();

        for (Point point : points) {
            if (point.getX() < lowX)
                lowX = point.getX();
            if (point.getY() < lowY)
                lowY = point.getY();
            if (point.getX() > topX)
                topX = point.getX();
            if (point.getY() > topY)
                topY = point.getY();
        }

        int low = Math.min(lowX, lowY);

        for (Point point : points) {
            point.setX(point.getX() - low);
            point.setY(point.getY() - low);
        }

        int[][] minimalArray = new int[topX + 1 - low][topY + 1 - low];

        for (Point point : points) {
            minimalArray[point.getX()][point.getY()] = 1;
        }

        return minimalArray;
    }

    private void setElevationOnLayer(int elevation) {
        for (int x = 0; x < tempTerrain.length; x++) {
            for (int y = 0; y < tempTerrain[0].length; y++) {
                if (tempTerrain[x][y] == 1)
                    tempTerrain[x][y] = elevation;
            }
        }
    }

    private void fillTerrainLayer() {
        for (int x = 0; x < tempTerrain.length; x++) {
            int yMin = 0;
            int yMax = tempTerrain[0].length - 1;

            if (tempTerrain[x][0] != 1 || tempTerrain[x][tempTerrain[0].length - 1] != 1) {
                for (int y = yMin; y < tempTerrain[0].length; y++) {
                    if (tempTerrain[x][y] == 1) {
                        yMin = y;
                        break;
                    }
                }

                for (int y = yMax; y >= 0; y--) {
                    if (tempTerrain[x][y] == 1) {
                        yMax = y;
                        break;
                    }
                }

                if (yMin == 0 && yMax == tempTerrain[0].length - 1)
                    continue;
            }

            for (int y = yMin; y <= yMax; y++) {
                tempTerrain[x][y] = 1;
            }
        }
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        if (x2 <= x1) {
            int _x1 = x1;
            x1 = x2;
            x2 = _x1;
        }

        if (y2 <= y1) {
            int _y1 = y1;
            y1 = y2;
            y2 = _y1;
        }

        if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
            for (int x = x1; x <= x2; x++) {
                float y = k * x + q;

                setTerrainAround(x, Math.round(y));
            }
        } else {
            for (int y = y1; y <= y2; y++) {
                float x = x1 == x2 ? x1 : (y - q) / k;

                setTerrainAround(Math.round(x), y);
            }
        }
    }

    private boolean isInMap(int x, int y) {
        return x >= 0 && x < this.width && y >= 0 && y < this.height;
    }

    private boolean isInTerrain(int x, int y) {
        return x >= 0 && x <= this.width && y >= 0 && y <= this.height;
    }

    private Tile getTile(int x, int y) {
        return map[x][y];
    }

    public void generateFullRandomTerrain() {
        terrain = new int[width + 1][height + 1];

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                int zOne = x != 0 ? terrain[x - 1][y] : -1;
                int zTwo = y != 0 ? terrain[x][y - 1] : -1;

                if (x == 0 && y == 0) {
                    int rand = utils.getRandomNumber(5);

                    zOne = rand;
                    zTwo = rand;
                } else if (x == 0) {
                    zOne = zTwo;
                } else if (y == 0) {
                    zTwo = zOne;
                }

                int abs = Math.abs(zOne - zTwo);
                int min = Math.min(zOne, zTwo);

                switch (abs) {
                    case 0 -> terrain[x][y] = utils.getRandomNumber(3) - 1 + min;
                    case 1 -> terrain[x][y] = utils.getRandomNumber(2) + min;
                    case 2 -> terrain[x][y] = min + 1;
                }
            }
        }
    }


    public int[][] getTerrain() {
        return terrain;
    }

    public int getElevation(int x, int y) {
        return terrain[x][y];
    }

    private void setTerrainWithTest(int x, int y) {
        if (isInMap(x, y))
            tempTerrain[x][y] = 1;
    }

    private void setTerrainAround(int x, int y) {
        setTerrainWithTest(x, y);
        setTerrainWithTest(x - 1, y - 1);
        setTerrainWithTest(x + 1, y + 1);
    }
}
