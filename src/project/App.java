package project;

public class App {
    public static void main(String[] args) {
//        new Variables();
//        Renderer renderer = new Renderer();
        //SettingsFrame settingsFrame = new SettingsFrame(300, 500);
        //new LwjglWindow(renderer, false);
        new LwjglWindow(new Renderer(), false);
    }

    public void se() {
        Renderer renderer = new Renderer();
        new LwjglWindow(renderer, false);
    }
}
