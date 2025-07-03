package br.pedro.smartgrid;

// Classe de testes para a bateria

public class BatteryTest {

    private static Battery novaBateria(double capacidadeKWh) {
        return new Battery(new DeviceFactory.DeviceSpecs(0, 0, capacidadeKWh));
    }

    public static void main(String[] args) {
       
        {
            Battery bat = novaBateria(3.0);
            double aceito = bat.charge(5.0);
            assert aceito == 3.0 : "carregou a mais!";
            assert bat.getStoredEnergy() == 3.0 : "SOC errado após carga";
        }

        
        {
            Battery bat = novaBateria(3.0);
            bat.charge(2.0);
            double dado = bat.discharge(5.0);
            assert dado == 2.0 : "forneceu energia inexistente!";
            assert bat.getStoredEnergy() == 0.0 : "SOC deveria ser 0";
        }

       
        {
            Battery bat = novaBateria(4.0);
            bat.charge(1.0);                   
            assert Math.abs(bat.getStateOfCharge() - 0.25) < 1e-6;
            bat.charge(1.0);                  
            assert Math.abs(bat.getStateOfCharge() - 0.50) < 1e-6;
        }

        System.out.println("✓ Todos os testes da Battery passaram!");
    }
}
