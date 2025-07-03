package br.pedro.smartgrid;

/**
 * Raiz do padrão Composite. Todo componente da rede deve
 * conseguir simular um “tick” e informar energia armazenada/capacidade.
 */
public interface EnergyComponent {

    /**
     * Executa um passo da simulação.
     *
     * @param tick        instante atual (0, 1, 2…)
     * @param irradiance  irradiância solar em kW/m² disponível neste tick
     * @return energia líquida produzida (+) ou consumida (-) em kWh
     */
    double simulateTick(int tick, double irradiance);

    /** Energia armazenada no componente (kWh). 0 se o componente não armazena. */
    default double getStoredEnergy()          { return 0.0; }

    /** Capacidade máxima de armazenamento (kWh). 0 se não armazena. */
    default double getStorageCapacity()       { return 0.0; }
}
