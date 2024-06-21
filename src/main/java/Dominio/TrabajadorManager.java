package Dominio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrabajadorManager {
    private JsonNode rootNodeTrabajador;
    private List<Isapre> isapres;
    private List<AFP> afps;
    private ObjectMapper objectMapper;
    private String trabajadorPath;

    public TrabajadorManager(String trabajadorPath, String isaprePath, String afpPath) throws IOException {
        this.trabajadorPath = trabajadorPath;
        objectMapper = new ObjectMapper();
        rootNodeTrabajador = objectMapper.readTree(new File(trabajadorPath));
        isapres = cargarIsapres(isaprePath, objectMapper);
        afps = cargarAfps(afpPath, objectMapper);
    }

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

    public Trabajador buscarTrabajadorPorNombre(String nombre) {
        JsonNode trabajadoresNode = rootNodeTrabajador.path("trabajador");
        Iterator<JsonNode> elements = trabajadoresNode.elements();
        while (elements.hasNext()) {
            JsonNode trabajadorNode = elements.next();
            if (trabajadorNode.path("nombre").asText().equals(nombre)) {
                Trabajador trabajador = new Trabajador(
                        trabajadorNode.path("nombre").asText(),
                        trabajadorNode.path("apellido").asText(),
                        trabajadorNode.path("rut").asText()
                );

                JsonNode isapreNode = trabajadorNode.path("isapre");
                if (!isapreNode.isEmpty()) {
                    Isapre isapre = new Isapre(
                            isapreNode.path("nombre").asText(),
                            isapreNode.path("plan").asText()
                    );
                    trabajador.setIsapre(isapre);
                }

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
        return null;
    }

    public void asignarIsapreYAFP(Trabajador trabajador, String isapreNombre, String afpNombre) {
        for (Isapre isapre : isapres) {
            if (isapre.getNombre().equals(isapreNombre)) {
                trabajador.setIsapre(isapre);
                break;
            }
        }

        for (AFP afp : afps) {
            if (afp.getNombre().equals(afpNombre)) {
                trabajador.setAfp(afp);
                break;
            }
        }

        actualizarTrabajadorEnJson(trabajador);
    }

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
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(trabajadorPath), rootNodeTrabajador);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getIsapreNames() {
        List<String> nombres = new ArrayList<>();
        for (Isapre isapre : isapres) {
            nombres.add(isapre.getNombre());
        }
        return nombres;
    }

    public List<String> getAfpNames() {
        List<String> nombres = new ArrayList<>();
        for (AFP afp : afps) {
            nombres.add(afp.getNombre());
        }
        return nombres;
    }
}

