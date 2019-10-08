import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wl.songapp.data.db.SongsDatabase
import com.wl.songapp.data.entity.SongEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var songDb: SongsDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        songDb = Room
            .inMemoryDatabaseBuilder(context, SongsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        songDb.close()
    }

    @Test
    fun insertSongEntity_getSongEntity() {
        val song = SongEntity(0L, "trackName", "name", "1991")
        songDb.songDao().insert(song)
        val returnedSong = songDb.songDao().getSongs().first()
        assert(song.equals(returnedSong))
        songDb.songDao().delete(returnedSong)
    }

    @Test
    fun getSong_returnNull() {
        val campaignEntity = songDb.songDao().getSongs()
        assert(!campaignEntity.any())
    }

    //todo more tests
}