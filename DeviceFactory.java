package br.pedro.smartgrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Factory Method + Flyweight.
 * Lê devices.csv, cria componentes e reaproveita DeviceSpecs idênticos.
 */
public class DeviceFactory {

    /** Flyweight imutável contendo especificações de um dispositivo. */
    public static final class DeviceSpecs {
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
        return CACHE.computeIfAbsent(key,
        ignored -> new DeviceSpecs(area, eff, cap));
    }

    /** Lê um arquivo CSV muito simples e devolve um Composite pronto. */
    public static CompositeEnergyUnit loadGridFromCsv(Path csvPath) throws IOException {
        CompositeEnergyUnit root = new CompositeEnergyUnit();
        List<String> lines = Files.readAllLines(csvPath);

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) continue; // comentário
            String[] parts = line.split(";");
            switch (parts[0].trim().toUpperCase()) {
                case "SOLAR" -> {
                    String model = parts[1].trim();
                    double area = Double.parseDouble(parts[2]);
                    double eff  = Double.parseDouble(parts[3]);
                    DeviceSpecs s = getOrCreate("SOLAR:" + model, area, eff, 0);
                    root.addComponent(new SolarPanel(s));
                }
                case "BATTERY" -> {
                    String model = parts[1].trim();
                    double cap   = Double.parseDouble(parts[2]);
                    DeviceSpecs s = getOrCreate("BAT:" + model, 0, 0, cap);
                    root.addComponent(new Battery(s));
                }
                default -> System.err.println("Linha ignorada: " + line);
            }
        }
        return root;
    }
}
