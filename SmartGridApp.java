package br.pedro.smartgrid;

import java.nio.file.Files;
import java.nio.file.Path;

public class SmartGridApp {

    public static void main(String[] args) {
        try { // tratamento de excessões
           
            Path csv = Path.of("devices.csv");

           
            if (!Files.exists(csv)) { // Se não encontrado, tenta novamente no diretório pai
                csv = Path.of(System.getProperty("user.dir"))
                          .getParent()          // sobe 1 diretório
                          .resolve("devices.csv");
            }

            System.out.println("CSV que vou usar: " + csv.toAbsolutePath()); // Linha para imprimir o diretório absoluto do arquivo csv, para ser encontrado manualmente se necessário

          
            CompositeEnergyUnit grid = DeviceFactory.loadGridFromCsv(csv); // Carrega a estrutura Composite completa atravéz do Factory Method
            SimulationManager manager = SimulationManager.getInstance(); // Inicializa a simulação (Singleton), configurando-a com a árvore recém carregada
            manager.init(grid);

            new SimpleView();          // Cria a GUI através do Simpleview, mostrando a aplicação funcionando visualmente
            new TickerThread().start(); // Inicia a thread que controla a passagem de tempo na simulação
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
