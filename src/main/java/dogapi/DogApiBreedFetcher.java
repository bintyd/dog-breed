package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url("https://dog.ceo/api/breeds/list/all")
            .build();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed)  {

        try {
            Response response = client.newCall(request).execute();
            String jsonstring = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonstring);
            ArrayList<String> breeds = new ArrayList<>();
            JSONObject json_breeds = new JSONObject(jsonObject.get("message"));
            JSONArray subbreeds = json_breeds.getJSONArray(breed);
            for (int i = 0; i <= subbreeds.length(); i++) {
                breeds.add(subbreeds.getString(i));
            }
            return breeds;

        } catch (IOException e) {
            throw new BreedNotFoundException(breed);

        }
    }
}