package br.pedro.smartgrid;

// Classe folha do Composite, implementando EnergyComponent
public class SolarPanel implements EnergyComponent {

    private final DeviceFactory.DeviceSpecs specs;

    SolarPanel(DeviceFactory.DeviceSpecs specs) { this.specs = specs; }

    @Override
public double simulateTick(int tick, double irradiance) {
    double kWh = irradiance * specs.area * specs.efficiency;
    SimulationManager.getInstance().addGeneration(kWh);
    return kWh;
}

}
