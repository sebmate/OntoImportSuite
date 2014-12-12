
import com.hp.hpl.jena.ontology.OntModel;

public class SourceStatementDeleter extends SourceStatementCreator {

    SourceStatementDeleter(OntModel ontModel, String mappingSystemNameSpace, String rightNameSpace) {
        super(ontModel, mappingSystemNameSpace, rightNameSpace);
    }

    void deleteStatement() {
        ontModel.remove(ontModel.createStatement(subject, predicate, object));
    }
}
