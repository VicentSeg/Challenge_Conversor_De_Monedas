import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class consultaCambioApi {

    private String monedaUno;
    private String monedaDos;
    private double monto;

    public void setMonedaUno(String monedaUno) {
        this.monedaUno = monedaUno;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setMonedaDos(String monedaDos) {
        this.monedaDos = monedaDos;
    }

    public String getMonedaDos() {
        return monedaDos;
    }
    public String getMonedaUno() {
        return monedaUno;
    }
    public double getMonto() {
        return monto;
    }

    public tipoDeCambios buscaCambio() {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/91c90242a7b1a7a696740d07/pair/"+monedaUno+"/"+monedaDos+"/"+monto);
        System.out.println(direccion);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), tipoDeCambios.class);
        } catch (Exception e) {
            throw new RuntimeException("Moneda no encontrada!!");
        }
    }
}