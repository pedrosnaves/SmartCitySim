package br.pedro.smartgrid;

import java.util.ArrayList;
import java.util.List;

public class CompositeEnergyUnit implements EnergyComponent {

    private final List<EnergyComponent> components = new ArrayList<>();

    public void addComponent(EnergyComponent comp) { components.add(comp); }

    @Override
    public double simulateTick(int tick, double irradiance) {
        double total = 0;
        for (EnergyComponent c : components) total += c.simulateTick(tick, irradiance);
        return total;
    }

    @Override public double getStoredEnergy() {
        return components.stream().mapToDouble(EnergyComponent::getStoredEnergy).sum();
    }

    /* >>>>>>  NOVO MÉTODO  — usado pelo SimulationManager  <<<<<< */
    public List<EnergyComponent> getChildren() {
        return components;                // se preferir, Collections.unmodifiableList(components)
    }
}
