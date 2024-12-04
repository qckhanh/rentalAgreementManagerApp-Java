package Manager;

import Model.Owner;
import Model.Preview;

import java.util.List;

public interface MangerInterface {
    String REPORT_PATH = "BuildAConsoleApp/src/Reports/RENTAL_AGREEMENT_REPORT.txt";

    boolean add();
    boolean remove();
    boolean update();
    boolean displayAll();
    List<List<String>> convertToTable(List<?> objects);
    void makeReport();
}
