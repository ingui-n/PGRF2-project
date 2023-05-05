package project;

import lwjglutils.OGLTexture2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import transforms.Vec3D;

import java.io.*;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static project.GluUtils.gluPerspective;

public class Renderer extends AbstractRenderer {
    private float zenith, azimuth, startZenit = 70.4f, startAzimut = 69.1f, radius = 5;
    private double camPositionX = 3, camPositionY = -44, camPositionZ = 36;
    private float trans, deltaTrans = 0;
    private boolean mouseButton1 = false;
    private float dx, dy, ox, oy;
    private GLCamera camera;
    private Vec3D pos = new Vec3D(camPositionX, camPositionY, camPositionZ);
    private OGLTexture2D dirtTexture, grassTexture, fallTexture, sandTexture, waterTexture;
    private ArrayList<OGLTexture2D> trees, rocks, golds;
    private Map map;
    private final int MAP_WIDTH = 20, MAP_HEIGHT = 20;


    public Renderer() {
        super();

        glfwKeyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
                if (action == GLFW_RELEASE) {
                    trans = 0;
                    deltaTrans = 0;
                }

                if (action == GLFW_PRESS) {
                    switch (key) {
                        case GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D -> deltaTrans = 0.01f;
                    }
                }

                switch (key) {
                    case GLFW_KEY_Q -> {
                        camera.up(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                    case GLFW_KEY_E -> {
                        camera.down(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                    case GLFW_KEY_W -> {
                        camera.forward(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                    case GLFW_KEY_S -> {
                        camera.backward(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                    case GLFW_KEY_A -> {
                        camera.left(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                    case GLFW_KEY_D -> {
                        camera.right(trans);
                        if (deltaTrans < 0.001f)
                            deltaTrans = 0.001f;
                        else
                            deltaTrans *= 1.02;
                    }
                }
            }
        };

        glfwMouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);

                mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

                if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    ox = (float) x;
                    oy = (float) y;
                }
            }

        };

        glfwCursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                if (mouseButton1) {
                    dx = (float) x - ox;
                    dy = (float) y - oy;
                    ox = (float) x;
                    oy = (float) y;
                    zenith -= dy / width * 180;
                    azimuth += dx / height * 180;
                    azimuth = azimuth % 360;
                    camera.setAzimuth(Math.toRadians(azimuth) + startAzimut);
                    camera.setZenith(Math.toRadians(zenith) + startZenit);
                    dx = 0;
                    dy = 0;
                }
            }
        };
    }

    @Override
    public void init() {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        camera = new GLCamera();
        camera.setPosition(pos);
        camera.setPosition(new Vec3D(camPositionX, camPositionY, camPositionZ));
        camera.setFirstPerson(true);
        camera.setZenith(startZenit);
        camera.addAzimuth(startAzimut);
        camera.setRadius(radius);

        initTextures();
        initMap();
    }

    @Override
    public void display() {
        glViewport(0, 0, width, height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        trans += deltaTrans;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        gluPerspective(45, width / (float) height, .1f, 500f);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        camera.setMatrix();
        glPushMatrix();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glPopMatrix();

        glRotated(45, 0, 0, 1);
        /*glRotated(90, 1, 0, 1);//todo set better viewpoint
        glRotated(90, 1, 1, 0);
        glRotated(180, 1, 1, 0);*/

        renderMap();
    }

    private void initMap() {
        map = new Map(MAP_WIDTH, MAP_HEIGHT, true);
    }

    private void initTextures() {
        try {
            dirtTexture = new OGLTexture2D("textures/dirt.png");
            grassTexture = new OGLTexture2D("textures/grass.png");
            fallTexture = new OGLTexture2D("textures/fall.png");
            sandTexture = new OGLTexture2D("textures/sand.png");
            waterTexture = new OGLTexture2D("textures/water.png");

            trees = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                trees.add(new OGLTexture2D("textures/tree" + i + ".png"));
            }

            rocks = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                rocks.add(new OGLTexture2D("textures/rock" + i + ".png"));
            }

            golds = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                golds.add(new OGLTexture2D("textures/gold" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderMap() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                renderTile(map.getTile(x, y));
            }
        }
    }

    private void renderTile(Tile tile) {
        switch (tile.getTerrainType()) {
            case 1 -> dirtTexture.bind();
            case 2 -> grassTexture.bind();
            case 3 -> fallTexture.bind();
            case 4 -> sandTexture.bind();
            case 5 -> waterTexture.bind();
        }

        int x = tile.getX();
        int y = tile.getY();

        int z0 = map.getElevation(x, y + 1);
        int z1 = map.getElevation(x + 1, y + 1);
        int z2 = map.getElevation(x + 1, y);
        int z3 = map.getElevation(x, y);

        x *= 4;
        y *= 4;

        glBegin(GL_TRIANGLE_FAN);
        glTexCoord2f(0, 1);
        glVertex3d(x, y + 4, z0);

        glTexCoord2f(1, 1);
        glVertex3d(x + 4, y + 4, z1);

        glTexCoord2f(1, 0);
        glVertex3d(x + 4, y, z2);

        glTexCoord2f(0, 0);
        glVertex3d(x, y, z3);
        glEnd();

        if (tile.getObject() != 0) {
            int objectHeight;

            switch (tile.getObject()) {
                case 1 -> {
                    trees.get(tile.getObjectTextureNum()).bind();
                    objectHeight = 8;
                }
                case 2 -> {
                    rocks.get(tile.getObjectTextureNum()).bind();
                    objectHeight = 2;
                }
                case 3 -> {
                    golds.get(tile.getObjectTextureNum()).bind();
                    objectHeight = 2;
                }
                default -> {
                    return;
                }
            }

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_ALPHA_TEST);
            glAlphaFunc(GL_GREATER, 0.1f);

            glBegin(GL_TRIANGLE_FAN);
            glTexCoord2f(0, 1);
            glVertex3d(x, y + 4, z0);
            glTexCoord2f(1, 1);
            glVertex3d(x + 4, y, z1);
            glTexCoord2f(1, 0);
            glVertex3d(x + 4, y, z2 + objectHeight);
            glTexCoord2f(0, 0);
            glVertex3d(x, y + 4, z3 + objectHeight);
            glEnd();
        }
    }
}
