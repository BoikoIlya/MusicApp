import com.example.musicapp.app.core.SharedPref


data class TokenDto(
   private val access_token: String,
   private val expires_in: Int,
   private val token_type: String
){

    fun map(cache: SharedPref<String>) = cache.save(access_token)

}