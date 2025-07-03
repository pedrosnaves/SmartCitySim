package br.pedro.smartgrid;

/**
 * Teste rápido usando asserções nativas do Java (`assert`).
 * Rode com:  java -ea br.pedro.smartgrid.BatteryTest
 */
public class BatteryTest {

    private static Battery novaBateria(double capacidadeKWh) {
        return new Battery(new DeviceFactory.DeviceSpecs(0, 0, capacidadeKWh));
    }

    public static void main(String[] args) {
        /* 1. Não ultrapassa capacidade */
        {
            Battery bat = novaBateria(3.0);
            double aceito = bat.charge(5.0);
            assert aceito == 3.0 : "carregou a mais!";
            assert bat.getStoredEnergy() == 3.0 : "SOC errado após carga";
        }

        /* 2. Não fornece mais do que tem */
        {
            Battery bat = novaBateria(3.0);
            bat.charge(2.0);
            double dado = bat.discharge(5.0);
            assert dado == 2.0 : "forneceu energia inexistente!";
            assert bat.getStoredEnergy() == 0.0 : "SOC deveria ser 0";
        }

        /* 3. SOC reportado corretamente */
        {
            Battery bat = novaBateria(4.0);
            bat.charge(1.0);                   // 25 %
            assert Math.abs(bat.getStateOfCharge() - 0.25) < 1e-6;
            bat.charge(1.0);                   // 50 %
            assert Math.abs(bat.getStateOfCharge() - 0.50) < 1e-6;
        }

        System.out.println("✓ Todos os testes da Battery passaram!");
    }
}
