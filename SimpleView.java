package br.pedro.smartgrid;

import javax.swing.*;
import java.awt.*;

public class SimpleView extends JFrame implements SimulationManager.Listener {

    private final JLabel tickLbl    = new JLabel("Tick: 0");
    private final JLabel prodLbl    = new JLabel("Gerado: 0.00 kWh");
    private final JLabel consLbl    = new JLabel("Consumido: 0.00 kWh");
    private final JLabel battLbl    = new JLabel("Bateria: 0 %");
    private final JLabel econLbl    = new JLabel("Economia: R$ 0.00");

    public SimpleView() {
        super("SmartGrid – Demo");

        setLayout(new GridLayout(5, 1));
        Font f1 = new Font("Arial", Font.BOLD, 16);
        tickLbl.setFont(f1);

        add(tickLbl); add(prodLbl); add(consLbl); add(battLbl); add(econLbl);

        setSize(320, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        SimulationManager.getInstance().addListener(this);
    }

    @Override
    public void onTick(long t, double prod, double cons, double soc, double econ) {
        tickLbl.setText("Tick: " + t);
        prodLbl.setText(String.format("Gerado: %.2f kWh", prod));
        consLbl.setText(String.format("Consumido: %.2f kWh", cons));
        battLbl.setText(String.format("Bateria: %.0f %%", soc * 100));
        econLbl.setText(String.format("Economia: R$ %.2f", econ));
    }
}
