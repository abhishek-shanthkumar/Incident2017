package in.co.inci17.auxiliary;
/*
 * Created by Abhishek on 19-01-2017.
 */

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CustomRequest extends Request<JSONArray> {

    private Response.Listener<JSONArray> listener;
    private HashMap<String, String> params;
    private boolean shouldCache = false;

    public CustomRequest(String url, HashMap<String, String> params, boolean shouldCache, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = listener;
        this.params = params;
        this.params.put(Constants.Keys.HASH, Constants.PASSPHRASE);
        this.shouldCache = shouldCache;
    }

    public CustomRequest(String url, HashMap<String, String> params, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        this(url, params, false, listener, errorListener);
    }

    @Override
    protected Map<String,String> getParams(){
        return params;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(jsonString.charAt(0)!='[')
                jsonString = "["+jsonString+"]";
            return Response.success(new JSONArray(jsonString), parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 3 mins, ttl == 24 hours.
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    private Cache.Entry parseCacheHeaders(NetworkResponse response) {
        if (shouldCache) {
            return parseIgnoreCacheHeaders(response);
        } else {
            return HttpHeaderParser.parseCacheHeaders(response);
        }
    }

    private static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = 0 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}
