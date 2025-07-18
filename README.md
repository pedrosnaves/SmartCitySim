# Projeto Final POO - Smart Grid

Alunos:
- Pedro Santana Naves
- Pedro Antônio Garcia Gonçalves

## Descrição do Projeto

O **Smart Grid** é um projeto em Java que simula uma microrrede elétrica inteligente, como uma casa que gera, armazena e gerencia energia renovável. Ele modela painéis solares que produzem energia com base na luz solar, baterias que armazenam o excedente, e um sistema que equilibra produção e consumo para economizar energia e reduzir custos. A interface gráfica exibe em tempo real a energia gerada, consumida, armazenada e a economia em reais. O projeto usa padrões de projeto como Composite, Factory Method, Flyweight, Singleton e Observer, sendo uma base para entender soluções energéticas em Smart Cities.

## Relação com Cidades Inteligentes

O *Smart Grid* está diretamente ligado ao conceito de *Smart Cities*, cidades que usam tecnologia para serem mais sustentáveis e eficientes. O projeto simula uma microrrede residencial, que é um pilar essencial para cidades inteligentes, pois permite que casas gerem, armazenem e gerenciem sua própria energia renovável, reduzindo a dependência de redes elétricas tradicionais e o impacto ambiental. 

### Escopo do Projeto
O simulador foca na *gestão energética de uma unidade residencial*, modelando:
- *Produção de energia*: Painéis solares geram eletricidade com base na irradiância solar.
- *Armazenamento*: Baterias guardam o excedente para uso posterior.
- *Consumo e equilíbrio*: O sistema decide quando usar energia armazenada ou da rede, otimizando custos.

Essa abordagem representa uma célula básica de uma cidade inteligente, servindo como base para estudos de expansão urbana, onde múltiplas microrredes podem ser integradas para criar redes energéticas mais amplas. O projeto é uma ferramenta educacional para entender como equilibrar energia limpa em cenários residenciais, com potencial para inspirar soluções em larga escala.

## Como Rodar o Projeto

### Pré-requisitos
- **Java Development Kit (JDK)**: Versão 8 ou superior instalada.
- **Arquivo devices.csv**: Contém configurações dos dispositivos no formato:
  ```
  SOLAR;Modelo;area_m2;eficiencia
  BATTERY;Modelo;capacidade_kWh
  ```
  Exemplo:
  ```
  SOLAR;PainelX;10;0.2
  BATTERY;BateriaY;5
  ```
  Coloque o arquivo no diretório de execução ou na raiz do projeto.

### Passos para Compilar e Executar

1. **Organize os arquivos**:
   - Certifique-se de que todos os arquivos `.java` (`SmartGridApp.java`, `EnergyComponent.java`, `CompositeEnergyUnit.java`, `Battery.java`, `TickerThread.java`, `SimulationManager.java`, `SimpleView.java`, `DeviceFactory.java`, `SolarPanel.java`) estão no diretório `br/pedro/smartgrid/` dentro do diretório do projeto.

2. **Compile os arquivos**:
   - Abra um terminal no diretório do projeto.
   - Execute o comando para compilar todos os arquivos `.java` em `.class`:
     ```bash
     javac br/pedro/smartgrid/*.java
     ```
   - Isso gera os arquivos `.class` no mesmo diretório.

3. **Execute o programa principal**:
   - Certifique-se de que o arquivo `devices.csv` está no diretório de execução ou na raiz do projeto.
   - Execute o programa com o comando:
     ```bash
     java -cp . br.pedro.smartgrid.SmartGridApp
     ```
   - O programa iniciará a simulação, abrirá uma interface gráfica e atualizará os dados a cada segundo.

4. **Testes (opcional)**:
   - Para executar os testes da bateria, compile e rode com asserções habilitadas:
     ```bash
     javac br/pedro/smartgrid/BatteryTest.java
     java -ea -cp . br.pedro.smartgrid.BatteryTest
     ```

### Notas
- Caso o `devices.csv` não seja encontrado, o programa exibirá uma mensagem de erro com instruções sobre o formato do arquivo.
- A simulação usa uma thread (`TickerThread`) para atualizar a cada segundo e uma interface gráfica (`SimpleView`) para exibir os resultados.
- Para parar a simulação, feche a janela gráfica.
