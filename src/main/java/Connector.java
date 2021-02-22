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

    //приветствие просто так
    public void printGreeting( final String message  ) {
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

    //Создание узла от класса Person в БД
    public void CreateNode(Person person) {
        try ( Session session = driver.session() ) {
            String createPNode = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String role = person.getRole();
                    String name = person.getName();

                    Result result = tx.run( "CREATE (a:" + role +
                                    "{name: $name} ) " +
                                    "RETURN a.name + ', from node ' + id(a)",
                            parameters( "name", name) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPNode );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание узла Movie в БД
    public void CreateNode(Movie movie) {
        try ( Session session = driver.session() ) {
            String createPNode = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String mtitle = movie.getTitle();

                    Result result = tx.run( "CREATE (a:Movie {title: $mtitle} ) " +
                                    "RETURN a.title + ', from node ' + id(a)",
                            parameters( "mtitle", mtitle) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPNode );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание узла Genre в БД
    public void CreateNode(Genre genre) {
        try ( Session session = driver.session() ) {
            String createPNode = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String gtitle = genre.getTitle();

                    Result result = tx.run( "CREATE (a:Genre {title: $gtitle} ) " +
                                    "RETURN a.title + ', from node ' + id(a)",
                            parameters( "gtitle", gtitle) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPNode );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание связи Person->Movie в БД
    public void CreateRelation( Person person, Movie movie, final String relation) {
        try ( Session session = driver.session() ) {
            String createPM = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String prole = person.getRole();
                    String pname = person.getName();
                    String mtitle = movie.getTitle();

                    Result result = tx.run( "MATCH (a:" + prole +"), (b:Movie) " +
                                    "WHERE a.name = $pname " +
                                    "AND b.title = $mtitle " +
                                    "CREATE (a)-[r:" + relation + "]->(b) " +
                                    "RETURN type(r)",
                            parameters( "pname", pname, "mtitle", mtitle,
                                    "relation", relation ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPM );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание связи Movie->Person в БД
    public void CreateRelation( Movie movie, Person person, final String relation) {
        try ( Session session = driver.session() ) {
            String createMP = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String prole = person.getRole();
                    String pname = person.getName();
                    String mtitle = movie.getTitle();

                    Result result = tx.run( "MATCH (a:Movie), (b:" + prole + ") " +
                                    "WHERE a.title = $mtitle " +
                                    "AND b.name = $pname " +
                                    "CREATE (a)-[r:" + relation + "]->(b) " +
                                    "RETURN type(r)",
                            parameters( "mtitle", mtitle, "pname", pname,
                                    "relation", relation ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createMP );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание связи Person->Person в БД
    public void CreateRelation( Person per1, Person per2, final String relation) {
        try ( Session session = driver.session() ) {
            String createPP = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String p1name = per1.getName();
                    String p2name = per2.getName();
                    String p1role = per1.getRole();
                    String p2role = per2.getRole();

                    Result result = tx.run( "MATCH (a:" + p1role + "), (b:" + p2role + ") " +
                                    "WHERE a.name = $p1name " +
                                    "AND b.name = $p2name " +
                                    "CREATE (a)-[r:" + relation + "]->(b) " +
                                    "RETURN type(r)",
                            parameters( "p1name", p1name, "p2name", p2name,
                                    "relation", relation ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPP );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание связи Person->Genre в БД
    public void CreateRelation( Person person, Genre genre, final String relation) {
        try ( Session session = driver.session() ) {
            String createPG = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String pname = person.getName();
                    String prole = person.getRole();
                    String gtitle = genre.getTitle();

                    Result result = tx.run( "MATCH (a:" + prole + "), (b:Genre) " +
                                    "WHERE a.name = $pname " +
                                    "AND b.title = $gtitle " +
                                    "CREATE (a)-[r:" + relation + "]->(b) " +
                                    "RETURN type(r)",
                            parameters( "pname", pname, "gtitle", gtitle,
                                    "relation", relation ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPG );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //Создание связи Movie->Genre в БД
    public void CreateRelation( Movie movie, Genre genre, final String relation) {
        try ( Session session = driver.session() ) {
            String createPG = session.writeTransaction( new TransactionWork<String>() {
                @Override
                public String execute( Transaction tx )
                {
                    String mtitle = movie.getTitle();
                    String gtitle = genre.getTitle();

                    Result result = tx.run( "MATCH (a:Movie), (b:Genre) " +
                                    "WHERE a.title = $mtitle " +
                                    "AND b.title = $gtitle " +
                                    "CREATE (a)-[r:" + relation + "]->(b) " +
                                    "RETURN type(r)",
                            parameters( "mtitle", mtitle, "gtitle", gtitle,
                                    "relation", relation ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( createPG );
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    //удаление узла в БД
    public void DeleteNode( final String message ) {
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

    //удаление связи в БД
    public void DeleteRelation( final String message ) {
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

    //Найти узел в БД
    public void FindNode( final String message ) {
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