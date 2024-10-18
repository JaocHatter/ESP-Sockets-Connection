#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "jaoc";
const char* pswd = "********";
const char* serverUrl = "http://192.168.XX.XX:8080"; // Server ip

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, pswd);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000); // Esperar 1 segundo
    Serial.println("Intentando conexión a WiFi...");
  }
  Serial.println("¡Conexión WiFi exitosa!");
}

float generarTemperatura() {
  return random(20, 30); // Generar un valor aleatorio
}

void loop() {
  enviarDatos();
  delay(3000); // Esperar 3 segundos
}

void enviarDatos() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;

    // Conectamos con el servidor
    http.begin(serverUrl);  
    // Especificamos el tipo de contenido
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");

    // Generamos la temperatura simulada y formateamos los datos
    float temp = generarTemperatura();
      // Realizar la solicitud POST 
    String datos = "temp= "+String(temp); 
    int httpResponseCode = http.POST(datos);

    // Verificar si la solicitud fue exitosa  
    if (httpResponseCode > 0) {
      String respuesta = http.getString();
      Serial.println("Respuesta del servidor: ");
      Serial.println(respuesta);
    } else {
      Serial.print("Error en la solicitud HTTP: ");
      Serial.println(httpResponseCode);
    }

    // Finalizamos la conexión
    http.end();
  } else {
    Serial.println("Conexión WiFi perdida");
  }
}

