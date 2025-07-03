package br.pedro.smartgrid;

/**
 * Folha do Composite: gera energia a partir da irradiância solar.
 * Compartilha suas especificações via Flyweight (DeviceFactory.DeviceSpecs).
 */
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
