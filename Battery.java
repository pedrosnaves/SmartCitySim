package br.pedro.smartgrid;

// Classe folha do Composite, implementando Flyweight

public class Battery implements EnergyComponent { // Referência compartilhada a DeviceSpecs usando o Flyweight

    private final DeviceFactory.DeviceSpecs specs;
    private double stored;                 

    public Battery(DeviceFactory.DeviceSpecs specs) {
        this.specs = specs;
        this.stored = 0;
    }

    
    public double getCapacity()          { return specs.capacity; }
    public double getStateOfCharge()     { return stored / specs.capacity; }

   
    public double charge(double kWh) {
        double aceito = Math.min(kWh, specs.capacity - stored);
        stored += aceito;
        return aceito;
    }

    
    public double discharge(double kWh) {
    double fornecido = Math.min(kWh, stored);
    stored -= fornecido;
    if (fornecido > 0)
        SimulationManager.getInstance().addGeneration(fornecido); // energia fornecida pela bateria
    return fornecido;
}

  
   @Override
public double simulateTick(int tick, double irradiance) { // Implementação do método obrigatório de simulação
    return 0;
}

    @Override public double getStoredEnergy() {
        return stored; }
}
