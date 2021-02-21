import com.sun.tools.internal.xjc.model.SymbolSpace;
import org.neo4j.driver.*;


import org.neo4j.driver.AuthTokens;
        import org.neo4j.driver.Driver;
        import org.neo4j.driver.GraphDatabase;
        import org.neo4j.driver.Result;
        import org.neo4j.driver.Session;
        import org.neo4j.driver.Transaction;
        import org.neo4j.driver.TransactionWork;

        import static org.neo4j.driver.Values.parameters;

public class Connector implements AutoCloseable {
    private final Driver driver;

    public Connector( String uri, String user, String password ) {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void printGreeting( final String message ) {
        try ( Session session = driver.session() ) {
            String greeting = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание узла в БД
    public void CreateNode( final String message ) {
        try ( Session session = driver.session() ) {
            String greeting = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //получение результата в БД
    public void getNode( final String message ) {
        try ( Session session = driver.session() ) {
            String greeting = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                    "SET a.message = $message " +
                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }
}