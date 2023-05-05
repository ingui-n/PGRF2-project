package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static project.Variables.MAP_WIDTH;

public class SettingsFrame extends JFrame implements ActionListener {
    //public int MAP_WIDTH = 20;
    public int MAP_HEIGHT = 20;
    Renderer renderer;
    LwjglWindow lwjglWindow;

    JTextField txtMapWidth, txtMapHeight;


    private JLabel widthLabel, heightLabel, elevationLabel, typeLabel, treesLabel;
    private JTextField mapWidthField, mapHeightField, mapElevationField, mapTreesField;
    private JComboBox<String> typeComboBox;
    private JButton confirmButton;

    public SettingsFrame(int width, int height) {
        super("Map Settings");

        initRenderer();
//        this.renderer = renderer;

        setSize(width, height);
       // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        initGui();
//        setVisible(true);


        widthLabel = new JLabel("Map Width:");
        heightLabel = new JLabel("Map Height:");
        elevationLabel = new JLabel("Max Elevation:");
        typeLabel = new JLabel("Map Type:");
        treesLabel = new JLabel("Number of Trees:");

        mapWidthField = new JTextField(Integer.toString(MAP_WIDTH), 5);
        mapHeightField = new JTextField(5);
        mapElevationField = new JTextField(5);
        mapTreesField = new JTextField(5);

        typeComboBox = new JComboBox<>(new String[] {"Terrain", "Satellite", "Hybrid"});

        confirmButton = new JButton("Generate Map");
        confirmButton.addActionListener(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        panel.add(widthLabel, constraints);
        constraints.gridx = 1;
        panel.add(mapWidthField, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        panel.add(heightLabel, constraints);
        constraints.gridx = 1;
        panel.add(mapHeightField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        panel.add(elevationLabel, constraints);
        constraints.gridx = 1;
        panel.add(mapElevationField, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        panel.add(typeLabel, constraints);
        constraints.gridx = 1;
        panel.add(typeComboBox, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        panel.add(treesLabel, constraints);
        constraints.gridx = 1;
        panel.add(mapTreesField, constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(confirmButton, constraints);

        add(panel);
        setVisible(true);
    }

    private void initRenderer() {
        renderer = new Renderer();
        lwjglWindow = new LwjglWindow(renderer, false);
    }

    public void actionPerformed(ActionEvent e) {
        // Handle button click event here
        MAP_WIDTH = 100;
        initRenderer();
    }

    private void initGui() {
        JPanel panelMain = new JPanel();

        panelMain.add(initMapWidthPanel());
        panelMain.add(initMapHeightPanel());

        add(panelMain);
    }

    private JPanel initMapWidthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Map width:"));
        txtMapWidth = new JTextField(Integer.toString(MAP_WIDTH), 5);
        panel.add(txtMapWidth);

        return panel;
    }

    private JPanel initMapHeightPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("Map height:"));
        txtMapHeight = new JTextField(Integer.toString(MAP_HEIGHT), 5);
        panel.add(txtMapHeight);

        return panel;
    }
}
