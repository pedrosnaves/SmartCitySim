package br.pedro.smartgrid;

import java.util.ArrayList;
import java.util.List;

public class CompositeEnergyUnit implements EnergyComponent {

    private final List<EnergyComponent> components = new ArrayList<>(); // Lista interna que armazena os filhos de Composite

    public void addComponent(EnergyComponent comp) { components.add(comp); }

    @Override
    public double simulateTick(int tick, double irradiance) { // Invoca o método nos filhos (polimorfismo) e soma a produção líquida
        double total = 0;
        for (EnergyComponent c : components) total += c.simulateTick(tick, irradiance);
        return total;
    }

    @Override public double getStoredEnergy() { // Calcula a energia armazenada total
        return components.stream().mapToDouble(EnergyComponent::getStoredEnergy).sum();
    }

   
    public List<EnergyComponent> getChildren() { // Permite so SimulationManager percorrer a árvore e encontrar os filhos. Rerotna a lista (Composite)
        return components;                
    }
}
