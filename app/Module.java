import LunchManCore.Storage;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import play.Environment;
import services.CSVRepository;
import services.PostgresRepository;

public class Module extends AbstractModule{

    @Override
    protected void configure() {
        bind(Storage.class).to(PostgresRepository.class);
    }

    @Provides
    CSVRepository provideCSVRepository() {
        CSVRepository csv = new CSVRepository(Environment.simple(), "conf/resources/apprentices.csv", "conf/resources/restaurants.csv", "conf/resources/schedule.csv", "conf/resources/employees.csv", "conf/resources/guests.csv");
        return csv;
    }
}
