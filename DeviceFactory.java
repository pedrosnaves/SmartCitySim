package br.pedro.smartgrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// Nesta classe, faremos o uso em combinação dos métodos Factory Method, Flyweight e Composite


public class DeviceFactory {

    
    public static final class DeviceSpecs { // Flyweight imutável contendo especificações dos dispositivos
        final double area;          // m² (apenas painéis)
        final double efficiency;    // [0-1] (apenas painéis)
        final double capacity;      // kWh   (apenas baterias)

        DeviceSpecs(double area, double efficiency, double capacity) {
            this.area = area; this.efficiency = efficiency; this.capacity = capacity;
        }
    }

    private static final Map<String, DeviceSpecs> CACHE = new HashMap<>();
    
    @SuppressWarnings("unused")
    private static DeviceSpecs getOrCreate(String key, double area,
                                           double eff, double cap) {
        return CACHE.computeIfAbsent(key,                // Cria uma única instância por chave - core do Flyweight
        ignored -> new DeviceSpecs(area, eff, cap));
    }

    // Factory Method -> decodifica um arquivo csv e devolve o Composite pronto
    public static CompositeEnergyUnit loadGridFromCsv(Path csvPath) throws IOException {
        CompositeEnergyUnit root = new CompositeEnergyUnit(); // Nó-raiz do Composite
        List<String> lines = Files.readAllLines(csvPath); // Uso dos métodos de leitura de arquivos (I/O)

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) continue; 
            String[] parts = line.split(";");
            switch (parts[0].trim().toUpperCase()) {
                case "SOLAR" -> {   // Constrói SolarPanel usando Flyweight specs e adiciona ao Composite
                    String model = parts[1].trim();
                    double area = Double.parseDouble(parts[2]);
                    double eff  = Double.parseDouble(parts[3]);
                    DeviceSpecs s = getOrCreate("SOLAR:" + model, area, eff, 0);
                    root.addComponent(new SolarPanel(s));
                }
                case "BATTERY" -> {  // Constrói Battery usando Flyweight specs e adiciona ao Composite
                    String model = parts[1].trim();
                    double cap   = Double.parseDouble(parts[2]);
                    DeviceSpecs s = getOrCreate("BAT:" + model, 0, 0, cap);
                    root.addComponent(new Battery(s));
                }
                default -> System.err.println("Linha ignorada: " + line);
            }
        }
        return root; // Retorna árvore totalmente populada
    }
}
