package br.pedro.smartgrid;

public class Battery implements EnergyComponent {

    private final DeviceFactory.DeviceSpecs specs;
    private double stored;                 // kWh

    public Battery(DeviceFactory.DeviceSpecs specs) {
        this.specs = specs;
        this.stored = 0;
    }

    /* ================= UTILITÁRIOS =================== */
    public double getCapacity()          { return specs.capacity; }
    public double getStateOfCharge()     { return stored / specs.capacity; }

    /** carrega e devolve quanto realmente aceitou */
    public double charge(double kWh) {
        double aceito = Math.min(kWh, specs.capacity - stored);
        stored += aceito;
        return aceito;
    }

    /** descarrega e devolve quanto realmente forneceu */
    public double discharge(double kWh) {
    double fornecido = Math.min(kWh, stored);
    stored -= fornecido;
    if (fornecido > 0)
        SimulationManager.getInstance().addGeneration(fornecido); // energia fornecida pela bateria
    return fornecido;
}

    /* ============== IMPLEMENTAÇÃO DO COMPONENT ============== */
   @Override
public double simulateTick(int tick, double irradiance) { return 0; }

    @Override public double getStoredEnergy()                          { return stored; }
}
