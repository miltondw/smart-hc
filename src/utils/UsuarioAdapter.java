package utils;

import com.google.gson.*;
import domain.*;
import java.lang.reflect.Type;

public class UsuarioAdapter implements JsonSerializer<Usuario>, JsonDeserializer<Usuario> {

    @Override
    public JsonElement serialize(Usuario src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tipo", src.getTipo().name());

        // Serializar usando el tipo concreto
        if (src instanceof Medico) {
            jsonObject.add("data", context.serialize(src, Medico.class));
        } else if (src instanceof Paciente) {
            jsonObject.add("data", context.serialize(src, Paciente.class));
        }

        return jsonObject;
    }

    @Override
    public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String tipo = jsonObject.get("tipo").getAsString();
        JsonElement data = jsonObject.get("data");

        if ("MEDICO".equals(tipo)) {
            return context.deserialize(data, Medico.class);
        } else if ("PACIENTE".equals(tipo)) {
            return context.deserialize(data, Paciente.class);
        }

        throw new JsonParseException("Tipo de usuario desconocido: " + tipo);
    }
}
