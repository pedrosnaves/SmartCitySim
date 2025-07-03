package br.pedro.smartgrid;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager {

    /* ---------- Listener ---------- */
    public interface Listener {
        void onTick(long t, double prod, double cons, double soc, double economy);
    }

    /* ---------- Singleton ---------- */
    private static final SimulationManager INSTANCE = new SimulationManager();
    public static SimulationManager getInstance() { return INSTANCE; }
    private SimulationManager() {}

    @SuppressWarnings("unused")
    private CompositeEnergyUnit root;
    private Battery battery;                       // assume 1 única
    private double producedTick, consumedTick;     // valores do tick atual
    private double economyR$;                      // acumulado
    private long tick;
    private final List<Listener> listeners = new ArrayList<>();

    /* ---------- Parâmetros ---------- */
    
    private static final double PRECO_KWH = 0.80;
       

    /* ---------- Inicialização ---------- */
    public void init(CompositeEnergyUnit r) {
        this.root = r;
        this.battery = findBattery(r);
    }
    private Battery findBattery(EnergyComponent e) {
        if (e instanceof Battery b) return b;
        if (e instanceof CompositeEnergyUnit ceu)
            for (EnergyComponent c : ceu.getChildren()) {
                Battery b = findBattery(c);
                if (b != null) return b;
            }
        return null;
    }

    /* ---------- APIs que as folhas chamam ---------- */
    synchronized void addGeneration(double kWh)  { producedTick += kWh; }
    synchronized void addConsumption(double kWh) { consumedTick += kWh; }

    /* ---------- Tick principal ---------- */
    public void simulateTick() {

    /* ===== 1. Produção e consumo do “dia” ===== */
    double producedKWh = 3.0 + 1.5 * Math.sin(tick * 2 * Math.PI / 30);  
    double consumedKWh  = 4.0;                    // fixo
    producedTick  = producedKWh;
    consumedTick  = consumedKWh;

    /* ===== 2. Balanço com a bateria ===== */
    double deficit = consumedKWh - producedKWh;   // >0 falta; <0 sobra

    if (deficit > 0) {                            // faltou energia
        double suprido = battery != null ? battery.discharge(deficit) : 0;
        consumedTick  -= suprido;                 // o que a bateria cobriu
        economyR$     += suprido * PRECO_KWH;     // economia no dia
    } else if (deficit < 0) {                     // sobrou energia
        if (battery != null) battery.charge(-deficit);
    }

    /* ===== 3. Atualiza GUI ===== */
    double soc = battery != null ? battery.getStateOfCharge() : 0;
    notifyListeners(tick, producedTick, consumedTick, soc, economyR$);
    tick++;                                       // próximo dia
}

    /* ---------- Observer infra ---------- */
    public void addListener(Listener l) { listeners.add(l); }
    private void notifyListeners(long t,double p,double c,double soc,double e){
        for (Listener l: listeners) l.onTick(t,p,c,soc,e);
    }
}
