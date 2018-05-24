import com.kempo.spellcheck.application.Analyzer;
import com.kempo.spellcheck.application.Organizer;
import org.junit.Test;

public class OperatorTest {

    private static Analyzer analyzer = new Analyzer();
    private static Organizer organizerInstance = new Organizer();

    @Test
    public void start() throws Exception {
        analyzer.setInput("Home");
        organizerInstance.loadList();
        analyzer.start(organizerInstance);
    }
}
