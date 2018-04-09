package com.jldev.mysnack.Navigation.NavigateClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jldev.mysnack.*;
import com.jldev.mysnack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jeffr on 5-4-2018.
 */

public class RouteHandler {

    private GoogleMap mMap;
    private Activity context;
    public static RequestQueue mapQueue;
    private List<List<LatLng>> lines;
    private Map<Integer,List<Polyline>> polylinesMap;
    private double distance = 0;


    public RouteHandler(Activity context, LatLng origin, LatLng dest, GoogleMap mMap){
        this.mMap = mMap;
        this.context = context;

        lines = new ArrayList<>();
        mapQueue = Volley.newRequestQueue(context);
        String url = getUrl(origin,dest);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,response -> {
            synchronized (lines){
                try {
                    System.out.println("THIS IS RESPONSE: "+response);
                    JSONArray jRoutes = response.getJSONArray("routes");
                    JSONArray jLegs = jRoutes.getJSONObject(0).getJSONArray("legs");


                    distance = Math.round(jLegs.getJSONObject(0).getJSONObject("distance").getInt("value") / 1000);


                    for (int j = 0; j < jLegs.length(); j++) {
                        JSONArray step = jLegs.getJSONObject(j).getJSONArray("steps");
                        for (int k = 0; k < step.length(); k++) {
                            JSONObject object = step.getJSONObject(k);
                            String polyline = object.getJSONObject("polyline").getString("points");
                            List<LatLng> list = decodePoly(polyline);
                            lines.add(list);
                        }
                    }
                    List<Polyline> polylines = new ArrayList<>();
                    for (PolylineOptions polylineOptions : getPolylineOptions()) {
                        Polyline p = mMap.addPolyline(polylineOptions);
                        polylines.add(p);
                    }
                    lines.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, onError);

        mapQueue.add(jsonObjectRequest);
    }

    public String getUrl(LatLng origin, LatLng dest){
        StringBuilder dirUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        dirUrl.append("origin=" + origin.latitude + "," + origin.longitude);
        dirUrl.append("&destination=" + dest.latitude + "," + dest.longitude);
        dirUrl.append("&mode=bicycling");
        dirUrl.append("&key=" + context.getString(R.string.Server_google_maps_key));
        Log.d("getUrl LOOK HERE :", dirUrl.toString());
        return (dirUrl.toString());
    }

    public Response.ErrorListener onError = (VolleyError error) -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Oeps")
                .setTitle("something went wrong")
                .setNeutralButton("OK", (dialogInterface, i) ->
                {
                    //do something
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d("Connection failed: ", error.toString());
    };

    public List<PolylineOptions> getPolylineOptions(){
        List<PolylineOptions> polylineOptions = new ArrayList<>();
        LatLng prevPosition = null;

        for (List<LatLng> line : lines) {
            for (LatLng latLng : line) {
                if(prevPosition == null){
                    prevPosition = latLng;
                }else {
                    polylineOptions.add(new PolylineOptions().width(10).color(Color.GREEN).add(prevPosition).add(latLng));
                    prevPosition = latLng;
                }
            }
        }

        return polylineOptions;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
