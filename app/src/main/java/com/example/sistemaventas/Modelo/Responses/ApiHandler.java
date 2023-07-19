package com.example.sistemaventas.Modelo.Responses;

import android.os.AsyncTask;
import android.util.Log;

import com.example.sistemaventas.Modelo.Entidades.Cliente;
import com.example.sistemaventas.Modelo.Entidades.DetalleVenta;
import com.example.sistemaventas.Modelo.Entidades.Factura;
import com.example.sistemaventas.Modelo.Entidades.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ApiHandler {

    //Listener para post
    public interface OnPostDataListener {
        void onPostDataSuccess(String response);

        void onPostDataError(IOException e);
    }

    //Listener para delete
    public interface OnDeleteDataListener {
        void onDeleteDataSuccess(String response);

        void onDeleteDataError(IOException e);
    }

    //Listener para PUT
    public interface OnUpdateDataListener {
        void onUpdateDataSuccess(String response);

        void onUpdateDataError(IOException e);
    }

    //GET-S
    //Clientes
    public static String getClientes(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class getClientesTask extends AsyncTask<String, Void, List<Cliente>> {

        List<Cliente> personList = new ArrayList<>();

        @Override
        public List<Cliente> doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.getClientes(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONArray dataArray = responseJson.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);

                    String contrasenia = jsonObject.getString("contraseña");
                    String telefono = jsonObject.getString("telefono");
                    String apellido = jsonObject.getString("apellido");
                    String nombre = jsonObject.getString("nombre");
                    String direccion = jsonObject.getString("direccion");
                    boolean activo = Boolean.parseBoolean(jsonObject.getString("activo"));
                    String cedula = jsonObject.getString("cedula");
                    String correo = jsonObject.getString("correo");
                    int codigoCliente = Integer.parseInt(jsonObject.getString("codigoCliente"));
                    String rol = jsonObject.getString("rol");

                    Cliente cl = new Cliente(
                            codigoCliente, nombre, apellido, activo,
                            direccion, telefono, cedula, rol,
                            correo, contrasenia);
                    personList.add(cl);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    public static class getClientesPorID extends AsyncTask<String, Void, Cliente> {

        Cliente personList;

        @Override
        public Cliente doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.getClientesActivos(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONObject data = responseJson.getJSONObject("data");

                String contrasenia = data.getString("contraseña");
                String telefono = data.getString("telefono");
                String apellido = data.getString("apellido");
                String nombre = data.getString("nombre");
                String direccion = data.getString("direccion");
                boolean activo = Boolean.parseBoolean(data.getString("activo"));
                String cedula = data.getString("cedula");
                String correo = data.getString("correo");
                int codigoCliente = Integer.parseInt(data.getString("codigoCliente"));
                String rol = data.getString("rol");

                Cliente cl = new Cliente(
                        codigoCliente, nombre, apellido, activo,
                        direccion, telefono, cedula, rol,
                        correo, contrasenia);
                personList = cl;

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    public static String getClientesActivos(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class getClientesActivosTask extends AsyncTask<String, Void, List<Cliente>> {

        List<Cliente> personList = new ArrayList<>();

        @Override
        public List<Cliente> doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.getClientesActivos(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONArray dataArray = responseJson.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);


                    String contrasenia = jsonObject.getString("contraseña");
                    String telefono = jsonObject.getString("telefono");
                    String apellido = jsonObject.getString("apellido");
                    String nombre = jsonObject.getString("nombre");
                    String direccion = jsonObject.getString("direccion");
                    boolean activo = Boolean.parseBoolean(jsonObject.getString("activo"));
                    String cedula = jsonObject.getString("cedula");
                    String correo = jsonObject.getString("correo");
                    int codigoCliente = Integer.parseInt(jsonObject.getString("codigoCliente"));
                    String rol = jsonObject.getString("rol");

                    Cliente cl = new Cliente(
                            codigoCliente, nombre, apellido, activo,
                            direccion, telefono, cedula, rol,
                            correo, contrasenia);
                    personList.add(cl);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    //Productos
    public static String GetProductos(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class getProductoPorID extends AsyncTask<String, Void, Producto> {

        Producto producto;

        @Override
        public Producto doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.getClientesActivos(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONObject data = responseJson.getJSONObject("data");

                int stock = Integer.parseInt(data.getString("stock"));
                int idProducto = Integer.parseInt(data.getString("idProducto"));
                String nombre = data.getString("nombre");
                double precio = Double.parseDouble(data.getString("precio"));
                boolean esActivo = Boolean.parseBoolean(data.getString("esActivo"));
                String fechaActivo = data.getString("fechaRegistro");

                Producto p = new Producto(stock, idProducto, nombre, precio, esActivo, fechaActivo);
                producto = p;

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return producto;
        }

        @Override
        protected void onPostExecute(Producto producto) {
            super.onPostExecute(producto);
        }
    }

    public static class GetProductosTask extends AsyncTask<String, Void, List<Producto>> {

        List<Producto> personList = new ArrayList<>();

        @Override
        public List<Producto> doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.GetProductos(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONArray dataArray = responseJson.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);

                    int stock = Integer.parseInt(jsonObject.getString("stock"));
                    int idProducto = Integer.parseInt(jsonObject.getString("idProducto"));
                    String nombre = jsonObject.getString("nombre");
                    double precio = Double.parseDouble(jsonObject.getString("precio"));
                    boolean esActivo = Boolean.parseBoolean(jsonObject.getString("esActivo"));
                    String fechaActivo = jsonObject.getString("fechaRegistro");

                    Producto cl = new Producto(
                            stock, idProducto, nombre, precio, esActivo, fechaActivo
                    );
                    personList.add(cl);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    public static String GetProductosActivos(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class GetProductosActivosTask extends AsyncTask<String, Void, List<Producto>> {

        List<Producto> personList = new ArrayList<>();

        @Override
        public List<Producto> doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.GetProductosActivos(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONArray dataArray = responseJson.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);

                    int stock = Integer.parseInt(jsonObject.getString("stock"));
                    int idProducto = Integer.parseInt(jsonObject.getString("idProducto"));
                    String nombre = jsonObject.getString("nombre");
                    double precio = Double.parseDouble(jsonObject.getString("precio"));
                    boolean esActivo = Boolean.parseBoolean(jsonObject.getString("esActivo"));
                    String fechaActivo = jsonObject.getString("fechaRegistro");

                    Producto cl = new Producto(
                            stock, idProducto, nombre, precio, esActivo, fechaActivo
                    );
                    personList.add(cl);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    //Facturas
    public static String GetFacturas(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class GetFacturasTask extends AsyncTask<String, Void, List<Factura>> {

        List<Factura> personList = new ArrayList<>();

        @Override
        public List<Factura> doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.GetFacturas(apiUrl);
                JSONObject responseJson = new JSONObject(jsonString);
                JSONArray dataArray = responseJson.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonObject = dataArray.getJSONObject(i);

                    int idVenta = Integer.parseInt(jsonObject.getString("idVenta"));
                    String cliente = jsonObject.getString("cliente");
                    double total = Double.parseDouble(jsonObject.getString("total"));
                    String fechaRegistro = jsonObject.getString("fechaRegistro");

                    Factura cl = new Factura(
                            idVenta, "EJEMPLO", "EJEMPLO",
                            "EJEMPLO", cliente, total,
                            fechaRegistro);
                    personList.add(cl);
                }

            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    public static String GetFacturasID(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    public static class GetFacturasIDTask extends AsyncTask<String, Void, JSONObject> {

        JSONObject personList = new JSONObject();

        @Override
        public JSONObject doInBackground(String... urls) {
            String apiUrl = urls[0];

            try {
                String jsonString = ApiHandler.GetFacturasID(apiUrl);
                personList = new JSONObject(jsonString);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return personList;
        }
    }

    //Posts
    public static String CrearDataAsync(String apiUrl, JSONObject postData, OnPostDataListener listener) {
        new CrearDataTask(listener).execute(apiUrl, postData.toString());
        return apiUrl;
    }

    private static class CrearDataTask extends AsyncTask<String, Void, String> {
        private OnPostDataListener listener;

        public CrearDataTask(OnPostDataListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            String postData = params[1];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                byte[] postDataBytes = postData.getBytes("UTF-8");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postDataBytes);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    throw new IOException("Error en la solicitud POST. Código de respuesta: " + responseCode);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                listener.onPostDataSuccess(response);
            } else {
                listener.onPostDataError(new IOException("Error en la solicitud POST."));
            }
        }
    }


    //Deletes
    public static void deleteAsync(String apiUrl, OnDeleteDataListener listener) {
        new DeleteTask(listener).execute(apiUrl);
    }

    private static class DeleteTask extends AsyncTask<String, Void, String> {
        private OnDeleteDataListener listener;

        public DeleteTask(OnDeleteDataListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    throw new IOException("Error en la solicitud DELETE. Código de respuesta: " + responseCode);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                listener.onDeleteDataSuccess(response);
            } else {
                listener.onDeleteDataError(new IOException("Error en la solicitud DELETE."));
            }
        }
    }

    //Update
    public static void ActualizarAsync(String apiUrl, JSONObject updateData, OnUpdateDataListener listener) {
        new ActualizarTask(apiUrl, updateData, listener).execute();
    }

    private static class ActualizarTask extends AsyncTask<Void, Void, String> {
        private String apiUrl;
        private JSONObject updateData;
        private OnUpdateDataListener listener;

        public ActualizarTask(String apiUrl, JSONObject updateData, OnUpdateDataListener listener) {
            this.apiUrl = apiUrl;
            this.updateData = updateData;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Envía los datos JSON en el cuerpo de la solicitud PUT
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(updateData.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Lectura de la respuesta
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    throw new IOException("Error en la solicitud PUT. Código de respuesta: " + responseCode);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                listener.onUpdateDataSuccess(response);
            } else {
                listener.onUpdateDataError(new IOException("Error en la solicitud PUT."));
            }
        }
    }

    //Login
    public static Cliente LoginAsync(String apiUrl, JSONObject postData, OnPostDataListener listener) throws ExecutionException, InterruptedException {
        String valor = new LoginTask(listener).execute(apiUrl).get();
        Cliente cl = null;

        try {
            JSONObject responseJson = new JSONObject(valor);
            JSONArray dataArray = new JSONArray();
            dataArray.put(responseJson.getJSONObject("data"));

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject = dataArray.getJSONObject(i);
                String contrasenia = jsonObject.getString("contraseña");
                String telefono = jsonObject.getString("telefono");
                String apellido = jsonObject.getString("apellido");
                String nombre = jsonObject.getString("nombre");
                String direccion = jsonObject.getString("direccion");
                boolean activo = Boolean.parseBoolean(jsonObject.getString("activo"));
                String cedula = jsonObject.getString("cedula");
                String correo = jsonObject.getString("correo");
                int codigoCliente = Integer.parseInt(jsonObject.getString("codigoCliente"));
                String rol = jsonObject.getString("rol");

                cl = new Cliente(
                        codigoCliente, nombre, apellido, activo,
                        direccion, telefono, cedula, rol,
                        correo, contrasenia);
            }

        } catch (JSONException e) {
            Log.d("ERROR", e.toString());
        }

        return cl;
    }

    public static class LoginTask extends AsyncTask<String, Void, String> {
        private OnPostDataListener listener;

        public LoginTask(OnPostDataListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } else {
                    throw new IOException("Error en la solicitud POST. Código de respuesta: " + responseCode);
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                listener.onPostDataSuccess(response);
            } else {
                listener.onPostDataError(new IOException("Error en la solicitud POST."));
            }
        }
    }

}
