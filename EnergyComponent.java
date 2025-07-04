package br.pedro.smartgrid;

// Interface: funciona como componente raiz do Composite. Todos os dispositivos e nós derivam dela


public interface EnergyComponent {

    
    double simulateTick(int tick, double irradiance); // Métodos abstratos: delega a cada implementação a lógica de simulação de um tick

   
    default double getStoredEnergy() { // Energia armazenada
        return 0.0;
    }

   
    default double getStorageCapacity() { // Capacidade máxima de armazenamento
        return 0.0;
    }
}
