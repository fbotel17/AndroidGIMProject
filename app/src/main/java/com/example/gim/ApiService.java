import java.util.List;  // Import pour List
import com.example.gim.Medicament;  // Import pour votre classe Medicament
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("medicaments/search")
    Call<List<Medicament>> searchMedicaments(@Query("nom") String nom);
}
