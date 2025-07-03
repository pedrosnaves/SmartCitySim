package br.pedro.smartgrid;

import java.nio.file.Files;
import java.nio.file.Path;

public class SmartGridApp {

    public static void main(String[] args) {
        try {
            // ① procura devices.csv no diretório de execução (CWD)
            Path csv = Path.of("devices.csv");

            // ② se não achar, tenta um nível acima (raiz do projeto)
            if (!Files.exists(csv)) {
                csv = Path.of(System.getProperty("user.dir"))
                          .getParent()          // sobe 1 diretório
                          .resolve("devices.csv");
            }

            System.out.println("CSV que vou usar: " + csv.toAbsolutePath());

            /* ---------------------------------------------------------------- */
            CompositeEnergyUnit grid = DeviceFactory.loadGridFromCsv(csv);
            SimulationManager manager = SimulationManager.getInstance();
            manager.init(grid);

            new SimpleView();          // GUI
            new TickerThread().start(); // relógio em thread separada
            /* ---------------------------------------------------------------- */

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("""
                Erro ao iniciar a simulação.
                Coloque devices.csv no diretório de execução OU na raiz do projeto.
                Formato:
                SOLAR;Modelo;area_m2;eficiencia
                BATTERY;Modelo;capacidade_kWh
            """);
        }
    }
}
