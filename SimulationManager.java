package br.pedro.smartgrid;

import java.util.ArrayList;
import java.util.List;



public class SimulationManager {

    // Listener
    public interface Listener {
        void onTick(long tick, double producedKWh, double boughtKWh,
                     double socPercent, double economyR$);
    }

    // Singleton
    private static final SimulationManager INSTANCE = new SimulationManager();
    public static SimulationManager getInstance() { return INSTANCE; }
    private SimulationManager() {}

     @SuppressWarnings("unused")
    private CompositeEnergyUnit root;
    private Battery             battery;
    private final List<SolarPanel> panels = new ArrayList<>();

    
    private double producedTick;
    private double consumedTick;
    private double economyR$;
    private long   tick;

    private final List<Listener> listeners = new ArrayList<>();

    
    private static final double PRECO_KWH        = 0.80; // custo kWh comprado
    private static final double FEED_IN_TARIFF   = 0.20; // valor por kWh exportado
    private static final double CONSUMO_FIXO_KWH = 6.67; // consumo diário de 1 residência

    // Inicialização
    public void init(CompositeEnergyUnit root) {
        this.root    = root;
        this.battery = findBattery(root);
        panels.clear();
        collectPanels(root);
    }

    private Battery findBattery(EnergyComponent e) {
        if (e instanceof Battery b) return b;
        if (e instanceof CompositeEnergyUnit ceu) {
            for (EnergyComponent child : ceu.getChildren()) {
                Battery found = findBattery(child);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void collectPanels(EnergyComponent e) {
        if (e instanceof SolarPanel) {
            panels.add((SolarPanel) e);
        } else if (e instanceof CompositeEnergyUnit ceu) {
            for (EnergyComponent child : ceu.getChildren()) collectPanels(child);
        }
    }

    
    public synchronized void addGeneration(double kWh)  { producedTick += kWh; }
    public synchronized void addConsumption(double kWh) { consumedTick += kWh; }

    // Simulação do tick
    public void simulateTick() {
        int nPanels = panels.size();
        if (nPanels == 0) {
            notifyListeners(tick++, 0, CONSUMO_FIXO_KWH,
                            battery != null ? battery.getStateOfCharge() : 0,
                            economyR$);
            return;
        }

        
        double baseProd  = 8.0 + 5.0 * Math.sin(tick * 2 * Math.PI / 30); // kWh por painel
        double totalProd = baseProd * nPanels;

        
        double consumedKWh = CONSUMO_FIXO_KWH * nPanels;

        
        double deficit = consumedKWh - totalProd; // >0 falta; <0 sobra

        if (deficit > 0) {
            
            double fromBatt = (battery != null) ? battery.discharge(deficit) : 0;
            consumedKWh -= fromBatt;             // o que ainda será comprado
            economyR$   += fromBatt * PRECO_KWH; // economia via bateria
        } else {
           
            double surplus = -deficit;
            if (battery != null) {
                double before = battery.getStateOfCharge() * battery.getCapacity();
                battery.charge(surplus);
                double after  = battery.getStateOfCharge() * battery.getCapacity();
                surplus -= (after - before);     // excedente não armazenado
            }
            if (surplus > 0) economyR$ += surplus * FEED_IN_TARIFF;
        }

        
        producedTick = totalProd;
        consumedTick = consumedKWh;
        double socPercent = (battery != null) ? battery.getStateOfCharge() : 0;

        notifyListeners(tick++, producedTick, consumedTick, socPercent, economyR$);
    }

    // Listeners
    public void addListener(Listener l) { listeners.add(l); }

    private void notifyListeners(long t, double p, double c,
                                 double soc, double e) {
        for (Listener l : listeners) l.onTick(t, p, c, soc, e);
    }
}
