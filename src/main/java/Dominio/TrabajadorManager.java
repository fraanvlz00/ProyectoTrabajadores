package Dominio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class TrabajadorManager {
    private JsonNode rootNodeTrabajador;
    private List<Isapre> isapres;
    private List<AFP> afps;
    private ObjectMapper objectMapper;
    private String trabajadorPath;

    // Constructor para leer los archivos JSON
    public TrabajadorManager(String trabajadorPath, String isaprePath, String afpPath) throws IOException {
        this.trabajadorPath = trabajadorPath;
        objectMapper = new ObjectMapper();
        rootNodeTrabajador = objectMapper.readTree(new File(trabajadorPath));
        isapres = cargarIsapres(isaprePath, objectMapper);
        afps = cargarAfps(afpPath, objectMapper);
    }

    // Método para cargar Isapres desde el JSON
    private List<Isapre> cargarIsapres(String isaprePath, ObjectMapper objectMapper) throws IOException {
        List<Isapre> isapreList = new ArrayList<>();
        JsonNode isapresNode = objectMapper.readTree(new File(isaprePath));
        Iterator<JsonNode> elements = isapresNode.elements();
        while (elements.hasNext()) {
            JsonNode isapreNode = elements.next();
            Isapre isapre = new Isapre(
                    isapreNode.path("nombre").asText(),
                    isapreNode.path("plan").asText()
            );
            isapreList.add(isapre);
        }
        return isapreList;
    }

    // Método para cargar Afps desde el JSON
    private List<AFP> cargarAfps(String afpPath, ObjectMapper objectMapper) throws IOException {
        List<AFP> afpList = new ArrayList<>();
        JsonNode afpsNode = objectMapper.readTree(new File(afpPath));
        Iterator<JsonNode> elements = afpsNode.elements();
        while (elements.hasNext()) {
            JsonNode afpNode = elements.next();
            AFP afp = new AFP(
                    afpNode.path("nombre").asText(),
                    afpNode.path("tipo").asText()
            );
            afpList.add(afp);
        }
        return afpList;
    }

    // Método para buscar un trabajador por nombre
    public Trabajador buscarTrabajadorPorNombre(String nombre) {
        JsonNode trabajadoresNode = rootNodeTrabajador.path("trabajador");
        Iterator<JsonNode> elements = trabajadoresNode.elements();
        while (elements.hasNext()) {
            JsonNode trabajadorNode = elements.next();
            if (trabajadorNode.path("nombre").asText().equals(nombre)) {
                // Crear y devolver el objeto Trabajador
                Trabajador trabajador = new Trabajador(
                        trabajadorNode.path("nombre").asText(),
                        trabajadorNode.path("apellido").asText(),
                        trabajadorNode.path("rut").asText()
                );

                // Mapear objeto Isapre
                JsonNode isapreNode = trabajadorNode.path("isapre");
                if (!isapreNode.isEmpty()) {
                    Isapre isapre = new Isapre(
                            isapreNode.path("nombre").asText(),
                            isapreNode.path("plan").asText()
                    );
                    trabajador.setIsapre(isapre);
                }

                // Mapear objeto Afp
                JsonNode afpNode = trabajadorNode.path("afp");
                if (!afpNode.isEmpty()) {
                    AFP afp = new AFP(
                            afpNode.path("nombre").asText(),
                            afpNode.path("tipo").asText()
                    );
                    trabajador.setAfp(afp);
                }

                return trabajador;
            }
        }
        return null; // Si no se encuentra ningún trabajador con el nombre buscado
    }

    // Método para asignar Isapre y Afp a un trabajador y actualizar el JSON
    public void asignarIsapreYAfp(String nombre) {
        Trabajador trabajador = buscarTrabajadorPorNombre(nombre);
        if (trabajador == null) {
            System.out.println("Trabajador no encontrado.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        if (trabajador.getIsapre() == null) {
            System.out.println("Seleccione una Isapre:");
            for (int i = 0; i < isapres.size(); i++) {
                System.out.println((i + 1) + ". " + isapres.get(i));
            }
            int opcionIsapre = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (opcionIsapre > 0 && opcionIsapre <= isapres.size()) {
                trabajador.setIsapre(isapres.get(opcionIsapre - 1));
            } else {
                System.out.println("Opción de Isapre no válida.");
            }
        } else {
            System.out.println("El trabajador ya tiene una Isapre asignada: " + trabajador.getIsapre());
        }

        if (trabajador.getAfp() == null) {
            System.out.println("Seleccione una Afp:");
            for (int i = 0; i < afps.size(); i++) {
                System.out.println((i + 1) + ". " + afps.get(i));
            }
            int opcionAfp = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            if (opcionAfp > 0 && opcionAfp <= afps.size()) {
                trabajador.setAfp(afps.get(opcionAfp - 1));
            } else {
                System.out.println("Opción de Afp no válida.");
            }
        } else {
            System.out.println("El trabajador ya tiene una Afp asignada: " + trabajador.getAfp());
        }

        // Actualizar el JSON
        actualizarTrabajadorEnJson(trabajador);
    }

    // Método para actualizar los datos del trabajador en el JSON
    private void actualizarTrabajadorEnJson(Trabajador trabajador) {
        JsonNode trabajadoresNode = rootNodeTrabajador.path("trabajador");
        for (JsonNode trabajadorNode : trabajadoresNode) {
            if (trabajadorNode.path("nombre").asText().equals(trabajador.getNombre())) {
                ((ObjectNode) trabajadorNode).putObject("isapre")
                        .put("nombre", trabajador.getIsapre().getNombre())
                        .put("plan", trabajador.getIsapre().getPlan());
                ((ObjectNode) trabajadorNode).putObject("afp")
                        .put("nombre", trabajador.getAfp().getNombre())
                        .put("tipo", trabajador.getAfp().getTipo());
                break;
            }
        }
        // Guardar los cambios en el archivo JSON
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(trabajadorPath), rootNodeTrabajador);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar todos los datos de un trabajador
    public void mostrarDatosTrabajador(String nombre) {
        Trabajador trabajador = buscarTrabajadorPorNombre(nombre);
        if (trabajador != null) {
            System.out.println("Datos del Trabajador:");
            System.out.println("Nombre: " + trabajador.getNombre());
            System.out.println("Apellido: " + trabajador.getApellido());
            System.out.println("RUT: " + trabajador.getRut());
            System.out.println("Isapre: " + (trabajador.getIsapre() != null ? trabajador.getIsapre() : "No asignado"));
            System.out.println("Afp: " + (trabajador.getAfp() != null ? trabajador.getAfp() : "No asignado"));
        } else {
            System.out.println("Trabajador no encontrado.");
        }
    }

    // Método para mostrar el menú
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Mostrar Datos del Trabajador");
            System.out.println("2. Asignar Isapre y Afp");
            System.out.println("3. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre del trabajador:");
                    String nombre = scanner.nextLine();
                    mostrarDatosTrabajador(nombre);
                    break;
                case 2:
                    System.out.println("Ingrese el nombre del trabajador:");
                    nombre = scanner.nextLine();
                    asignarIsapreYAfp(nombre);
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
}
