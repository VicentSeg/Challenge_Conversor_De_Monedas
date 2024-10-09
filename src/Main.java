import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        boolean ciclo;
        consultaCambioApi ConsultaCambioApi = new consultaCambioApi();
        ConsultaCambioApi.setMonto(0);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    ******************************************************
                    Sea bienvenido/a al conversor de monedas
                    
                    1) Dólar =>> Peso argentino
                    2) Peso argentino =>> Dólar
                    3) Dólar =>> Real brasileño
                    4) Real brasileño =>> Dólar
                    5) Dólar =>> Peso colombiano
                    6) Peso colombiano =>> Dólar
                    7) Salir
                    Elija una opción válida:
                    ******************************************************""");

            do {
                ciclo = false;
                int conversion = scanner.nextInt();
                switch (conversion) {
                    case 1 -> {
                        ConsultaCambioApi.setMonedaUno("USD");
                        ConsultaCambioApi.setMonedaDos("ARS");
                    }
                    case 2 -> {
                        ConsultaCambioApi.setMonedaUno("ARS");
                        ConsultaCambioApi.setMonedaDos("USD");
                    }
                    case 3 -> {
                        ConsultaCambioApi.setMonedaUno("USD");
                        ConsultaCambioApi.setMonedaDos("BRL");
                    }
                    case 4 -> {
                        ConsultaCambioApi.setMonedaUno("BRL");
                        ConsultaCambioApi.setMonedaDos("USD");
                    }
                    case 5 -> {
                        ConsultaCambioApi.setMonedaUno("USD");
                        ConsultaCambioApi.setMonedaDos("COP");
                    }
                    case 6 -> {
                        ConsultaCambioApi.setMonedaUno("COP");
                        ConsultaCambioApi.setMonedaDos("USD");
                    }
                    case 7 -> {
                        System.out.println("Saliendo del programa.");
                        return;
                    }
                    default -> {
                        System.out.println("Opción inválida. Por favor, elija una opción válida.");
                        ciclo=true;
                    }
                }
            }while (ciclo);

            System.out.println("Estás convirtiendo " + ConsultaCambioApi.getMonedaUno() + " a " + ConsultaCambioApi.getMonedaDos());
            ConsultaCambioApi.setMonto(0);

            while (ConsultaCambioApi.getMonto() <= 0) {
                System.out.println("Ingrese el valor que deseas convertir:");
                ConsultaCambioApi.setMonto(scanner.nextDouble());

                if (ConsultaCambioApi.getMonto()  <= 0) {
                    System.out.println("Por favor, ingrese un monto mayor a 0");
                }
            }
            tipoDeCambios TipoDeCambios = ConsultaCambioApi.buscaCambio();
            System.out.println("*******************************************************************************");
            System.out.println("El valor de: " + ConsultaCambioApi.getMonto() + " ["+ ConsultaCambioApi.getMonedaUno()
                    +"]"+ " ==>>> corresponde al valor final de: " + TipoDeCambios.conversion_result() + " [" + ConsultaCambioApi.getMonedaDos() + "]" );

        }
    }
}